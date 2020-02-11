package com.example.flupic.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.BaseApplication
import com.example.flupic.databinding.FragmentProfileUserBinding
import com.example.flupic.model.Post
import com.example.flupic.util.setUpSnackbar
import com.example.flupic.util.view.SnackbarMessageManager
import com.example.flupic.util.viewModelProvider
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class UserProfileFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    private lateinit var profileViewModel: UserProfileViewModel
    private lateinit var binding: FragmentProfileUserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()

        profileViewModel = viewModelProvider(viewModelFactory)

        binding = FragmentProfileUserBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = profileViewModel
                info.viewModel = profileViewModel
            }

        setupUI()

        return binding.root
    }

    private fun setupUI(){

        setUpSnackbar(profileViewModel.snackBarMessage, binding.snackbar, snackbarMessageManager)

        setupPosts()
    }

    private fun setupPosts(){ with(binding.content.postsRecyclerView){
        layoutManager = GridLayoutManager(context!!, 2)
        setHasFixedSize(true)
    }
    }

    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@UserProfileFragment)
    }
}
