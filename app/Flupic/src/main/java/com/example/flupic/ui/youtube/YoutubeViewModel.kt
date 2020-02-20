package com.example.flupic.ui.youtube

import android.util.Log
import androidx.lifecycle.*
import com.example.flupic.data.video.VideoTrack
import com.example.flupic.domain.video.GetVideoTrackParameters
import com.example.flupic.domain.video.GetVideoTrackUseCase
import com.example.flupic.result.Event
import com.example.flupic.util.map
import com.example.flupic.util.view.SnackbarMessage
import javax.inject.Inject

class YoutubeViewModel @Inject constructor(
    videoPlayerControllerDelegate: VideoPlayerControllerDelegate,
    private val getVideoTrackUseCase: GetVideoTrackUseCase
) : ViewModel(), VideoPlayerControllerDelegate by videoPlayerControllerDelegate{

    private val _snackBarMessage = MediatorLiveData<Event<SnackbarMessage>>()
    val snackBarMessage: LiveData<Event<SnackbarMessage>>
        get() = _snackBarMessage

    private val getVideoTrackResult = MutableLiveData<com.example.flupic.result.Result<VideoTrack>>()

    val currentVideoTrack: LiveData<VideoTrack?>

    init {

        currentVideoTrack = getVideoTrackResult.map {
            Log.i("VLAD", "90 ! ")
            val videoTrack = (it as? com.example.flupic.result.Result.Success)?.data
            Log.i("VLAD", "5 ! ${videoTrack?.videoId}")
            launchVideoPlayer(videoTrack?.videoId ?: "")
            videoTrack
        }

        _snackBarMessage.addSource(getVideoTrackResult){
            if (it is com.example.flupic.result.Result.Error) {
//                _snackBarMessage.value =
//                    Event(
//                        SnackbarMessage(
//                            messageId = R.string.feed_loading_error,
//                            longDuration = true
//                        )
//                    )
            }
        }
    }

    fun play(title: String, artist: String){
        Log.i("VLAD", "$title  $artist")
        getVideoTrackUseCase(GetVideoTrackParameters(title, artist), getVideoTrackResult, viewModelScope)
    }
}