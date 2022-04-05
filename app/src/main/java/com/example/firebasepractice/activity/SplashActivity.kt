package com.example.firebasepractice.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import com.example.firebasepractice.R
import com.example.firebasepractice.managers.AuthManager

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        countDownTimer()

    }

    private fun countDownTimer(){
        object : CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {

            }
            override fun onFinish() {
                if (AuthManager.isSignedIn()){
                    callMainActivity(this@SplashActivity)
                }else {
                    callSignInActivity(this@SplashActivity)
                }

            }
        }.start()
    }


}