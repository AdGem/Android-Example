package com.adgem.android.example

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.adgem.android.AdGem
import com.adgem.android.AdGemCallback
import com.adgem.android.OfferWallCallback
import kotlinx.android.synthetic.main.activity_adgem.*

class AdGemActivity : AppCompatActivity(), AdGemCallback, OfferWallCallback {
    private val adGem by lazy { AdGem.get() }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adgem)

        adGemSdkVersionTextView.text = getString(R.string.adgem_version, BuildConfig.ADGEM_VERSION)

        showStandardVideoButton.apply {
            setOnClickListener {
                adGem.showStandardVideoAd(this@AdGemActivity)
            }
            updateWithAdGemState(adGem.standardVideoAdState, R.string.show_standard_video)
        }

        showRewardedVideoButton.apply {
            setOnClickListener {
                adGem.showRewardedVideoAd(this@AdGemActivity)
            }
            updateWithAdGemState(adGem.rewardedVideoAdState, R.string.show_rewarded_video)
        }

        showOfferWallButton.apply {
            setOnClickListener {
                adGem.showOfferWall(this@AdGemActivity)
            }
            updateWithAdGemState(adGem.offerWallState, R.string.show_offer_wall)
        }

        adGem.registerCallback(this)
        adGem.registerOfferWallCallback(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        adGem.unregisterCallback(this)
        adGem.unregisterOfferWallCallback(this)
    }

    override fun onStandardVideoAdStateChanged(newState: Int) {
        showStandardVideoButton.updateWithAdGemState(newState, R.string.show_standard_video)
    }

    override fun onRewardedVideoAdStateChanged(newState: Int) {
        showRewardedVideoButton.updateWithAdGemState(newState, R.string.show_rewarded_video)
    }

    override fun onOfferWallStateChanged(newState: Int) {
        showOfferWallButton.updateWithAdGemState(newState, R.string.show_offer_wall)
    }

    override fun onStandardVideoComplete() {
        showMessage(R.string.done_playing_standard_video)
    }

    override fun onRewardedVideoComplete() {
        showMessage(R.string.done_playing_rewarded_video)
    }

    override fun onRewardUser(amount: Int) {
        showMessage(getString(R.string.user_rewarded, amount))
    }

    override fun onOfferWallClosed() {
        showMessage(R.string.offer_wall_closed_hint)
    }

    private fun showMessage(@StringRes text: Int) {
        showMessage(getString(text))
    }

    private fun showMessage(text: String) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun Button.updateWithAdGemState(state: Int, @StringRes buttonText: Int) {
        if (state == AdGem.STATE_READY) {
            isEnabled = true
            setText(buttonText)
        } else {
            isEnabled = false
            val stateText = when (state) {
                AdGem.STATE_ERROR -> getString(R.string.error)
                AdGem.STATE_DISABLED -> getString(R.string.disabled)
                AdGem.STATE_NEEDS_INITIALIZATION,
                AdGem.STATE_INITIALIZING -> getString(R.string.initializing)
                AdGem.STATE_NEEDS_CAMPAIGN_REFRESH,
                AdGem.STATE_REFRESHING_CAMPAIGN -> getString(R.string.refreshing)
                AdGem.STATE_NEEDS_DOWNLOAD,
                AdGem.STATE_DOWNLOADING -> getString(R.string.downloading)
                else -> null
            }
            if (stateText != null) {
                text = "${getString(buttonText)} ($stateText)"
            }
        }
    }
}
