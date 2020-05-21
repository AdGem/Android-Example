AdGem Android SDK
========

Java native, mobile optimized AdGem Android SDK.

This is an extended version of the simplified integration guide [here][1].

PlayStore: https://play.google.com/store/apps/details?id=com.adgem.android.example

Download
--------

[![Download](https://api.bintray.com/packages/adgemsdk/android/adgem-android/images/download.svg) ](https://bintray.com/adgemsdk/android/adgem-android/_latestVersion)

Gradle:
```groovy
implementation 'com.adgem:adgem-android:2.4.0'
```

Maven:
```xml
<dependency>
  <groupId>com.adgem</groupId>
  <artifactId>adgem-android</artifactId>
  <version>2.4.0</version>
  <type>pom</type>
</dependency>
```

build.gradle
```groovy
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}
```


AdGem Android SDK requires a minimum of Android 4.1.

Overview
--------
AdGem Android SDK library is automatically downloaded by the build system. To configure SDK for your project:
1. Add ```adgem_config.xml``` to ```res/xml``` folder of your project structure:
```xml
<adgem-configuration 
            applicationId="ADGEM_APP_ID"
            interstitialAdsEnabled="true|false"
            rewardedAdsEnabled="true|false"
            offerWallEnabled="true|false" 
            lockOrientation="true|false" />
```
2. Add following tag to ```<application>``` to the ```AndroidManifest.xml```:
```xml
<meta-data android:name="com.adgem.Config"
           android:resource="@xml/adgem_config"/>
```

R8/ProGuard
--------
All necessary R8 or Proguard configurations are automatically supplied by the library. No additional configuration is needed.

API overview
--------
All communications with the SDK happen via the ```java AdGem``` class:
```java
AdGem adgem = AdGem.get();
```
There is no need to store instance of AdGem globally. The SDK will cache the instance on a first call and will always return the same one for all subsequent calls to ```AdGem.get();```

### AdGemCallback:
The AdGem SDK provides callbacks with notifications of internal state changes.
```java
AdGem adgem = AdGem.get();
adgem.registerCallback(callback);
```
Once registered, a callback will be used to deliver SDK state change updates.

Keep in mind that AdGem will hold a strong reference to the callback. It is the caller's responsibility to unregister it. For example, if a callback is being registered in activity's ```onCreate()```, then it must be unregistered in the corresponding ```onDestroy()``` call.

```java
public class GameActivity extends AppCompatActivity {
    private AdGem adGem;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ...
        adGem = AdGem.get();
        adGem.registerCallback(callback);
        ...
    }

    @Override
    protected void onDestroy() {
        ...
        adGem.unRegisterCallback(callback);
        ...
    }
}
```

### Displaying ads:
AdGem will download, prepare and cache interstitial and/or rewarded ads if they are configured via the configuration XML:
```xml
<adgem-configuration 
            applicationId="ADGEM_APP_ID"
            interstitialAdsEnabled="true|false"
            rewardedVideoAdsEnabled="true|false"
            offerWallEnabled="true|false"
            lockOrientation="true|false"/>
```

Once AdGem has an interstitial/rewarded video ready to play, it will notify a client via the ```AdGemCallback```.

Once the ad is in a ready state (as signaled by the callback), it can be shown either by calling ```adGem.showInterstitialAd()``` or ```adGem.showRewardedAd()``` respectively. If a user cancels ad play, cancellation event can be received via the ```AdGemCallback.onInterstitialAdClosed()``` or ```AdGemCallback.onRewardedAdCancelled()``` respectively. 
Ad readiness flags are also available via: ```adGem.isInterstitialAdReady()``` and ```adGem.isRewardedAdReady()``` fields.

Note that once an interstitial or rewarded ad starts showing, AdGem will immediately initiate download of the next ad. It is important to monitor changes in an ad state since it will transition through multiple states before becoming "ready". 

### Ads:
AdGem will lock ad orientation to the same screen orientation configured in a publisher's console. To enable orientation locking use following configuration:
```xml
<adgem-configuration 
  ...
  lockOrientation="true|false"
  ... />
```
This value is ```false``` by default.

### Offer Wall:
AdGem will download and prepare the Offer Wall as it is configured in AdGem configuration XML:
```xml
<adgem-configuration 
  ...
  offerWallEnabled="true|false"
  ... />
```

Once the Offer Wall is ready, AdGem will notify a subscriber via the ```OfferWallCallback```:
```java
    OfferWallCallback callback = new OfferWallCallback() {
            @Override
            public void onOfferWallStateChanged(int newState) {
		
            }
        
            @Override
            public void onOfferWallReward(int amount) {
                // Notifies that the user has completed an action and should be rewarded with a specified amount. 
            }
            
            @Override
            public void onOfferWallClosed() {
                // Notifies that the offer wall was closed. 
            }
    };
``` 
Offer wall callback may be registered through the instance of ```AdGem```:
```java
AdGem adgem = AdGem.get();
adgem.registerOfferWallCallback(callback);
```
Once registered, a callback will be used to deliver the offer wall updates.
Once Offer Wall is in the ready state, it can be displayed by calling  ```adGem.showOfferWall(activity)```.  Offer Wall readiness flag is available via the ```adGem.isOfferWallReady()``` field.

### Status codes:

Same status codes will be used to notify about the state ads or offer wall.

| Constant value  | Description |
| ------------- | ------------- |
| AdGem.STATE_ERROR | Identifies that internal error ocurred. AdGem will retry download automatically. Exact error is immediately available via ```adGem.getError()``` |
| AdGem.STATE_DISABLED | A component is disabled in configuration xml |
| AdGem.STATE_INITIALIZING | AdGem is initializing this component now. Usually happens on session start up |
| AdGem.STATE_NEEDS_INITIALIZATION | Component needs initialization |
| AdGem.STATE_NEEDS_CAMPAIGN_REFRESH | On initial launch or after invalidating internal caches |
| AdGem.STATE_REFRESHING_CAMPAIGN | Checking for active campaign  |
| AdGem.STATE_NEEDS_DOWNLOAD | Will start downloading new ad soon |
| AdGem.STATE_DOWNLOADING | Downloading and caching new ads |
| AdGem.STATE_READY | A component (ad or an offer wall) is ready to be displayed |

[1]: https://adgem.com/docs/android/integration-guide/
