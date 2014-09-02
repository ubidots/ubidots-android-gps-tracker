package com.ubidots.ubidots.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Variable;
import com.ubidots.ubidots.Constants;
import com.ubidots.ubidots.R;
import com.ubidots.ubidots.VerificationActivity;

import java.util.HashMap;
import java.util.Map;

public class BrowserFragment extends Fragment {
    private WebView mWebView;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditorPreferences;

    public BrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browser, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.web_view);
        mSharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Bundle arguments = getArguments();

        if (arguments != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setUserAgentString(Constants.BROWSER_CONFIG.USER_AGENT);
            mWebView.loadUrl(arguments.getString(Constants.URL));
            mWebView.setWebViewClient(new WebViewClientController());
            mWebView.addJavascriptInterface(new WebViewJavascriptHandler(),
                    "JavascriptHandler");
        }

        return rootView;
    }

    private class WebViewClientController extends WebViewClient {
        private WebViewClientController() {
            // Required empty public constructor
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }
    }

    private final class WebViewJavascriptHandler {
        private ApiClient mApiClient;
        private DataSource mDataSource;
        private Variable mVariable;

        private WebViewJavascriptHandler() {
            // Required empty public constructor
        }

        @JavascriptInterface
        public void sendToAndroid(String token) {
            mEditorPreferences = mSharedPreferences.edit();
            mEditorPreferences.putString(Constants.TOKEN, token);

            // Create or get the variable
            mApiClient = new ApiClient().fromToken(token);
            mDataSource = createDataSource();
            mVariable = createVariable();

            mEditorPreferences.putString(Constants.VARIABLE_ID, mVariable.getId());
            mEditorPreferences.apply();

            Intent i = new Intent(getActivity().getApplicationContext(), VerificationActivity.class);
            startActivity(i);
            getActivity().finish();
        }

        private DataSource createDataSource() {
            String dataSourceName = "Android" + Build.MODEL;
            DataSource[] dataSources = mApiClient.getDataSources();

            for (DataSource dataSource : dataSources) {
                if (dataSource.getName().equals(dataSourceName)) {
                    return dataSource;
                }
            }

            Map<String, String> context = new HashMap<String, String>();
            context.put("_icon", "android");

            return mApiClient.createDataSource(dataSourceName, context, null);
        }

        private Variable createVariable() {
            Variable[] variables = mDataSource.getVariables();

            for (Variable variable : variables) {
                if (variable.getName().equals(Constants.DATASOURCE_VARIABLE)) {
                    return variable;
                }
            }

            return mDataSource.createVariable(Constants.DATASOURCE_VARIABLE, " ");
        }
    }
}