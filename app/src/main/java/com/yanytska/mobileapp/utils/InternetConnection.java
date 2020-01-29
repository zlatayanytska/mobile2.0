package com.yanytska.mobileapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetConnection {

    private InternetConnection() {
        //default empty constructor for Singleton class with no attributes
    }

    public static boolean checkConnection(Context context){
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo()!=null;
    }
}
