package com.kiwi.auready_ver2.login;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kiwi.auready_ver2.Injection;
import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.util.ActivityUtils;

public class LoginActivity extends AppCompatActivity implements
        SignupFragment.SignupFragmentListener {

    public static final int REQ_LOGINOUT = 1;
    public static final String REGISTERED_EMAIL = "registeredEmail";

    // interface
    private LoginActivityListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.content_frame, LoginFragment.TAG_LOGINFRAGMENT);
        }
        // Create the presenter
        LoginPresenter presenter = new LoginPresenter(
                Injection.provideUseCaseHandler(),
                loginFragment,
                Injection.provideSaveFriends(getApplicationContext())
        );

        initView();
    }

    private void initView() {
        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.login);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof LoginFragment) {
            mListener = (LoginActivityListener) fragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    /*
    * SignupFragment listener
    * */
    @Override
    public void onSignupSuccess(String email, String name) {

        // pop LoginFragment in backStack
        getSupportFragmentManager().popBackStack();

        // todo. Send email data to LoginFragment
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LoginFragment.TAG_LOGINFRAGMENT);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.content_frame, LoginFragment.TAG_LOGINFRAGMENT);
        }

        mListener = loginFragment;
        mListener.onSendData(email, name);
    }

    // Interface with LoginFragment
    public interface LoginActivityListener {
        void onSendData(String registeredEmail, String registeredName);
    }
}