package com.yanytska.mobileapp.main_group.fare;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.data.model.Fare;
import com.squareup.picasso.Picasso;

public class FareWithDetailsFragment extends Fragment {

    private View view;
    private Fare fare;

    private TextView fullCarNameTv;
    private TextView fullCarIdTv;
    private TextView fullCarTypeTv;

    private ImageView carImageIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fare, container, false);

        initView();
        setInfoToCar(fare);
        return view;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    private void initView() {
        RecyclerView recyclerView = view.findViewById(R.id.car_rv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        fullCarNameTv = view.findViewById(R.id.full_car_name_tv);
        fullCarIdTv = view.findViewById(R.id.full_car_id_tv);
        fullCarTypeTv = view.findViewById(R.id.full_car_type_tv);

        carImageIv = view.findViewById(R.id.car_image_iv);
    }

    private void setInfoToCar(Fare fare) {
        fullCarNameTv.setText(fare.getUuid());
        fullCarIdTv.setText(String.valueOf(fare.getId()));
        fullCarTypeTv.setText(fare.getType());

        Picasso.get()
                .load(fare.getImage())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
                .into(carImageIv);
    }
}

