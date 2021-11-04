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
import com.adgem.android.example.databinding.ActivityAdgemBinding

class AdGemActivity : AppCompatActivity(), AdGemCallback, OfferWallCallback {
    private val adGem by lazy { AdGem.get() }
    private lateinit var binding : ActivityAdgemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)

        binding = ActivityAdgemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adGemSdkVersionTextView.text = getString(R.string.adgem_version, BuildConfig.ADGEM_VERSION)

        binding.showStandardVideoButton.apply {
            setOnClickListener {
                adGem.showInterstitialAd(this@AdGemActivity)
            }
            updateWithAdGemState(adGem.interstitialAdState, R.string.show_interstitial_ad)
        }

        binding.showRewardedVideoButton.apply {
            setOnClickListener {
                adGem.showRewardedAd(this@AdGemActivity)
            }
            updateWithAdGemState(adGem.rewardedAdState, R.string.show_rewarded_ad)
        }

        binding.showOfferWallButton.apply {
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

    override fun onInterstitialAdStateChanged(newState: Int) {
        binding.showStandardVideoButton.updateWithAdGemState(newState, R.string.show_interstitial_ad)
    }

    override fun onRewardedAdStateChanged(newState: Int) {
        binding.showRewardedVideoButton.updateWithAdGemState(newState, R.string.show_rewarded_ad)
    }

    override fun onOfferWallStateChanged(newState: Int) {
        binding.showOfferWallButton.updateWithAdGemState(newState, R.string.show_offer_wall)
    }

    override fun onInterstitialAdClosed() {
        showMessage(R.string.done_showing_interstitial_ad)
    }

    override fun onRewardedAdComplete() {
        showMessage(R.string.done_showing_rewarded_ad)
    }

    override fun onRewardedAdCancelled() {
        showMessage(R.string.cancel_showing_rewarded_ad)
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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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
