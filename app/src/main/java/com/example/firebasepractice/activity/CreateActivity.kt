package com.example.firebasepractice.activity

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebasepractice.R
import com.example.firebasepractice.databinding.ActivityCreateBinding
import com.example.firebasepractice.managers.DatabaseHandler
import com.example.firebasepractice.managers.DatabaseManager
import com.example.firebasepractice.managers.StorageHandler
import com.example.firebasepractice.managers.StorageManager
import com.example.firebasepractice.model.Post
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import java.lang.Exception

class CreateActivity : BaseActivity() {
    private lateinit var binding: ActivityCreateBinding

    var pickedPhoto: Uri? = null
    var allPhotos = ArrayList<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.bCreate.setOnClickListener {
            storePost(Post(binding.etTitle.text.toString().trim(), binding.etBody.text.toString().trim()))
        }

        binding.ivCamera.setOnClickListener {
            pickUserPhoto()
        }
    }

    fun pickUserPhoto(){
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(allPhotos)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }

    val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                allPhotos = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                pickedPhoto = allPhotos.get(0)
                    binding.ivPhoto.setImageURI(pickedPhoto)
            }
        }

    fun storePost(post: Post){
        if (pickedPhoto != null){
            storeStorage(post)
        }else {
            storeDatabase(post)
        }
    }

    private fun storeStorage(post: Post) {
        showLoading(this)
        StorageManager.uploadPhoto(pickedPhoto!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeDatabase(post)
            }

        })

    }

    fun storeDatabase(post: Post) {
        DatabaseManager.storePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("@@@", "post is saved")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("@@@", "post is not saved")
            }
        })
    }

    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}