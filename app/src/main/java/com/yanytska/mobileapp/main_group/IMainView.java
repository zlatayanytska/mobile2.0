package com.yanytska.mobileapp.main_group;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.yanytska.mobileapp.R;

public interface IMainView {
    void showProgress();

    void hideProgress();

    void hideRefreshing();

    default void showNotFound(Context context) {
        Toast.makeText(context, R.string.not_found, Toast.LENGTH_SHORT).show();
    }

    default void showNotInternetConnection(Context context) {
        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    void setAdapter(RecyclerView.Adapter adapter);

    void setEnabledSearch(boolean enabled);
}
