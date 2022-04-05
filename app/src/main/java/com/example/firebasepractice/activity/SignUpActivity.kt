package com.example.firebasepractice.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebasepractice.R
import com.example.firebasepractice.databinding.ActivitySignUpBinding
import com.example.firebasepractice.managers.AuthHandler
import com.example.firebasepractice.managers.AuthManager
import com.example.firebasepractice.utils.Extensions.toast
import java.lang.Exception

class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bSignup.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            firebaseSignUp(email, password)

        }
    }

    fun firebaseSignUp(email: String, password: String) {
        showLoading(this)
        AuthManager.signUp(email, password, object : AuthHandler {
            override fun onSuccess() {
                toast("Signed up successfully")
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast("Sign up failed")
            }

        })
    }
}