package com.yanytska.mobileapp.main_group.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.utils.SharedPrefsHelper;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.yanytska.mobileapp.utils.Utils.isValidEmail;
import static com.yanytska.mobileapp.utils.Utils.isValidPhone;

public class ProfileEditFragment extends Fragment {

    private SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;

    private View view;

    private EditText usernameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    private ImageView currentUserPicture;
    private ImageView usernameSubmitBtn;
    private ImageView phoneSubmitBtn;
    private ImageView emailSubmitBtn;
    private ImageView uploadPictureBtn;
    private ImageView editDoneBtn;

    private StorageReference folderReference;
    private StorageReference imageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        initElements();
        initValues();
        initListeners();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageData = Objects.requireNonNull(data).getData();
            imageReference.putFile(
                    Objects.requireNonNull(imageData))
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(getActivity(), getString(R.string.saved),
                                Toast.LENGTH_SHORT).show();
                        placeImage();
                    });
        }
    }

    private void initElements() {
        usernameEditText = view.findViewById(R.id.profile_edit_name_et);
        phoneEditText = view.findViewById(R.id.profile_phone_edit_et);
        emailEditText = view.findViewById(R.id.profile_email_edit_et);

        uploadPictureBtn = view.findViewById(R.id.profile_photo_edit_iv);
        usernameSubmitBtn = view.findViewById(R.id.username_edit_done_iv);
        phoneSubmitBtn = view.findViewById(R.id.phone_edit_done_iv);
        emailSubmitBtn = view.findViewById(R.id.email_edit_done_iv);

        currentUserPicture = view.findViewById(R.id.profile_photo_iv);
        editDoneBtn = view.findViewById(R.id.profile_edit_done_iv);

        folderReference = FirebaseStorage.getInstance().getReference().child("folder");
    }

    private void initValues() {
        user = auth.getCurrentUser();

        if (user != null) {
            usernameEditText.setText(user.getDisplayName());
            emailEditText.setText(user.getEmail());
            phoneEditText.setText(sharedPrefsHelper.loadPhone());
            imageReference = folderReference.child(user.getUid() + ".jpg");

            placeImage();
        } else {
            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
        }
    }

    private void initListeners() {
        final Context mContext = view.getContext();

        usernameSubmitBtn.setOnClickListener(view -> {
            String newUsername = Objects.requireNonNull(usernameEditText.getText()).toString();

            if (Objects.equals(user.getDisplayName(), newUsername)) {
                usernameEditText.setError(getString(R.string.same_username));
                usernameEditText.requestFocus();
            } else if (!TextUtils.isEmpty(newUsername)) {
                updateUsername(user, newUsername);
            } else {
                usernameEditText.setError(getString(R.string.invalid_username));
                usernameEditText.requestFocus();
            }
        });

        phoneSubmitBtn.setOnClickListener(view -> {
            String newPhone = Objects.requireNonNull(phoneEditText.getText()).toString().trim();

            if (Objects.equals(sharedPrefsHelper.loadPhone(), newPhone)) {
                phoneEditText.setError(getString(R.string.same_phone));
                phoneEditText.requestFocus();
            } else if (isValidPhone(newPhone)) {
                updatePhone(newPhone);
            } else {
                phoneEditText.setError(getString(R.string.invalid_phone));
                phoneEditText.requestFocus();
            }
        });

        emailSubmitBtn.setOnClickListener(view -> {
            String newEmail = Objects.requireNonNull(emailEditText.getText()).toString().trim();

            if (Objects.equals(user.getEmail(), newEmail)) {
                emailEditText.setError(getString(R.string.same_email));
                emailEditText.requestFocus();
            } else if (isValidEmail(newEmail)) {
                updateEmail(user, newEmail);
            } else {
                emailEditText.setError(getString(R.string.invalid_email));
                emailEditText.requestFocus();
            }
        });

        uploadPictureBtn.setOnClickListener(view -> uploadProfilePicture());

        editDoneBtn.setOnClickListener(view -> {
            FragmentManager fragmentManager =
                    ((AppCompatActivity) mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
            fragmentManager.executePendingTransactions();
        });
    }

    private void uploadProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void placeImage() {
        imageReference
                .getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Picasso.get().load(uri.toString()).into(currentUserPicture));
    }

    private void updateUsername(final FirebaseUser user, final String newName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private void updateEmail(FirebaseUser user, final String newEmail) {
        user.updateEmail(newEmail).addOnCompleteListener(task ->
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT)
                        .show());
    }

    private void updatePhone(final String newPhone) {
        sharedPrefsHelper.savePhone(newPhone);
        Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
    }
}