package com.yanytska.mobileapp.main_group.fare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.api.ApiService;
import com.yanytska.mobileapp.api.RetrofitClient;
import com.yanytska.mobileapp.data.model.Fare;
import com.yanytska.mobileapp.utils.InternetConnection;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddFareFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private static final String FOLDER_NAME = "folder";
    private static final String JPG_EXTENSION = ".jpg";

    private ApiService api;

    private View view;
    private Context context;

    private EditText nameEt;
    private EditText typeEt;

    private ImageView carImageIv;
    private ImageView carImageChooseIv;

    private Button addCarBtn;
    private Button closeBtn;

    private StorageReference imageReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_fare, container, false);
        context = getContext();

        initView();
        initListeners();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageData = Objects.requireNonNull(data).getData();
            imageReference.putFile(
                    Objects.requireNonNull(imageData))
                    .addOnSuccessListener(taskSnapshot -> placeImage());
        }
    }

    private void initView() {
        nameEt = view.findViewById(R.id.name_et);
        typeEt = view.findViewById(R.id.type_et);

        carImageIv = view.findViewById(R.id.car_photo_iv);
        carImageChooseIv = view.findViewById(R.id.car_photo_edit_iv);

        addCarBtn = view.findViewById(R.id.add_car_btn);
        closeBtn = view.findViewById(R.id.close_car_btn);

        StorageReference folderReference =
                FirebaseStorage.getInstance().getReference().child(FOLDER_NAME);
        imageReference =
                folderReference.child(LocalDateTime.now().toString().trim() + JPG_EXTENSION);
        api = RetrofitClient.getRetroClient();
    }

    private void initListeners() {
        carImageChooseIv.setOnClickListener(view -> uploadPicture());

        closeBtn.setOnClickListener(view -> getFragmentManager().popBackStack());

        addCarBtn.setOnClickListener(view -> uploadData());
    }

    private void uploadData() {
        final String name = nameEt.getText().toString();
        final String type = typeEt.getText().toString();

        if (areFieldsValid(name, type)) {
            imageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                Fare fare = new Fare(name, type, uri.toString());
                sendData(fare);
            });
        } else {
            showFieldsError();
        }
    }

    private void uploadPicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void placeImage() {
        imageReference.getDownloadUrl().addOnSuccessListener(
                uri -> Picasso.get().load(uri.toString()).into(carImageIv));
    }

    private void sendData(Fare fare) {
        if (InternetConnection.checkConnection(context)) {
            showProgress();

            processCall(fare);
        } else {
            showNoInternetConnection();
        }
    }

    private void processCall(Fare fare) {
        Call<Fare> call = api.addFare(fare);
        call.enqueue(new Callback<Fare>() {
            @Override
            public void onResponse(Call<Fare> call, Response<Fare> response) {
                hideProgress();
                Objects.requireNonNull(getFragmentManager()).popBackStack();
            }

            @Override
            public void onFailure(Call<Fare> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void showProgress() {
        showProgressLoaderWithBackground(true, context.getString(R.string.load_data));
    }

    private void hideProgress() {
        showProgressLoaderWithBackground(false, context.getString(R.string.load_data));
    }

    private void showProgressLoaderWithBackground(boolean visibility, String text) {
        if (text == null) {
            text = "";
        }

        ((TextView) view.findViewById(R.id.progress_bar_text)).setText(text);

        if (visibility) {
            view.findViewById(R.id.container_progress_bar).setVisibility(View.VISIBLE);
            Objects.requireNonNull(getActivity()).getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            view.findViewById(R.id.container_progress_bar).setVisibility(View.GONE);
            Objects.requireNonNull(getActivity()).getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void showNoInternetConnection() {
        Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    private void showFieldsError() {
        Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
    }

    private boolean areFieldsValid(final String name, final String type) {
        if (TextUtils.isEmpty(name)) {
            nameEt.setError(context.getString(R.string.invalid_field));
        }

        if (TextUtils.isEmpty(type)) {
            typeEt.setError(context.getString(R.string.invalid_field));
        }

        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(type) ;
    }
}
