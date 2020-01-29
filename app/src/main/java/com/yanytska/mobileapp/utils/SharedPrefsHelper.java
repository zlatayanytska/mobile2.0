package com.yanytska.mobileapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yanytska.mobileapp.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SharedPrefsHelper {

    private static final String SHARED_PREFS = "SHARED_PREFS";
    private static final String SAVED_PHONE = "SAVED_PHONE";

    private Context mContext;

    public SharedPrefsHelper() {
        mContext = getApplicationContext();
    }

    public void savePhone(String phone) {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS, 0);
        SharedPreferences.Editor prefsEdit = prefs.edit();

        prefsEdit.putString(SAVED_PHONE, phone);
        prefsEdit.apply();
    }

    public String loadPhone() {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS, 0);

        String defaultValue = getApplicationContext().getString(R.string.unavailable);

        return prefs.getString(SAVED_PHONE, defaultValue);
    }

    public void clearPrefs() {
        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS, 0);
        prefs.edit()
                .clear()
                .apply();
    }
}