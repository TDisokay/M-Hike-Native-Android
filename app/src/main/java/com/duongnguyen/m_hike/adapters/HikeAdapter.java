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
import com.duongnguyen.m_hike.models.Hike;
import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {
    private final Context context;
    private final ArrayList<Hike> hikeList;
    private final OnHikeActionListener actionListener;

    // Interface for click events
    public interface OnHikeActionListener {
        void onHikeClick(Hike hike);
        void onEditClick(Hike hike);
        void onDeleteClick(Hike hike);
    }

    public HikeAdapter(Context context, ArrayList<Hike> hikeList, OnHikeActionListener listener) {
        this.context = context;
        this.hikeList = hikeList;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hike, parent, false);
        return new HikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        if (hike == null) return;

        holder.tvHikeName.setText(hike.getName());
        holder.tvHikeLocation.setText(hike.getLocation());
        holder.tvHikeDate.setText(hike.getHikeDate());

        // Click listener for whole item
        holder.itemView.setOnClickListener(v -> actionListener.onHikeClick(hike));

        // Click listener for edit button
        holder.btnEditHike.setOnClickListener(v -> actionListener.onEditClick(hike));

        // Click listener for delete button
        holder.btnDeleteHike.setOnClickListener(v -> actionListener.onDeleteClick(hike));
    }

    @Override
    public int getItemCount() {
        return hikeList.size();
    }

    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        TextView tvHikeName, tvHikeLocation, tvHikeDate;
        ImageButton btnEditHike, btnDeleteHike;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHikeName = itemView.findViewById(R.id.tvHikeName);
            tvHikeLocation = itemView.findViewById(R.id.tvHikeLocation);
            tvHikeDate = itemView.findViewById(R.id.tvHikeDate);
            btnEditHike = itemView.findViewById(R.id.btnEditHike);
            btnDeleteHike = itemView.findViewById(R.id.btnDeleteHike);
        }
    }
}