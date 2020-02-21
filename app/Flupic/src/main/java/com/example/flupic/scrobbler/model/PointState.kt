package com.example.flupic.scrobbler.model

import android.media.session.PlaybackState
import com.example.flupic.scrobbler.model.firebase.FirePointState
import java.util.*

class ActivationState(val state: Boolean)  //TODO : Make inline

data class PointState(
    val isActive: Boolean,
    val position: Long
){
    companion object{

        fun from(playbackState: PlaybackState?) : PointState?{
            playbackState ?: return null

            val position = playbackState.position

            return PointState(playbackState.isActive(), position)
        }
    }
}

fun PlaybackState.isActive() = (this.state != 1 && this.state != 2)

//    public static final int STATE_STOPPED = 1;
//    public static final int STATE_PAUSED = 2;

//    public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
//    public static final int STATE_BUFFERING = 6;
//    public static final int STATE_CONNECTING = 8;
//    public static final int STATE_ERROR = 7;
//    public static final int STATE_FAST_FORWARDING = 4;
//    public static final int STATE_NONE = 0;
//    public static final int STATE_PLAYING = 3;
//    public static final int STATE_REWINDING = 5;
//    public static final int STATE_SKIPPING_TO_NEXT = 10;
//    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
//    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;