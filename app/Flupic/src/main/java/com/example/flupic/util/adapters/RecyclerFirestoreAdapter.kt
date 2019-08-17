package com.example.flupic.util.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.flupic.TAG
import com.google.firebase.firestore.*

abstract class RecyclerFirestoreAdapter<VH : RecyclerView.ViewHolder>(val query: Query) : RecyclerView.Adapter<VH>() , EventListener<QuerySnapshot>{

    protected val snapshots: MutableList<DocumentSnapshot> = mutableListOf()
    protected var listener: ListenerRegistration? = null

    fun startListening(){
        if(listener == null) {
            listener = query.addSnapshotListener(this)
        }
    }

    fun stopListening(){
        listener?.remove()
        listener = null

        snapshots.clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = snapshots.size

    private fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots.set(change.oldIndex, change.document)
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {

        p1?.let { Log.e(TAG, p1.message) ; return}

        p0?.let {
            for(change in p0.documentChanges){
                when (change.type){
                    DocumentChange.Type.ADDED ->{ onDocumentAdded(change) }
                    DocumentChange.Type.MODIFIED ->{ onDocumentModified(change) }
                    DocumentChange.Type.REMOVED ->{ onDocumentRemoved(change) }
                }
            }
        }
    }
}