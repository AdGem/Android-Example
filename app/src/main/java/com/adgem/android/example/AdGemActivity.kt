package com.adgem.android.example

import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.appcompat.app.AppCompatActivity
import com.adgem.android.AdGem
import com.adgem.android.AdGemCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_adgem.*

class AdGemActivity : AppCompatActivity(), AdGemCallback {
    private val adGem by lazy { AdGem.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adgem)

        showStandardVideoButton.setOnClickListener {
            adGem.showStandardVideoAd(this)
        }

        showRewardedVideoButton.setOnClickListener {
            adGem.showRewardedVideoAd(this)
        }

        showOfferWallButton.setOnClickListener {
            adGem.showOfferWall(this)
        }

        adGem.registerCallback(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        adGem.unregisterCallback(this)
    }

    override fun onStandardVideoAdStateChanged(newState: Int) {
        showStandardVideoButton.isEnabled = newState == AdGem.STATE_READY
    }

    override fun onRewardedVideoAdStateChanged(newState: Int) {
        showRewardedVideoButton.isEnabled = newState == AdGem.STATE_READY
    }

    override fun onOfferWallStateChanged(newState: Int) {
        showOfferWallButton.isEnabled = newState == AdGem.STATE_READY
    }

    override fun onStandardVideoComplete() {
        showMessage("Done playing standard video")
    }

    override fun onRewardUser(amount: Int) {
        showMessage("User receives $amount")
    }

    override fun onRewardedVideoComplete() {
        showMessage("Done playing rewarded video")
    }

    private fun showMessage(text: String) {
        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT).show()
    }
}
