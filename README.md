# Import the project

Import and configure the project.

You must have Android Studio installed in your system, this is not compatible with Eclipse + ADT because the project uses Gradle to manage dependencies.

If you like to use the Google Maps with your own keystore, you must sign it using your own Google provided API Key. To obtain a Google Maps for Android API Key, you must follow [this instructions](https://developers.google.com/maps/documentation/android/start#install_and_configure_the_google_play_services_sdk)

When you have your Google API Key, then replace the *here_goes_api_key* in the **AndroidManifest.xml** file with your API Key.

```xml
    <meta-data
        android:name="com.google.android.maps.v2.API_KEY"
        android:value="here_goes_api_key" />```

We use Gradle to import the Gson library and the Ubidots API.