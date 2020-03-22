package com.example.flupic.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.flupic.BaseApplication
import com.example.flupic.R
import com.example.flupic.databinding.FragmentAuthBinding
import com.example.flupic.di.subcomponent.AuthComponent
import com.example.flupic.model.AuthenticationUser
import com.example.flupic.model.PhoneVerification
import com.example.flupic.result.Event
import com.example.flupic.result.EventObserver
import com.example.flupic.ui.activity.LaunchDestination
import com.example.flupic.ui.base.BaseAuthFragmentChild
import com.example.flupic.util.*
import com.example.flupic.util.exception.FirebaseAuthError
import com.example.flupic.util.handler.PhoneNumberVerificationHandler
import com.example.flupic.util.handler.PhoneNumberVerificationRequiredException
import com.example.flupic.util.handler.PhoneProviderResponseHandler
import com.example.flupic.util.handler.SmartLockHandler
import com.example.flupic.util.view.SnackbarMessage
import com.example.flupic.util.view.SnackbarMessageManager
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject


class AuthFragment : Fragment(), ProgressView {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var authComponent: AuthComponent

    private lateinit var verificationHandler: PhoneNumberVerificationHandler
    private lateinit var providerResponseHandler: PhoneProviderResponseHandler
    private lateinit var smartLockHandler: SmartLockHandler

    private lateinit var authViewModel: AuthViewModel

    private lateinit var binding: FragmentAuthBinding

    private val navHostFragment: NavHostFragment
        get() = (childFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment)

    private val navController: NavController
        get() = navHostFragment.navController

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager
    private val snackBarMessage = MutableLiveData<Event<SnackbarMessage>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        setupUI()

        return binding.root
    }

    private fun setupUI(){ setUpSnackbar(snackBarMessage, binding.snackbar, snackbarMessageManager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        verificationHandler =  viewModelProvider(viewModelFactory)
        providerResponseHandler = viewModelProvider(viewModelFactory)
        smartLockHandler = viewModelProvider(viewModelFactory)
        authViewModel = viewModelProvider(viewModelFactory)

        verificationHandler.onRestoreInstanceState(savedInstanceState)

        setupAuthFlow()
    }

    private fun setupAuthFlow(){
        setupSmartLockHandlerObserver()

        setupPhoneProviderResponseHandlerObserver()

        setupPhoneVerifierHandlerObserver()

        authViewModel.launchDestination.observe(this, EventObserver{
            when(it){
                LaunchDestination.APPLICATION -> navigateToProfileFragment()
                LaunchDestination.FULLY_REGISTRATION -> navigateToInfoSupplyFragment()
                else -> {}
            }
        })
    }

    private fun setupPhoneVerifierHandlerObserver(){
        verificationHandler.operationResult.observe(this, object : AuthResultObserver<PhoneVerification>(this) {

            override fun onSuccess(t: PhoneVerification) {
                if (t.isAutoVerified) {
                        showAuthFlowMessage(R.string.automatically_verified_number)

                        if(navController.currentDestination?.id == R.id.confirmationFragment)
                            navController.popBackStack()
                    }

                providerResponseHandler.startSignIn(t.credential, AuthenticationUser(pendingCredential = t.credential))
            }

            override fun onFailure(e: Exception) {
                if (e is PhoneNumberVerificationRequiredException) {

                    if(navController.currentDestination?.id != R.id.confirmationFragment)
                        navigateToConfirmationFragment(e.number)

                } else handleError(e)
            }
        })
    }

    private fun setupPhoneProviderResponseHandlerObserver(){
        providerResponseHandler.operationResult.observe(this, object : AuthResultObserver<AuthenticationUser>(this){

            override fun onSuccess(t: AuthenticationUser) {
                startSaveCredentials(providerResponseHandler.getCurrentUser(), t)
            }

            override fun onFailure(e: Exception) { handleError(e) }
        })

    }

    private fun setupSmartLockHandlerObserver(){
        smartLockHandler.operationResult.observe(this, object : AuthResultObserver<AuthenticationUser>(this){
            // Equals since we don't want to halt sign-in just because of a credential save error.

            override fun onSuccess(t: AuthenticationUser) { authViewModel.finishAuthFlow() }
            override fun onFailure(e: Exception) { authViewModel.finishAuthFlow() }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        verificationHandler.onSaveInstanceState(outState)
    }

    private fun navigateToConfirmationFragment(number: String){
        val action = SignInFragmentDirections.actionSignInFragmentToConfirmationFragment(number)
        navController.navigate(action)
    }

    private fun startSaveCredentials(firebaseUser: FirebaseUser?, authenticationUser: AuthenticationUser){

        val credential = buildCredential(firebaseUser, authenticationUser.password, authenticationUser.accountType)

        if(smartLockHandler.operationResult.value == null)
            smartLockHandler.saveCredentials(credential, authenticationUser)
        else Log.i(TAG, "Save operation in progress, doing nothing")

    }

    private fun navigateToInfoSupplyFragment(){
        findNavController().navigate(R.id.infoSupplyFragment)
    }

    private fun navigateToProfileFragment(){
        findNavController().navigate(R.id.profileFragment)
    }

    private fun handleError(e: Exception){
        if(e is FirebaseAuthException){
            showAuthFlowMessage(FirebaseAuthError.fromException(e).toStringResource())
        }else{
            Log.e(FirebaseAuthError.TAG, e.message.toString())
            showAuthFlowMessage(R.string.encapsulated_error)
        }

    }

    private fun showAuthFlowMessage(@StringRes messageId: Int){
        snackBarMessage.value = Event(
            SnackbarMessage(
                messageId = messageId,
                longDuration = true
            )
        )
    }

    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        authComponent = this.authComponent().create()
        authComponent.inject(this@AuthFragment)
    }

    override fun showProgress() {
        binding.authProgress.visibility = View.VISIBLE
        (getAuthChildFragment() as? ProgressView)?.showProgress()
    }

    override fun hideProgress() {
        binding.authProgress.visibility = View.INVISIBLE
        (getAuthChildFragment() as? ProgressView)?.hideProgress()
    }

    private fun getAuthChildFragment(): BaseAuthFragmentChild? =
        (navHostFragment.childFragmentManager.fragments[0] as? BaseAuthFragmentChild)


    companion object{
        private const val TAG = "TAG AuthFragment"
    }
}
