package com.jgdeveloppement.android_academie

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jgdeveloppement.android_academie.databinding.ActivityMainBinding
import com.jgdeveloppement.android_academie.home.HomeActivity
import com.jgdeveloppement.android_academie.utils.Utils
import com.rommansabbir.animationx.Zoom
import com.rommansabbir.animationx.animationXZoom

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.myApp = this

        binding.textView.animationXZoom(Zoom.ZOOM_IN_DOWN)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.textView2.setTypedText(getString(R.string.typed_text))
            lifecycle.addObserver(binding.textView2.lifecycleObserver)
        },(1 * 1000).toLong())


        binding.button.setOnClickListener { HomeActivity.navigate(this) }

    }
}