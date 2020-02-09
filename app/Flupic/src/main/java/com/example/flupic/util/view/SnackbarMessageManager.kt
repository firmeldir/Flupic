package com.example.flupic.util.view

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import com.example.flupic.result.Event
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SnackbarMessageManager @Inject constructor(){

    companion object {
        const val MAX_ITEMS = 10
    }

    private val messages = mutableListOf<Event<SnackbarMessage>>()

    private val result = MutableLiveData<Event<SnackbarMessage>>()

    fun addMessage(msg: SnackbarMessage) {

        // If the new message is about the same change as a pending one, keep the new one. (rare)
        val sameRequestId = messages.filter {
            it.peekContent().requestChangeId == msg.requestChangeId && !it.hasBeenHandled
        }
        if (sameRequestId.isNotEmpty()) {
            messages.removeAll(sameRequestId)
        }

        // If the new message is about a change that was already notified, ignore it.
        val alreadyHandledWithSameId = messages.filter {
            it.peekContent().requestChangeId == msg.requestChangeId && it.hasBeenHandled
        }

        // Only add the message if it hasn't been handled before
        if (alreadyHandledWithSameId.isEmpty()) {
            messages.add(Event(msg))
            loadNextMessage()
        }

        // Remove old messages
        if (messages.size > MAX_ITEMS) {
            messages.retainAll(messages.drop(messages.size - MAX_ITEMS))
        }
    }

    fun loadNextMessage() {
        result.postValue(messages.firstOrNull { !it.hasBeenHandled })
    }

    fun observeNextMessage(): MutableLiveData<Event<SnackbarMessage>> {
        return result
    }
}