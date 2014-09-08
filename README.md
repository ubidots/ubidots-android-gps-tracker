# Ubidots GPS Tracker for Android

## Description

Welcome to the Ubidots GPS Tracker for Android devices. With this application you can push your actual location to Ubidots in an easy way; just log-in to your Ubidots account in the application and start pushing your data at any time interval you choose.

Our application asks for these permissions to install:

* **Your location:** We ask permission to access your location, so we can push that data into Ubidots and show it to you in a map.
* **Modify or delete contents of your USB storage:** This doesn't mean we are going to access your storage to read all your files! This is needed because we save some values for the configuration of the application.
* **Read Google service configuration:** This is needed by the application to show the map.
* **Full network access & view network connection:** We need to know if you are connected to the Internet to push the data to Ubidots.

## Import the project

*This project was made using Android Studio. It hasn't been tested in Eclipse + ADT, so we can't guarantee that it'll work there.*

Just clone this project in your computer and import it using Android Studio.

If you like to use Google Maps with your own keystore, you must sign it using your own Google provided API Key. To obtain a Google Maps for Android API Key, you must follow [this instructions](https://developers.google.com/maps/documentation/android/start#install_and_configure_the_google_play_services_sdk)

When you have your Google API Key, replace the *here_goes_api_key* text in the **AndroidManifest.xml** file with your API Key.

```xml
    <meta-data
        android:name="com.google.android.maps.v2.API_KEY"
        android:value="here_goes_api_key" />
```
