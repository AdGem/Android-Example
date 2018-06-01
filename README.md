AdGem Android SDK
========

Java native mobile optimized AdGem Android SDK.

This is an extenden version of simplified integration guide [here][1].


Download
--------

Download [the latest AAR][2] or grab via Maven:
```xml
<dependency>
  <groupId>com.adgem</groupId>
  <artifactId>adgem-android</artifactId>
  <version>0.6.8</version>
  <type>pom</type>
</dependency>
```
or Gradle:
```groovy
implementation 'com.adgem:adgem-android:0.6.8'
```

AdGem Android SDK requires at minimum Android 4.4.

Overview
--------
AdGem android SDK is automatically configured by a buld system. To configure SDK specifically for your project:
1. Add ```adgem_config.xml``` to ```res/values``` folder of your project structure:
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

Proguard config is automatically supplied with AAR. There is no additional configurations needed.

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
AdGem will download, prepare and cache standard videos if they are configured via configuration XML:
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
        }
        
        @Override
        public void onRewardedVideoAdStateChanged(int newState) {
            // newState will notify a state of a rewarded video
        }

        @Override
        public void onStandardVideoComplete() {
			// Notifies that user has finished watching standard video ad.
        }

        @Override
        public void onRewardedVideoComplete() {
        	// Notifies that user has finished watching rewarded video ad.
        }
    };
``` 

Once video is in ready state (as signaled by a callback), it can be played either via ```adGem.showStandardVideoAd()``` or ```adGem.showRewardedVideoAd()``` respectively. Video readiness flags are also available via: ```adGem.isStandardVideoAdReady()``` and ```adGem.isRewardedVideoAdReady()``` fields.

[1]: https://help.adgem.com/sdk-integration/android-integration-guide
[2]: https://bintray.com/adgemsdk/android/download_file?file_path=com%2Fadgem%2Fadgem-android%2F0.6.8%2Fadgem-android-0.6.8.aar


