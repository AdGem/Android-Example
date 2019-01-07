AdGem Android SDK
========

Java native mobile optimized AdGem Android SDK.

This is an extenden version of simplified integration guide [here][1].


Download
--------

[![Download](https://api.bintray.com/packages/adgemsdk/android/adgem-android/images/download.svg) ](https://bintray.com/adgemsdk/android/adgem-android/_latestVersion)

Gradle:
```groovy
implementation 'com.adgem:adgem-android:1.1.0'
```

Maven:
```xml
<dependency>
  <groupId>com.adgem</groupId>
  <artifactId>adgem-android</artifactId>
  <version>1.1.0</version>
  <type>pom</type>
</dependency>
```

AdGem Android SDK requires at minimum Android 4.4.

Overview
--------
AdGem android SDK is automatically configured by a buld system. To configure SDK specifically for your project:
1. Add ```adgem_config.xml``` to ```res/xml``` folder of your project structure:
```xml
<adgem-configuration applicationId="ADGEM_APP_ID"
		     standardVideoAdsEnabled="true|false"
		     rewardedVideoAdsEnabled="true|false"
		     offerWallEnabled="true|false" />
```
2. Add following tag to ```<application>``` to the ```AndroidManifest.xml```:
```xml
<meta-data android:name="com.adgem.Config"
           android:resource="@xml/adgem_config"/>
```

ProGuard
--------
All necessary proguard configurations are automatically supplied by the library. There is no additional configurations needed.

API overview
--------
All communication with SDK can happen via the ```java AdGem``` class:
```java
AdGem adgem = AdGem.get();
```
There is no need to store instance of AdGem globally. SDK will cache it instance on a first call and will always return the same one for all subsequent calls to ```AdGem.get();```

### AdGemCallback:
AdGem SDK offers callbacks that notify when its internal state changes.
```java
AdGem adgem = AdGem.get();
adgem.registerCallback(callback);
```
Once registered, a callback will be used to deliver SDK state change update.

Keep in mind that AdGem will hold a strong reference to a callback. It is caller's responsibility to unregister it. For example, if a callback is being registered in activity's ```onCreate()``` then is must be unregistered in corresponding ```onDestroy()``` call.

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

### Playing videos:
AdGem will download, prepare and cache standard and/or rewarded videos if they are configured via configuration XML:
```xml
<adgem-configuration applicationId="ADGEM_APP_ID"
		     standardVideoAdsEnabled="true|false"
		     rewardedVideoAdsEnabled="true|false"
		     offerWallEnabled="true|false" />
```

Once AdGem has a standard/rewarded video ready to play, it will notify a client via the ```AdGemCallback```:
```java
    AdGemCallback callback = new AdGemCallback() {
        @Override
        public void onStandardVideoAdStateChanged(int newState) {
		// newState will notify a state of a standard video
		// Full list of possible state codes is defined in AdGem class.
        }
        
        @Override
        public void onRewardedVideoAdStateChanged(int newState) {
		// newState will notify a state of a rewarded video
		// Full list of possible state codes is defined in AdGem class.
        }

        @Override
        public void onStandardVideoComplete() {
		// Notifies that the user has finished watching standard video ad.
        }

        @Override
        public void onRewardedVideoComplete() {
		// Reward user for watching a rewarded video ad.
        }
    };
``` 

Once video is in ready state (as signaled by a callback), it can be played either via ```adGem.showStandardVideoAd()``` or ```adGem.showRewardedVideoAd()``` respectively. Video readiness flags are also available via: ```adGem.isStandardVideoAdReady()``` and ```adGem.isRewardedVideoAdReady()``` fields.

Note that once standard or rewarded video started playing, AdGem will immediately initiate downloading a next one. It is important to monitor changes in a video state since it will transition through multiple states before becoming "ready". 

### Offer Wall:
AdGem will download and prepare offer wall if it is configured in adgem configuration XML:
```xml
<adgem-configuration ...
		     offerWallEnabled="true|false" 
		     ... />
```

Once AdGem has Offer Wall ready, it will notify subcriber via the ```AdGemCallback```:
```java
    AdGemCallback callback = new AdGemCallback() {
            @Override
            public void onOfferWallStateChanged(int newState) {
		
            }
        
            @Override
            public void onRewardUser(int amount) {
		// Notifies that the user has completed an action and should be rewarded with a specified amount. 
            }
    };
``` 
Once Offer Wall is in ready state, it can be displayed by calling  ```adGem.showOfferWall()```.  Offer Wall readiness flag is available via the ```adGem.isOfferWallReady()``` field.

### Status codes:

Same status codes will be used to notify about state of a standard/rewarded video or offer wall.

| Constant value  | Description |
| ------------- | ------------- |
| AdGem.STATE_ERROR | Identifies that internal error ocurred. AdGem will retry download automatically. Exact error is immediately available via ```adGem.getError()``` |
| AdGem.STATE_DISABLED | A component is disabled in configuration xml |
| AdGem.STATE_INITIALIZING | AdGem is initializing this component now. Usually hapenns on session start up |
| AdGem.STATE_NEEDS_INITIALIZATION | Component needs initialization |
| AdGem.STATE_NEEDS_CAMPAIGN_REFRESH | On initial launch or after invalidating internal caches |
| AdGem.STATE_REFRESHING_CAMPAIGN | Checking for active campaign  |
| AdGem.STATE_NEEDS_DOWNLOAD | Will start downloading new media soon |
| AdGem.STATE_DOWNLOADING | Downloading and caching new ads |
| AdGem.STATE_READY | A component (video or offer wall) is ready to be displayed |

[1]: https://help.adgem.com/integration-guides/sdk-integration/android-sdk-integration-guide
