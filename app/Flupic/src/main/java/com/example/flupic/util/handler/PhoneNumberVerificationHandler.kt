package com.example.flupic.util.handler

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.flupic.di.subcomponent.AuthScope
import com.example.flupic.ui.base.OperableViewModel
import com.example.flupic.model.PhoneVerification
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AuthScope
class PhoneNumberVerificationHandler @Inject constructor(
    application: Application,
    private val phoneAuth: PhoneAuthProvider
): OperableViewModel<PhoneVerification>(application){

    private var verificationId: String? = null
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null

    private val _delayBeforeResend = MutableLiveData<Long>()
    val delayBeforeResend : LiveData<Long>
        get() = _delayBeforeResend

    companion object{
        private const val VERIFICATION_ID_KEY = "verification-id"
        private const val AUTO_RETRIEVAL_TIMEOUT_SECONDS: Long = 120
        private const val DELAY_TIME_MILLIS: Long = 15000
        private const val TICK_TIME_MILLIS: Long = 1000
    }


    private fun launchResendTimer(){
        viewModelScope.launch {
            for(i: Long in DELAY_TIME_MILLIS/ TICK_TIME_MILLIS downTo 0 step 1){
                _delayBeforeResend.value = i
                delay(TICK_TIME_MILLIS)
            }
        }
    }

    fun verifyPhoneNumber(number: String, force: Boolean){
        onOperationLoading()

        phoneAuth.verifyPhoneNumber(
            number,
            AUTO_RETRIEVAL_TIMEOUT_SECONDS,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    setOperationResult(PhoneVerification(number, credential, true))
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    setOperationResult(e)
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = id
                    forceResendingToken = token

                    launchResendTimer()

                    setOperationResult(
                        PhoneNumberVerificationRequiredException(
                            number
                        )
                    )
                }
            },
            if(force) forceResendingToken else null
        )
    }

    fun submitVerificationCode(number: String, code: String){
        setOperationResult(
            PhoneVerification(number, PhoneAuthProvider.getCredential(verificationId!!, code), false)
        )
    }

    fun onSaveInstanceState(outState: Bundle){
        outState.putString(VERIFICATION_ID_KEY, verificationId)
    }

    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (verificationId == null && savedInstanceState != null) {
            verificationId = savedInstanceState.getString(VERIFICATION_ID_KEY)
        }
    }
}

data class PhoneNumberVerificationRequiredException(val number: String)
    : Exception("Phone number requires verification")