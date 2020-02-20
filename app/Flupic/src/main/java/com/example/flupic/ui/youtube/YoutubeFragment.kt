package com.example.flupic.ui.youtube


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.flupic.BaseApplication
import com.example.flupic.R
import com.example.flupic.databinding.FragmentYoutubeBinding
import com.example.flupic.ui.profile.UserProfileViewModel
import com.example.flupic.util.view.SnackbarMessageManager
import com.example.flupic.util.viewModelProvider
import javax.inject.Inject


class YoutubeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var youtubeViewModel: YoutubeViewModel

    lateinit var binding: FragmentYoutubeBinding
    private var videoBox: VideoBoxFragment? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inject()
        binding = FragmentYoutubeBinding.inflate(inflater, container, false)
        youtubeViewModel = viewModelProvider(viewModelFactory)

        setupVideoBox()
        setupUI()

        return binding.root
    }

    private fun setupUI(){
        videoBox?.setupVideoPlayerController(youtubeViewModel)

        binding.button.setOnClickListener {
            youtubeViewModel.play(binding.title.text.toString(), binding.artist.text.toString())
        }

        youtubeViewModel.currentVideoTrack.observe(this as LifecycleOwner, Observer {
            Log.i("VLAD", "+++")
        })
    }

    private fun setupVideoBox(){
        videoBox = childFragmentManager.findFragmentById(R.id.videoBoxFragment)
                as VideoBoxFragment?

        if(videoBox == null)Log.e(TAG, "VideoBoxFragment has not initialized")
    }

    private fun inject() = with((activity!!.application as BaseApplication).appComponent){
        this.inject(this@YoutubeFragment)
    }

    companion object{
        private const val TAG = "TAG YoutubeFragment"
    }
}
