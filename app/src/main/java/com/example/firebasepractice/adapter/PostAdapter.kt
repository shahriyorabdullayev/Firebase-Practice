package com.example.firebasepractice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasepractice.databinding.ItemPostBinding
import com.example.firebasepractice.model.Post
import com.squareup.picasso.Picasso

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    var posts: ArrayList<Post> = ArrayList()

    inner class PostViewHolder(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun bind() {
            binding.apply {
                tvTitle.text = posts[adapterPosition].title
                tvBody.text = posts[adapterPosition].body
                if (posts[adapterPosition].img.isNotEmpty()){
                    Picasso.get().load(posts[adapterPosition].img).into(img)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
       return PostViewHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = posts.size

    fun submitData(posts: ArrayList<Post>){
        this.posts = posts
        notifyDataSetChanged()
    }
}