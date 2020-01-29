package com.yanytska.mobileapp.auth.signup;

public interface SignUpContract {

    interface ISignUpView {
        void showUsernameError();

        void showEmailError();

        void showPhoneError();

        void showPasswordError();

        void showConfirmPasswordError();
    }

    interface ISignUpPresenter {
        void signUp(final String username, final String email, final String phone,
                    final String password, final String confirmPassword);
    }
}
