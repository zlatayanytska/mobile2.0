package com.yanytska.mobileapp.auth;

public interface IAuthenticationView {

    void showSignIn();

    void showSignUp();

    void showForgotPassword();

    void signIn(String email, String password);

    void signUp(String email, String password, String username, String phone);

    void googleSignIn();

    void facebookSignIn();

    void sendRecoveryCode(String email);

    void startMainActivity();
}