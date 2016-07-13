package com.kiwi.auready_ver2.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kiwi.auready_ver2.R;


public class SignupFragment extends Fragment implements
        SignupContract.View {

    private EditText mEmail;
    private EditText mPassword;
    private EditText mName;
    private Button mBtSignupComplete;
    private SignupContract.Presenter mSignupPresenter;

    // interface
    private SignupFragmentListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSignupPresenter = new SignupPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        mEmail = (EditText) root.findViewById(R.id.ed_signup_email);
        mPassword = (EditText) root.findViewById(R.id.ed_password);
        mName = (EditText) root.findViewById(R.id.ed_name);
        mBtSignupComplete = (Button) root.findViewById(R.id.bt_signup_complete);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBtSignupComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignupPresenter.attemptSignup(
                        mEmail.getText().toString(),
                        mPassword.getText().toString()
                );
            }
        });
    }

    @Override
    public void showEmailError(int resourceId) {
        mEmail.requestFocus();
        mEmail.setError(getString(resourceId));
    }

    @Override
    public void setSignupSuccessUI(String email) {

        Snackbar.make(getView(), email + getString(R.string.signup_success_msg), Snackbar.LENGTH_SHORT).show();
        // send email to LoginFragment
        if(mListener != null) {
            mListener.onSignupSuccess(email);
        }

        // pop LoginFragment in backStack
        getFragmentManager().popBackStack();
    }

    @Override
    public void setSignupFailMessage(int resourceId) {
        Snackbar.make(getView(), getString(resourceId), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordError(int resourceId) {
        mPassword.requestFocus();
        mPassword.setError(getString(resourceId));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignupFragmentListener) {
            mListener = (SignupFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SignupFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }


    // Interface with LoginActivity
    public interface SignupFragmentListener {
        void onSignupSuccess(String email);
    }
}
