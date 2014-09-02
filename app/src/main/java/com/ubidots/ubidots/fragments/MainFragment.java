package com.ubidots.ubidots.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ubidots.ubidots.R;
public class MainFragment extends Fragment {
    public interface MainFragmentButtonsInterface {
        public void onLoginButtonClick(Fragment fragment);
        public void onSignUpButtonClick(Fragment fragment);
    }

    private Button mLoginButton;
    private Button mSignUpButton;
    private MainFragmentButtonsInterface mInterface;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_main, container, false);

        mLoginButton = (Button) viewRoot.findViewById(R.id.login_button);
        mSignUpButton = (Button) viewRoot.findViewById(R.id.sign_up_button);

        mLoginButton.setOnClickListener(new LoginButtonClickListener());
        mSignUpButton.setOnClickListener(new SignUpButtonClickListener());

        return viewRoot;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mInterface = (MainFragmentButtonsInterface) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private class LoginButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mInterface.onLoginButtonClick(MainFragment.this);
        }
    }

    private class SignUpButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mInterface.onSignUpButtonClick(MainFragment.this);
        }
    }
}