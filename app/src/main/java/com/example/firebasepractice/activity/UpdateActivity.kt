package com.example.firebasepractice.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.example.firebasepractice.MainActivity
import com.example.firebasepractice.R
import com.example.firebasepractice.databinding.ActivityUpdateBinding
import com.example.firebasepractice.managers.DatabaseHandler
import com.example.firebasepractice.managers.DatabaseManager
import com.example.firebasepractice.model.Post

class UpdateActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var id:String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id").toString()
        Log.d("idddd", id)
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        binding.etTitle.text = Editable.Factory.getInstance().newEditable(title)
        binding.etBody.text = Editable.Factory.getInstance().newEditable(body)

        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.bUpdate.setOnClickListener {
            updateDatabase(id, Post(binding.etTitle.text.toString().trim(), binding.etBody.text.toString().trim()))

        }
    }

    fun updateDatabase(key: String, post: Post){
        showLoading(this)
        DatabaseManager.apiUpdatePost( key,post,object : DatabaseHandler{
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                finishIntent()
            }

            override fun onError() {

            }

        })
    }

    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}