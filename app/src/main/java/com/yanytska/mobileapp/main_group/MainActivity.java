package com.yanytska.mobileapp.main_group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.auth.Auth;
import com.yanytska.mobileapp.auth.AuthenticationActivity;
import com.yanytska.mobileapp.main_group.about.AboutFragment;
import com.yanytska.mobileapp.main_group.fares.FaresFragment;
import com.yanytska.mobileapp.main_group.profile.ProfileFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    private Auth auth = Auth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra("fareId")) {
            replaceFragment(new FaresFragment());
        } else {
            initView();
            initListeners();

            setSupportActionBar(mToolbar);

            mActionBarDrawerToggle =
                    new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
            mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
            mActionBarDrawerToggle.syncState();

            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

            replaceFragment(new ProfileFragment());
            getSupportActionBar().setTitle(R.string.profile);

            logFirebaseToken();
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        getSupportFragmentManager().popBackStack();

        switch (item.getItemId()) {
            case R.id.nav_profile:
                replaceFragment(new ProfileFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.profile);
                break;

            case R.id.nav_fares:
                replaceFragment(new FaresFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.fares);
                break;

            case R.id.nav_about:
                replaceFragment(new AboutFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.about);
                break;

            case R.id.log_out:
                signOut();
                break;

            default:
                break;
        }

        mDrawerLayout.closeDrawers();
        return false;
    }

    private void signOut() {
        auth.signOut();
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToolbar = findViewById(R.id.nav_action_bar);
        mNavigationView = findViewById(R.id.navigationView);
    }

    private void initListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void logFirebaseToken() {
        final String TAG = "FCM Token";

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    String token = Objects.requireNonNull(task.getResult()).getToken();

                    Log.d(TAG, token);
                });
    }
}
