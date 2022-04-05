package com.example.firebasepractice.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.firebasepractice.MainActivity
import com.example.firebasepractice.databinding.ActivitySignInBinding
import com.example.firebasepractice.managers.AuthHandler
import com.example.firebasepractice.managers.AuthManager
import com.example.firebasepractice.utils.Extensions.toast
import java.lang.Exception

class SignInActivity : BaseActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bSignin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()){
                firebaseSignIn(email, password)
            }
        }


        binding.tvSignup.setOnClickListener {
            callSignUpActivity()
        }

    }

    fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess() {
                toast("Signed is successfully")
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast("Signed is failed")
            }

        })
    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

}