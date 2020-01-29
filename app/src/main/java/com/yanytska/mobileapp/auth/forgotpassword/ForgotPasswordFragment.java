package com.yanytska.mobileapp.auth.forgotpassword;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yanytska.mobileapp.R;

public class ForgotPasswordFragment extends Fragment
        implements ForgotPasswordContract.IForgotPasswordView {

    private View view;
    private ForgotPasswordPresenter forgotPasswordPresenter;

    private EditText emailEt;
    private Button sendRecoverCodeBtn;
    private TextView backToSignInTv;

    private void initView() {
        emailEt = view.findViewById(R.id.email_et);
        sendRecoverCodeBtn = view.findViewById(R.id.send_recover_code_btn);
        backToSignInTv = view.findViewById(R.id.back_signin_txt);

        emailEt.setError(null);
    }

    private void initPresenter() {
        forgotPasswordPresenter = new ForgotPasswordPresenter(this, getActivity());
    }

    private void initListeners() {
        sendRecoverCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEt.setError(null);
                String email = emailEt.getText().toString();
                forgotPasswordPresenter.sendRecoveryCode(email);
            }
        });

        backToSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordPresenter.showSignIn();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        initView();
        initPresenter();
        initListeners();

        return view;
    }

    @Override
    public void recoveryCodeIsSent() {
        Toast.makeText(getActivity(), R.string.recovery_code_is_sent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailError() {
        emailEt.setError(getString(R.string.invalid_email));
    }
}
