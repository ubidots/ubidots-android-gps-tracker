package com.ubidots.ubidots.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.ubidots.ApiClient;
import com.ubidots.Variable;
import com.ubidots.ubidots.Constants;
import com.ubidots.ubidots.NetworkUtil;
import com.ubidots.ubidots.receivers.PushAlarmReceiver;

import java.util.HashMap;
import java.util.Map;

public class PushLocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    // For repeating this service
    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;

    // For location updates
    private LocationRequest mLocationRequest;
    private LocationClient mLocationClient;

    // Read preference data
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get the update frequency
        int updateFreq = mPrefs.getInt(Constants.PUSH_TIME, 1);
        // Get the Network status
        int connectionStatus = NetworkUtil.getConnectionStatus(this);
        // Check if the service is still activated by the user
        boolean isRunning = mPrefs.getBoolean(Constants.SERVICE_RUNNING, false);

        // If the service is activated and we have network connection
        // Create a pending intent
        if (isRunning && (connectionStatus == NetworkUtil.TYPE_MOBILE ||
                connectionStatus == NetworkUtil.TYPE_WIFI)) {
            int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
            long timeToRefresh = SystemClock.elapsedRealtime() + updateFreq * 1000;
            mAlarmManager.setInexactRepeating(alarmType, timeToRefresh, updateFreq * 1000,
                    mAlarmIntent);
        } else {
            deleteNotification();
            mAlarmManager.cancel(mAlarmIntent);
        }

        // If Google Play is available and we have network connection
        if ((isGooglePlayAvailable() && isRunning) &&
                (connectionStatus == NetworkUtil.TYPE_MOBILE ||
                        connectionStatus == NetworkUtil.TYPE_WIFI)) {
            mLocationClient.connect();
        } else if (mLocationClient != null && mLocationClient.isConnected()) {
                mLocationClient.removeLocationUpdates(this);
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        String ALARM_ACTION = PushAlarmReceiver.ACTION_PUSH_LOCATION_ALARM;
        Intent intentToFire = new Intent(ALARM_ACTION);
        mAlarmIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mPrefs.edit();

        if (isGooglePlayAvailable())
            initializeLocationUpdates();
    }

    @Override
    public void onDestroy() {
        stopSelf();
        if (mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
            mLocationClient.disconnect();
        }

        super.onDestroy();
    }

    public boolean isGooglePlayAvailable() {
        int availability = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        return availability == ConnectionResult.SUCCESS;
    }

    public void initializeLocationUpdates() {
        int updateFreq = mPrefs.getInt(Constants.PUSH_TIME, 1);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(updateFreq * 1000);
        mLocationRequest.setFastestInterval((updateFreq - 1) * 1000);

        mLocationClient = new LocationClient(this, this, this);
    }

    public void deleteNotification() {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager man = (NotificationManager)
                getApplicationContext().getSystemService(ns);
        man.cancel(Constants.NOTIFICATION_ID);
    }

    @Override
    public void onLocationChanged(Location location) {
        String token = mPrefs.getString(Constants.TOKEN, null);
        new UbidotsAPI(location.getLongitude(), location.getLatitude()).execute(token);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public class UbidotsAPI extends AsyncTask<String, Void, Void> {
        private final String variableID = mPrefs.getString(Constants.VARIABLE_ID, null);
        private double longitude;
        private double latitude;

        public UbidotsAPI(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                Map<String, Object> context = new HashMap<String, Object>();
                ApiClient apiClient = new ApiClient().fromToken(params[0]);

                if (variableID != null) {
                    Variable variable = apiClient.getVariable(variableID);
                    context.put(Constants.VARIABLE_CONTEXT.LATITUDE, latitude);
                    context.put(Constants.VARIABLE_CONTEXT.LONGITUDE, longitude);

                    if (!context.get(Constants.VARIABLE_CONTEXT.LATITUDE).equals(0.0) &&
                            !context.get(Constants.VARIABLE_CONTEXT.LONGITUDE).equals(0.0)) {
                        variable.saveValue(1.0, context);
                    }
                }
            } catch (Exception e) {
                Handler h = new Handler(getMainLooper());

                h.post(new Runnable() {
                    @Override
                    public void run() {
                        if (getApplicationContext() != null) {
                            Toast.makeText(getApplicationContext(),
                                    "Invalid Token",
                                    Toast.LENGTH_SHORT).show();
                        }
                        cancel(true);
                    }
                });
                mEditor.putBoolean(Constants.SERVICE_RUNNING, false);
                mEditor.apply();
                stopSelf();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopSelf();
        }
    }
}