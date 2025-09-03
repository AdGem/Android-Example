AdGem Android SDK
========

Java native, mobile optimized AdGem Android SDK.

This is an extended version of the simplified integration guide [here][1].

Download
--------
[![Maven Central](https://img.shields.io/maven-central/v/com.adgem/adgem-android?style=flat-square)](https://search.maven.org/artifact/com.adgem/adgem-android)

Gradle:
```groovy
implementation 'com.adgem:adgem-android:4.0.3'
```

Maven:
```xml
<dependency>
  <groupId>com.adgem</groupId>
  <artifactId>adgem-android</artifactId>
  <version>4.0.1</version>
  <type>pom</type>
</dependency>
```

build.gradle
```groovy
compileOptions {
  sourceCompatibility JavaVersion.VERSION_17
  targetCompatibility JavaVersion.VERSION_17
}
```

AdGem Android SDK requires a minimum of Android 5.0.

Overview
--------
AdGem Android SDK library is automatically downloaded by the build system. To configure SDK for your project:
1. Add ```adgem_config.xml``` to ```res/xml``` folder of your project structure:
```xml
<adgem-configuration 
            applicationId="ADGEM_APP_ID"
            offerwallEnabled="true|false" 
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

### Player Metadata:
For increased fraud protection, we require you set the `playerId`  (a unique id for your user) parameter.
```java
  PlayerMetadata player = new PlayerMetadata.Builder.createWithPlayerId("myPlayerId")
    .age(23)
    .iapTotalUsd(10)
    .level(4)
    .placement(2)
    .isPayer(true)
    .gender(PlayerMetadata.Gender.FEMALE)
    .createdAt("2018-11-16 06:23:19.07")
    .customField1("custom_field_1")
    .customField2("custom_field_2")
    .customField3("custom_field_3")
    .customField4("custom_field_4")
    .customField5("custom_field_5")
    .build();

  adgem.setPlayerMetaData(player);
```

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
  OfferwallCallback callback = new OfferwallCallback() {
      @Override
      public void onOfferwallLoadingStarted() {
          // Notifies that the offer wall loading has started.
      }

      @Override
      public void onOfferwallLoadingFinished() {
          // Notifies that the offer wall has been loaded.
      }

      @Override
      public void onOfferwallLoadingFailed(String error) {
          // Notifies that the offer wall has failed to load.
      }

      @Override
      public void onOfferwallRewardReceived(int amount) {
          // Notifies that the user has completed an action and should be rewarded with a specified virtual currency amount.
      }

      @Override
      public void onOfferwallClosed() {
          // Notifies that the offer wall was closed.
      }
  };
``` 
Offer wall callback may be registered through the instance of ```AdGem```:
```java
AdGem adgem = AdGem.get();
adgem.registerOfferwallCallback(callback);
```
Once registered, a callback will be used to deliver the offer wall updates.

Keep in mind that AdGem will hold a strong reference to a callback. It is the caller’s responsibility to unregister it. For example, if a callback is being registered in activity’s `onCreate()` then it must be unregistered in corresponding `onDestroy()` call.

```java
  public class GameActivity extends AppCompatActivity {
      private AdGem adGem;

      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
          ...
          adGem = AdGem.get();
          adGem.registerOfferwallCallback(callback);
          ...
      }

      @Override
      protected void onDestroy() {
          ...
          adGem.unregisterOfferwallCallback(callback);
          ...
      }
  }
```

Offer Wall can be displayed by calling  ```adGem.showOfferwall(activity)```.