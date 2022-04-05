package com.example.firebasepractice.managers

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class StorageManager {
    companion object{
        val storage = FirebaseStorage.getInstance()
        var storageRef = storage.getReference()
        var photosRef = storageRef.child("photos")


        fun uploadPhoto(uri: Uri, handler: StorageHandler) {
            val filename = System.currentTimeMillis().toString()

            val uploadTask: UploadTask = photosRef.child(filename).putFile(uri)
            uploadTask.addOnSuccessListener {
                val result = it.metadata!!.reference!!.downloadUrl

                result.addOnSuccessListener {
                    var imgUrl = it.toString()
                    handler.onSuccess(imgUrl)
                }.addOnFailureListener{ e ->
                    handler.onError(e)
                }
            }.addOnFailureListener{e->
                handler.onError(e)
            }
        }
    }
}