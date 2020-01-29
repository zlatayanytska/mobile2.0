package com.yanytska.mobileapp.main_group.fares;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.api.ApiService;
import com.yanytska.mobileapp.api.RetrofitClient;
import com.yanytska.mobileapp.data.model.Fare;
import com.yanytska.mobileapp.main_group.fare.AddFareFragment;
import com.yanytska.mobileapp.main_group.fare.FareWithDetailsFragment;
import com.yanytska.mobileapp.utils.JSONParser;

import java.lang.reflect.Type;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    FaresFragment extends Fragment implements IFaresView, View.OnClickListener {

    private View view;
    private Context mContext;

    private RecyclerView recyclerView;
    private FloatingActionButton searchFab;
    private FloatingActionButton addFab;

    private FaresPresenter carsPresenter;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fares, container, false);
        mContext = view.getContext();

        initView();
        initPresenter();

        if (Objects.requireNonNull(getActivity()).getIntent().hasExtra("fareId")) {
            String carId = getActivity().getIntent().getStringExtra("fareId");
            String message = getActivity().getIntent().getStringExtra("message");

            loadOneCar(carId);
            showMessage(message);
        } else {
            initListener();

            carsPresenter.loadData();

            swipeRefreshLayout.setOnRefreshListener(() -> carsPresenter.loadData());
        }

        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.car_rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        searchFab = view.findViewById(R.id.car_search_fab);
        addFab = view.findViewById(R.id.car_add_fab);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshCars);
    }

    private void initListener() {
        searchFab.setOnClickListener(this);
        addFab.setOnClickListener(this);
    }

    private void initPresenter() {
        carsPresenter = new FaresPresenter(this, mContext);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.car_search_fab) {
            final EditText editText = new EditText(mContext);
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setTitle(R.string.search_car)
                    .setMessage(R.string.search_car_message)
                    .setView(editText)
                    .setPositiveButton(R.string.search_label, (dialogInterface, i) -> {
                        String id = editText.getText().toString();
                        carsPresenter.findCarByName(id);
                    })
                    .setNegativeButton(R.string.close, null)
                    .create();
            dialog.show();
        }

        if (view.getId() == R.id.car_add_fab) {
            AddFareFragment addFareFragment = new AddFareFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_main_container, addFareFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
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

    @Override
    public void showProgress() {
        showProgressLoaderWithBackground(true, mContext.getString(R.string.load_data));
    }

    @Override
    public void hideProgress() {
        showProgressLoaderWithBackground(false, mContext.getString(R.string.load_data));
    }

    @Override
    public void hideRefreshing() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setEnabledSearch(boolean enabled) {
        searchFab.setEnabled(enabled);
    }

    public void showCar(Fare fare) {
        FareWithDetailsFragment fareWithDetailsFragment = new FareWithDetailsFragment();
        FragmentManager fragmentManager =
                Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_main_container, fareWithDetailsFragment)
                .addToBackStack(null)
                .commit();
        fareWithDetailsFragment.setFare(fare);
    }

    private void loadOneCar(final String id) {
        final ApiService api = RetrofitClient.getRetroClient();

        Call<JsonObject> jsonArrayCall = api.getFareById(id);

        jsonArrayCall.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String responseBody = Objects.requireNonNull(response.body()).toString();

                    Type type = new TypeToken<Fare>() {
                    }.getType();
                    Fare fare = JSONParser.getFromJSONToFare(responseBody, type);
                    showCar(fare);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showMessage(final String message) {
        final TextView editText = new TextView(mContext);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("Received message")
                .setMessage(message)
                .setView(editText)
                .setNegativeButton(R.string.close, null)
                .create();
        dialog.show();
    }
}