package com.finance.movieslisttmdb.screens.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.finance.movieslisttmdb.R
import com.finance.movieslisttmdb.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var _binding: ActivitySplashBinding
    private val DELAY: Long = 2300
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        if (mainLooper != null) {
            Handler(mainLooper).postDelayed({
                moveToHomeScreen()
            }, DELAY)
        }
    }

    private fun moveToHomeScreen() {
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}