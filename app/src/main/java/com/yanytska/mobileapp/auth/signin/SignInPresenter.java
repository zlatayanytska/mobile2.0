package com.yanytska.mobileapp.auth.signin;

import android.app.Activity;

import com.yanytska.mobileapp.auth.IAuthenticationView;

import static com.yanytska.mobileapp.utils.Utils.isValidEmail;
import static com.yanytska.mobileapp.utils.Utils.isValidPassword;

public class SignInPresenter implements SignInContract.ISignInPresenter {

    private SignInContract.ISignInView view;
    private IAuthenticationView authenticationView;

    public SignInPresenter(SignInContract.ISignInView view, Activity activity) {
        this.view = view;
        authenticationView = (IAuthenticationView) activity;
    }

    private boolean validateInput(final String email, final String password) {
        boolean isEmailOk = isValidEmail(email);
        boolean isPasswordOk = isValidPassword(password);

        if (!isEmailOk) {
            view.showEmailError();
        }

        if (!isPasswordOk) {
            view.showPasswordError();
        }

        return (isEmailOk && isPasswordOk);
    }

    public void signIn(final String email, final String password) {
        if (validateInput(email, password)) {
            authenticationView.signIn(email, password);
        }
    }

    public void googleSignIn() {
        authenticationView.googleSignIn();
    }

    public void facebookSignIn() {
        authenticationView.facebookSignIn();
    }

    public void showForgotPassword() {
        authenticationView.showForgotPassword();
    }

    public void showSignUp() {
        authenticationView.showSignUp();
    }
}