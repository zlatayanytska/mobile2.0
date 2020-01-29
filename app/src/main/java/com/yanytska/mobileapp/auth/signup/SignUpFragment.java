package com.yanytska.mobileapp.auth.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yanytska.mobileapp.R;

public class SignUpFragment extends Fragment implements SignUpContract.ISignUpView {

    private View view;
    private SignUpPresenter signUpPresenter;

    private EditText usernameEt;
    private EditText emailEt;
    private EditText phoneEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;

    private Button signUpBtn;

    private TextView signInTv;

    private void initView() {
        usernameEt = view.findViewById(R.id.username_et);
        emailEt = view.findViewById(R.id.email_et);
        phoneEt = view.findViewById(R.id.phone_et);
        passwordEt = view.findViewById(R.id.password_et);
        confirmPasswordEt = view.findViewById(R.id.confirm_password_et);

        signUpBtn = view.findViewById(R.id.signup_btn);

        signInTv = view.findViewById(R.id.signin_txt);

        usernameEt.setError(null);
        emailEt.setError(null);
        phoneEt.setError(null);
        passwordEt.setError(null);
        confirmPasswordEt.setError(null);
    }

    private void initPresenter() {
        signUpPresenter = new SignUpPresenter(this, getActivity());
    }

    private void initListeners() {
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEt.getText().toString();
                String email = emailEt.getText().toString();
                String phone = phoneEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();
                signUpPresenter.signUp(username, email, phone, password, confirmPassword);
            }
        });

        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpPresenter.showSignIn();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        initView();
        initPresenter();
        initListeners();
        return view;
    }

    @Override
    public void showUsernameError() {
        usernameEt.setError(getString(R.string.invalid_username));
    }

    @Override
    public void showEmailError() {
        emailEt.setError(getString(R.string.invalid_email));
    }

    @Override
    public void showPhoneError() {
        phoneEt.setError(getString(R.string.invalid_phone));
    }

    @Override
    public void showPasswordError() {
        passwordEt.setError(getString(R.string.invalid_password));
    }

    @Override
    public void showConfirmPasswordError() {
        confirmPasswordEt.setError(getString(R.string.different_password_fields));
    }
}


