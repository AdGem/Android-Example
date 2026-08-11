AdGem Android SDK
========

Java native, mobile optimized AdGem Android SDK.

This is an extended version of the simplified integration guide [here][1].

Download
--------
[![Maven Central](https://img.shields.io/maven-central/v/com.adgem/adgem-android?style=flat-square)](https://search.maven.org/artifact/com.adgem/adgem-android)

Gradle:
```groovy
implementation 'com.adgem:adgem-android:5.0.0'
```

Maven:
```xml
<dependency>
  <groupId>com.adgem</groupId>
  <artifactId>adgem-android</artifactId>
  <version>5.0.0</version>
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

Requirements
--------

| | |
|---|---|
| Minimum Android | 6.0 (API 23) |
| `compileSdk` | 37 or later |
| Android Gradle plugin | 9.1.0 or later |

The `compileSdk` and AGP floors come from `androidx.core:core-ktx:1.19.0`, which the SDK depends
on — AGP 8.x builds fail to resolve it. AGP 9 supplies Kotlin itself, so a project that applied
`org.jetbrains.kotlin.android` must drop that plugin when it upgrades; see
[AGP built-in Kotlin](https://kotl.in/gradle/agp-built-in-kotlin).

Overview
--------
The SDK is initialized explicitly. Call `initialize` once before any other AdGem call —
typically in `Application.onCreate()`:

```kotlin
class ExampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AdGem.get().initialize(this, AdGemConfig.Builder("ADGEM_APP_ID").build())
    }
}
```

Register the class in your `AndroidManifest.xml`:
```xml
<application android:name=".ExampleApplication" ... >
```

> **Upgrading from 4.x:** `res/xml/adgem_config.xml` and the `com.adgem.Config` manifest
> meta-data are no longer read. Delete both and pass your App ID through `AdGemConfig.Builder`.
> The `offerwallEnabled` and `lockOrientation` options have no 5.x equivalent.

Call `close()` on logout, or before re-initializing with a different configuration.

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
For increased fraud protection, we require you set the `playerId` (a unique id for your user,
max 256 characters) parameter. Call `setPlayer` once the player identity is known — this may be
at startup or later, e.g. after login.

```java
  PlayerMetadata player = new PlayerMetadata.Builder("myPlayerId")
    .age(23)
    .iapTotalUsd(10)
    .level(4)
    .placement(2)
    .isPayer(true)
    .gender(PlayerMetadata.Gender.FEMALE)
    .createdAt(new Date())
    .customField1("custom_field_1")
    .customField2("custom_field_2")
    .customField3("custom_field_3")
    .customField4("custom_field_4")
    .customField5("custom_field_5")
    .build();

  adgem.setPlayer(player);
```

`createdAt` takes a `java.util.Date` and is serialized in UTC. Values outside the documented
bounds are logged and skipped rather than sent.

> **Upgrading from 4.x:** `Builder.createWithPlayerId()` and the no-arg `Builder()` are replaced
> by `Builder(String playerId)`; `setPlayerMetaData()` is now `setPlayer()`; `createdAt` takes a
> `Date` instead of a `String`.

### Offer Wall:
Once the Offer Wall is ready, AdGem will notify a subscriber via the ```OfferwallCallback```.
Every method has a default implementation, so override only the ones you need:
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
      public void onOfferwallLoadingFailed(AdGemError error) {
          // Notifies that the offer wall has failed to load.
          // Inspect error.getKind() to handle specific cases: NOT_INITIALIZED,
          // NOT_READY, OFFERWALL_UNAVAILABLE, INTERNAL.
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
Once registered, a callback will be used to deliver the offer wall updates. All callback methods
are invoked on the main thread, and `registerOfferwallCallback` / `unregisterOfferwallCallback`
must themselves be called from the main thread.

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