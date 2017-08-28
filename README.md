# Sample application for Touch SDK integration

* Shows list of tenants
* Allows start chat with selected tenant
* Allows clear all histories

To use Touch SDK in a project, add it as a build dependency and import it.
1. Go to Android Studio | New Project | Minimum SDK
2. Select API 16: Android 4.1 or higher and create your new project.
3. After you create a new project, unzip touch-android-sdk-VER.zip archive and put it into your main project
4. Open your_app | build.gradle
5. Add maven repo url "$rootDir/touch-sdk-android" to Module-level /app/build.gradle before dependencies:
```groovy
apply plugin: 'com.android.application'
repositories {
  maven { url "$rootDir/touch-sdk-android" }
}
android {...}
``` 
6. Add the compile dependency with the latest version of the Touch SDK in the build.gradle file, like that:
```groovy 
dependencies {
  compile('com.clickatell.chatsecure.androidsdk:touch-android-sdk:+')
}
```
7. React-native requires to add ndk { abiFilters "armeabi-v7a", "x86" } to app’s defaultConfig, like that:
```groovy
android {
  compileSdkVersion 25
  buildToolsVersion "25.0.2"
  defaultConfig {
      applicationId "com.chatsample.touchsample"
      minSdkVersion 16
      targetSdkVersion 28
      versionCode 1
      versionName "1.0"
      multiDexEnabled true
      testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
      ndk {
          abiFilters "armeabi-v7a", "x86"
      }
  }
```  
7. Due to that fact that Touch SDK exceeds methods limit it is required to add multidex support to your app, doc can be found here - https://developer.android.com/studio/build/multidex.html.
8. In your main Application class, initialize Touch SDK with provided token, in order to start work with it. (What is Application class and how to work with it - https://developer.android.com/reference/android/app/Application.html )
```java
public class App extends Application {
  @Override
  public void onCreate() {
      super.onCreate();
      MultiDex.install(this);
      TouchSdk.install(this, getString(R.string.clickatell_token));
}

## Public API

1. To fetch all tenants available for client use TouchSdk.service().apiService().tenants():
```java
ApiCall<List<Tenant>> apiCall = TouchSdk.service().apiService().tenants();
apiCall.enqueue(new Callback<List<Tenant>>() {
  @Override
  public void onSuccess(@NonNull List<Tenant> tenants) {
      Log.d(TAG, "tenants " + Arrays.asList(tenants.toArray()));
      mAdapter.swapData(tenants);
  }
  @Override
  public void onError(Throwable throwable) {
      Log.e(TAG, "tenants call error", throwable);
  }
});
//To cancel request(e.g, when activity/fragment is no longer available)
apiCall.cancel();
```
2. To start chat activity:
```java
ChatActivity.startActivity(this/*activity*/, tenant);
```
3. Touch SDK includes possibility to view/clear chats started by the client, in order to do
that, use PersistenceService from TouchSdk.service().persistenceService()
```java
PersistenceService persistenceService = TouchSdk.service().persistenceService();
List<ChatModel> availableChats = persistenceService.availableChats();
boolean isSuccessful = persistenceService.clearAllChats();
```

## Firebase Cloud Messaging

If your application have to use FCM, please look into Google documentation (https://firebase.google.com/docs/cloud-messaging/android/client)
In order to Touch SDK works properly, you should change your implementation of FirebaseMessagingService
Please add such code to you implementation of onMessageReceived
```java
@Override
public void onMessageReceived(RemoteMessage remoteMessage) {
  Map<String, String> notificationData = remoteMessage.getData();
  if (PushNotificationHelper.isClickatellPush(notificationData)){
      PushNotificationHelper.handlePush(getApplicationContext(), notificationData);
  } else {
      <Your code here>
  }
}
```
## Conflict resolution

Sometimes you could see errors like this
Error:Conflict with dependency 'com.android.support:support-annotations' in project ':app'. Resolved versions for app (26.0.0-alpha1) and test app (23.1.1) differ. See http://g.co/androidstudio/app-test-app-conflict for details.
In such cases you could read this https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html
and use one of proposed solutions, for example add such block to you app gradle
```groovy
configurations.all {
  resolutionStrategy {
      force 'com.android.support:support-annotations:23.1.1'
  }
}
```
Or exclude conflicted version from one of library:
```groovy
androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
  exclude group: 'com.android.support', module: 'support-annotations'
})}
```

Now installation process is finished.
