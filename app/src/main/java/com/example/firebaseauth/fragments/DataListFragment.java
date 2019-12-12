package com.example.firebaseauth.fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firebaseauth.api.ApiService;
import com.example.firebaseauth.utils.ApplicationEx;
import com.example.firebaseauth.utils.NetworkChangeReceiver;
import com.example.firebaseauth.entities.Panel;
import com.example.firebaseauth.adapters.PanelAdapter;
import com.example.firebaseauth.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private PanelAdapter adapter;

    public DataListFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(Objects.requireNonNull(getView()));
        registerNetworkMonitoring();
        loadPanels();
    }

    private void registerNetworkMonitoring() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        NetworkChangeReceiver receiver = new NetworkChangeReceiver(linearLayout);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
    }

    private void initViews(View root){
        recyclerView = root.findViewById(R.id.data_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        linearLayout = root.findViewById(R.id.linearLayout);
        swipeRefreshLayout = root.findViewById(R.id.data_list_swipe_refresh);

        setupSwipeToRefresh();
    }

    private void setupSwipeToRefresh(){
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPanels();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    private void loadPanels(){
        swipeRefreshLayout.setRefreshing(true);
        final ApiService apiService = getApplicationEx().getApiService();
        Call<List<Panel>> call = apiService.getPanels();
        call.enqueue(new Callback<List<Panel>>() {
            @Override
            public void onResponse(final Call<List<Panel>> call,
                                   final Response<List<Panel>> response) {
                adapter = new PanelAdapter(getActivity(), response.body());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Panel>> call, Throwable t) {
                Snackbar.make(linearLayout, R.string.failure, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private ApplicationEx getApplicationEx(){
        return ((ApplicationEx) Objects.requireNonNull(getActivity()).getApplication());
    }
}