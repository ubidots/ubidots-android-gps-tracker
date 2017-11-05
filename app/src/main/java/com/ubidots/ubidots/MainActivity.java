package com.ubidots.ubidots;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.ubidots.ubidots.fragments.BrowserFragment;
import com.ubidots.ubidots.fragments.MainFragment;

import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;

//public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentButtonsInterface {
public class MainActivity extends Activity implements MainFragment.MainFragmentButtonsInterface {
    // We want to know if the user has logged in before
    private SharedPreferences mSharedPreferences;
    private boolean mUserFirstTime;
    private boolean isUserLoggedIn;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers());
        setContentView(R.layout.activity_main);

        mFragmentManager = getFragmentManager();
        // Get the preference to check if the user has logged in previously
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserFirstTime = mSharedPreferences.getBoolean(Constants.FIRST_TIME, true);
        //isUserLoggedIn = mSharedPreferences.getBoolean(Constants.FIRST_TIME, false);

    /*    if (savedInstanceState == null) {
            if (mUserFirstTime) {
                MainFragment mainFragment = new MainFragment();
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, mainFragment)
                        .commit();
            } else {
                Intent ubidotsIntent = new Intent(this, UbidotsActivity.class);
                startActivity(ubidotsIntent);
                finish();
            }
        }*/
        /*if (savedInstanceState == null) {
            if(!isUserLoggedIn) {
                MainFragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, mainFragment);
                fragmentTransaction.commit();
                Intent ubidotsIntent = new Intent(this, UbidotsActivity.class);
                ubidotsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ubidotsIntent);
                finish();
            }

        }
*/
        if (savedInstanceState == null) {
            if (mUserFirstTime) {
                MainFragment mainFragment = new MainFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, mainFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Intent ubidotsIntent = new Intent(this, UbidotsActivity.class);
                startActivity(ubidotsIntent);
                finish();
            }
        }

    }

    // Method from MainFragment
    @Override
    public void onLoginButtonClick(Fragment fragment) {
        Bundle arguments = new Bundle();
        BrowserFragment logInFragment = new BrowserFragment();

        arguments.putString(Constants.URL, Constants.BROWSER_CONFIG.LOGIN_URL);
        logInFragment.setArguments(arguments);

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .add(R.id.fragment_container, logInFragment)
                .commit();
    }

    // Method from MainFragment
    @Override
    public void onSignUpButtonClick(Fragment fragment) {
        Bundle arguments = new Bundle();
        BrowserFragment signUpFragment = new BrowserFragment();

        arguments.putString(Constants.URL, Constants.BROWSER_CONFIG.SIGN_UP_URL);
        signUpFragment.setArguments(arguments);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, signUpFragment)
                .addToBackStack(null)
                .commit();
    }
}
