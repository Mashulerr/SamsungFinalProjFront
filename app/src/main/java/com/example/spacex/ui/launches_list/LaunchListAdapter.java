package com.example.spacex.ui.launches_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacex.R;
import com.example.spacex.databinding.ItemLaunchBinding;
import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LaunchListAdapter extends RecyclerView.Adapter<LaunchListAdapter.ViewHolder> {

    @NonNull
    private final Consumer<String> onItemClick;

    private final List<ItemLaunchEntity> data = new ArrayList<>();


    public LaunchListAdapter(@NonNull Consumer<String> onItemClick) {

        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public LaunchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemLaunchBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull LaunchListAdapter.ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<ItemLaunchEntity> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemLaunchBinding binding;

        public ViewHolder(@NonNull ItemLaunchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ItemLaunchEntity item) {
            binding.tvMissionName.setText(item.getMissionName());
            binding.tvLaunchYear.setText(item.getLaunchYear());
            binding.tvRocketName.setText(item.getRocketName());
            Context context = binding.getRoot().getContext();

            binding.tvStatus.setText(item.isSuccess()
                    ? context.getString(R.string.status_success)
                    : context.getString(R.string.status_failed));
            if (item.getMissionPatch() != null) {
                Picasso.get().load(item.getMissionPatch()).into(binding.imageMission);
            }
            binding.getRoot().setOnClickListener(v -> {
                onItemClick.accept(item.getFlightNumber());
            });
        }
    }
}
