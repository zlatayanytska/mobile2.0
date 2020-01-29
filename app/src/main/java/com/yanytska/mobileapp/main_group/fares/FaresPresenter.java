package com.yanytska.mobileapp.main_group.fares;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.yanytska.mobileapp.api.ApiService;
import com.yanytska.mobileapp.api.RetrofitClient;
import com.yanytska.mobileapp.data.model.Fare;
import com.yanytska.mobileapp.data.repository.FareRepository;
import com.yanytska.mobileapp.main_group.adapter.FareAdapter;
import com.yanytska.mobileapp.utils.InternetConnection;
import com.yanytska.mobileapp.utils.JSONParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaresPresenter implements IFaresPresenter {

    private IFaresView carsView;
    private Context context;

    private FareAdapter adapter;
    private FareRepository fareRepository;
    private String responseBody;

    public FaresPresenter(IFaresView iFaresView, Context context) {
        this.carsView = iFaresView;
        this.context = context;
        this.adapter = new FareAdapter(new ArrayList<>());
        this.carsView.setAdapter(this.adapter);
    }

    @Override
    public void loadData() {
        carsView.setEnabledSearch(false);

        if (InternetConnection.checkConnection(context)) {
            carsView.showProgress();

            loadCars();
        } else {
            carsView.showNotInternetConnection(context);
            carsView.hideRefreshing();
        }
    }

    @Override
    public void setList() {
        adapter.updateCars(fareRepository.getList());
    }

    @Override
    public void findCarByName(final String name) {
        if (name.isEmpty() || fareRepository.getByName(name) == null) {
            carsView.showNotFound(context);
        } else {
            adapter = new FareAdapter(fareRepository.getByName(name));
            carsView.setAdapter(adapter);
        }
    }

    private void loadCars() {
        final ApiService api = RetrofitClient.getRetroClient();

        Call<JsonArray> jsonArrayCall = api.getFares();

        jsonArrayCall.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {
                    responseBody = Objects.requireNonNull(response.body()).toString();

                    Type type = new TypeToken<List<Fare>>() {
                    }.getType();
                    List<Fare> arrayList = JSONParser.getFromJSONtoArrayList(responseBody, type);
                    fareRepository = new FareRepository(arrayList);

                    setList();
                    carsView.hideRefreshing();
                    carsView.setEnabledSearch(true);
                    carsView.hideProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                carsView.hideRefreshing();
                carsView.hideProgress();
            }
        });
    }
}
