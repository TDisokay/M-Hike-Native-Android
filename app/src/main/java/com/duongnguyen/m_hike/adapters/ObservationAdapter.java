package com.duongnguyen.m_hike.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.duongnguyen.m_hike.R;
import com.duongnguyen.m_hike.models.Observation;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {

    private final Context context;
    private final ArrayList<Observation> observationList;
    private final OnObservationActionListener listener;

    public interface OnObservationActionListener {
        void onDeleteClick(Observation observation);
    }

    public ObservationAdapter(Context context, ArrayList<Observation> observationList, OnObservationActionListener listener) {
        this.context = context;
        this.observationList = observationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_observation, parent, false);
        return new ObservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        Observation observation = observationList.get(position);

        holder.tvObservationText.setText(observation.getObservation());
        holder.tvObservationTime.setText(observation.getObservationTime());

        if (observation.getComments() != null && !observation.getComments().isEmpty()) {
            holder.tvObservationComments.setText(observation.getComments());
            holder.tvObservationComments.setVisibility(View.VISIBLE);
        } else {
            holder.tvObservationComments.setVisibility(View.GONE);
        }

        holder.btnDeleteObservation.setOnClickListener(v -> listener.onDeleteClick(observation));
    }

    @Override
    public int getItemCount() {
        return observationList.size();
    }

    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvObservationText, tvObservationTime, tvObservationComments;
        ImageButton btnDeleteObservation;

        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvObservationText = itemView.findViewById(R.id.tvObservationText);
            tvObservationTime = itemView.findViewById(R.id.tvObservationTime);
            tvObservationComments = itemView.findViewById(R.id.tvObservationComments);
            btnDeleteObservation = itemView.findViewById(R.id.btnDeleteObservation);
        }
    }
}