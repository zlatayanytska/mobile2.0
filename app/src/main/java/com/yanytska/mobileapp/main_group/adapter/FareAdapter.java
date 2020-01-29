package com.yanytska.mobileapp.main_group.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yanytska.mobileapp.R;
import com.yanytska.mobileapp.data.model.Fare;
import com.yanytska.mobileapp.main_group.fares.FaresFragment;

import java.util.List;

public class FareAdapter extends RecyclerView.Adapter<FareAdapter.ViewHolder> {

    private List<Fare> fareList;

    private FaresFragment carWithDetailsFragment;

    public FareAdapter(List<Fare> list) {
        this.fareList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view_fare, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Context mContext = holder.itemView.getContext();
        final Fare fare = fareList.get(position);

        holder.uuidTv.setText(fare.getUuid());

        holder.idTv.setText(String.valueOf(fare.getId()));
        holder.statusTv.setText(fare.getType());

        holder.seeDetailsBtn.setOnClickListener(view -> {
            carWithDetailsFragment = new FaresFragment();

            FragmentManager fragmentManager =
                    ((AppCompatActivity) mContext).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main_container, carWithDetailsFragment)
                    .addToBackStack(null)
                    .commit();
            fragmentManager.executePendingTransactions();
            carWithDetailsFragment.showCar(fare);
        });
    }

    @Override
    public int getItemCount() {
        return fareList.size();
    }

    public void updateCars(final List<Fare> fares) {
        this.fareList = fares;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView uuidTv;
        private TextView idTv;
        private TextView statusTv;

        private Button seeDetailsBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            uuidTv = itemView.findViewById(R.id.row_fare_uuid_tv);
            idTv = itemView.findViewById(R.id.row_fare_id_tv);
            statusTv = itemView.findViewById(R.id.row_fare_status_tv);

            seeDetailsBtn = itemView.findViewById(R.id.row_fare_see_details_btn);
        }
    }
}
