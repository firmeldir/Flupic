package com.example.flupic.scrobbler.model.firebase

import android.media.session.PlaybackState
import com.example.flupic.scrobbler.model.PointState

/*
*   TODO : Test to periodical sending 'isActive'
*/

data class FirePointState(
    val isActive: Boolean = false,
    val position: Long = 0L
){
    companion object{
        fun from(state: PointState) = FirePointState(state.isActive, state.position)
    }
}



