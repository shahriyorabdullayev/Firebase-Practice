package com.example.firebasepractice.managers

import android.util.Log
import com.example.firebasepractice.model.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseManager {
    companion object {

        private var database = FirebaseDatabase.getInstance().reference
        private var reference = database.child("myposts")

        fun storePost(post: Post, handler: DatabaseHandler) {
            val key = reference.push().key
            if (key == null) return
            post.id = key
            reference.child(key).setValue(post)
                .addOnSuccessListener {
                    handler.onSuccess()
                }.addOnFailureListener {
                    handler.onError()
                }
        }

        fun apiLoadPosts(handler: DatabaseHandler) {
            reference.addValueEventListener(object : ValueEventListener {
                var posts: ArrayList<Post> = ArrayList()
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (snapshot in dataSnapshot.children) {
                            val post: Post? = snapshot.getValue(Post::class.java)
                            post.let {
                                posts.add(post!!)
                            }
                        }
                        handler.onSuccess(posts = posts)
                    } else {
                        handler.onSuccess(posts = ArrayList())
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    handler.onError()
                }
            })
        }

        fun apiDeletePost(post: Post, handler: DatabaseHandler) {
            val key = post.id
            reference.child(key!!).removeValue()
                .addOnSuccessListener {
                    handler.onSuccess()
                }.addOnFailureListener {
                    handler.onError()
                }
        }

        fun apiUpdatePost(id: String,post: Post, handler: DatabaseHandler){
            val key = id
            val map = HashMap<String, Any>()
            map.put("title", post.title!!)
            map.put("body", post.body!!)
            reference.child(key!!).updateChildren(map)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        handler.onSuccess()
                    }else{
                        handler.onError()
                    }
                }

        }

    }
}