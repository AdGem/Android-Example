package com.adgem.example

import android.app.Application
import com.adgem.android.AdGem
import com.adgem.android.AdGemConfig

/**
 * SDK 5.0.0 requires an explicit [AdGem.initialize] before any other AdGem call — the
 * auto-init content provider and the `com.adgem.Config` manifest meta-data that earlier
 * versions relied on are both gone. Doing it here means the SDK is ready before any
 * activity starts.
 *
 * The player is identified separately, in [AdGemActivity], because a real host app only
 * knows its player id once the user is known (e.g. after login).
 */
class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AdGem.get().initialize(this, AdGemConfig.Builder(APP_ID).build())
    }

    companion object {
        /** Each application must register its own applicationId with adgem.com. */
        const val APP_ID = "282"
    }
}
