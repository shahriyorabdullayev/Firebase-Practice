package com.example.firebasepractice.managers

import com.example.firebasepractice.model.Post

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}