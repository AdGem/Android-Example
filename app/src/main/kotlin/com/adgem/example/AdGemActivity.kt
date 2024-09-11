package com.adgem.example

import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.adgem.android.AdGem
import com.adgem.android.BuildConfig
import com.adgem.android.OfferwallCallback
import com.adgem.android.PlayerMetadata
import com.adgem.example.databinding.ActivityAdgemBinding
import java.util.UUID

class AdGemActivity : AppCompatActivity(), OfferwallCallback {
    private val adGem by lazy { AdGem.get() }
    private lateinit var binding: ActivityAdgemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(FLAG_LAYOUT_NO_LIMITS, FLAG_LAYOUT_NO_LIMITS)
        super.onCreate(savedInstanceState)

        binding = ActivityAdgemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.adGemSdkVersionTextView.text =
            getString(R.string.adgem_version, BuildConfig.VERSION_NAME)

        binding.showOfferWallButton.apply {
            setOnClickListener {
                adGem.showOfferwall(this@AdGemActivity)
            }
        }

        adGem.registerOfferwallCallback(this)

        applyAdGemPlayerId()
    }

    override fun onDestroy() {
        super.onDestroy()
        adGem.unregisterOfferwallCallback(this)
    }

    override fun onOfferwallRewardReceived(amount: Int) {
        showMessage(getString(R.string.user_rewarded, amount))
    }

    override fun onOfferwallClosed() {
        showMessage(R.string.offer_wall_closed_hint)
    }

    override fun onOfferwallLoadingFailed(error: String?) {
        error?.let { showMessage(it) }
    }

    private fun showMessage(@StringRes text: Int) {
        showMessage(getString(text))
    }

    private fun showMessage(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun applyAdGemPlayerId() {
        val playerIdKey = "PLAYER_ID"
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        var playerId = sharedPreferences.getString(playerIdKey, null)
        if (playerId == null) {
            playerId = "${UUID.randomUUID()}"
            sharedPreferences.edit().putString(playerIdKey, playerId).apply()
        }

        val metadata = PlayerMetadata.Builder.createWithPlayerId(playerId)
            .age(32)
            .createdAt("2018-11-10 18:39:45")
            .gender(PlayerMetadata.Gender.FEMALE)
            .iapTotalUsd(123f)
            .level(100)
            .placement(1000)
            .customField1("custom_field_1")
            .customField2("custom_field_2")
            .customField3("custom_field_3")
            .customField4("custom_field_4")
            .customField5("custom_field_5")
            .build()
        AdGem.get().setPlayerMetaData(metadata)
    }
}
