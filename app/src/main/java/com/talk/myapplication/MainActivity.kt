package com.talk.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.talk.myapplication.conversation.FragmentConversation
import com.talk.myapplication.databinding.ActivityMainBinding
import com.talk.myapplication.databinding.ActivitySplashBinding
import com.talk.myapplication.databinding.FragmentConversationBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            val fragment = FragmentConversation()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            binding.img.visibility = View.GONE

        }, 2000)

    }
}