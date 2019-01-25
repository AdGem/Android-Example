package com.adgem.android.example

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class AdGemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        setContentView(R.layout.activity_adgem)
    }
}
