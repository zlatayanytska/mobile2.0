package com.yanytska.mobileapp.main_group.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.utils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private View view;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;

    private Button editProfileButton;

    private ImageView profilePicture;

    private SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper();

    private StorageReference folderReference;
    private StorageReference imageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initElements();
        initValues();
        initListeners();

        return view;
    }

    private void initElements() {
        nameTextView = view.findViewById(R.id.name_tv);
        phoneTextView = view.findViewById(R.id.phone_tv);
        emailTextView = view.findViewById(R.id.email_tv);

        editProfileButton = view.findViewById(R.id.edit_profile_btn);

        profilePicture = view.findViewById(R.id.profile_picture_image_view);

        folderReference = FirebaseStorage.getInstance().getReference().child("folder");
    }

    private void initListeners() {
        final Context mContext = view.getContext();

        editProfileButton.setOnClickListener(view -> {
            FragmentManager fragmentManager =
                    ((AppCompatActivity) mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, new ProfileEditFragment())
                    .addToBackStack(null)
                    .commit();
            fragmentManager.executePendingTransactions();
        });
    }

    private void initValues() {
        FirebaseUser user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser());

        String displayName = user.getDisplayName();
        String email = user.getEmail();

        String welcome = getString(R.string.welcome) + displayName;
        String phone = getString(R.string.user_phone_label) + sharedPrefsHelper.loadPhone();
        String yourEmail = getString(R.string.user_email_label) + email;

        nameTextView.setText(welcome);
        phoneTextView.setText(phone);
        emailTextView.setText(yourEmail);

        imageReference = folderReference.child(user.getUid() + ".jpg");

        placeImage();
    }

    private void placeImage() {
        imageReference
                .getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Picasso.get().load(uri.toString()).into(profilePicture));
    }
}
