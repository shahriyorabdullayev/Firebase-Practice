package com.example.firebasepractice

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.*
import com.example.firebasepractice.activity.BaseActivity
import com.example.firebasepractice.activity.CreateActivity
import com.example.firebasepractice.activity.UpdateActivity
import com.example.firebasepractice.adapter.PostAdapter
import com.example.firebasepractice.databinding.ActivityMainBinding
import com.example.firebasepractice.helper.SwipeHelper
import com.example.firebasepractice.managers.AuthManager
import com.example.firebasepractice.managers.DatabaseHandler
import com.example.firebasepractice.managers.DatabaseManager
import com.example.firebasepractice.model.Post
import com.example.firebasepractice.utils.Extensions.toast

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding


    private var postList: ArrayList<Post> = ArrayList()
    private lateinit var postAdapter: PostAdapter

    lateinit var listener:((post:Post) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivLogout.setOnClickListener {
            logOut()
            callSignInActivity(context)
        }

        binding.fabCreate.setOnClickListener {
            callCreateActivity()
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        apiLoadPosts()
        postAdapter = PostAdapter()
        binding.recyclerView.adapter = postAdapter
        postAdapter.submitData(postList)

        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        //binding.recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))


        object : SwipeHelper(this, binding.recyclerView, false) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {
                // Delete Button
                underlayButtons?.add(UnderlayButton("Delete", AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_delete),
                    resources.getColor(R.color.red), Color.parseColor("#FF3700B3")

                ) { pos: Int ->
                    Toast.makeText(this@MainActivity,
                        "Delete clicked at $pos",
                        Toast.LENGTH_SHORT).show()
                    apiDeletePost(postList[pos])
                    postList.clear()


                })
                //update
                underlayButtons?.add(UnderlayButton("Update", AppCompatResources.getDrawable(this@MainActivity, R.drawable.ic_update),
                    resources.getColor(R.color.green), Color.parseColor("#FF3700B3")

                ) { pos: Int ->
                    callUpdateActivity(postList[pos].id!!, postList.get(pos).title!!, postList.get(pos).body!!)

                })
            }
        }
    }

    private fun callUpdateActivity(id:String, title: String, body: String) {
        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("title", title)
        intent.putExtra("body", body)
        updatetLauncher.launch(intent)
    }

    private fun callCreateActivity() {
        val intent = Intent(this, CreateActivity::class.java)
        resultLauncher.launch(intent)
    }

    var updatetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Load all posts...
            apiLoadPosts()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Load all posts...
            apiLoadPosts()
        }
    }

    fun apiLoadPosts() {
        showLoading(this)
        DatabaseManager.apiLoadPosts(object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                dismissLoading()
                postList.clear()
                postList.addAll(posts)
                postAdapter.notifyDataSetChanged()
            }

            override fun onError() {
                dismissLoading()
            }
        })
    }

    fun apiDeletePost(post: Post) {
        DatabaseManager.apiDeletePost(post, object: DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                apiLoadPosts()
                postAdapter.notifyDataSetChanged()
            }

            override fun onError() {

            }
        })
    }

    fun logOut(){
        AuthManager.signOut()
    }
}