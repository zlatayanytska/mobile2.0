package com.yanytska.mobileapp.auth.signin;

public interface SignInContract {

    interface ISignInView {
        void showEmailError();

        void showPasswordError();
    }

    interface ISignInPresenter {
        void signIn(final String email, final String password);

        void googleSignIn();

        void facebookSignIn();
    }
}
