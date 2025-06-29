package com.example.spacex.ui.launches_list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentLaunchesListBinding;
import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.example.spacex.ui.launch.LaunchFragment;
import com.example.spacex.ui.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LaunchListFragment extends Fragment {

    private FragmentLaunchesListBinding binding;

    private LaunchListViewModel viewModel;

    public LaunchListFragment() {
        super(R.layout.fragment_launches_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLaunchesListBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LaunchListViewModel.class);
        binding.refresh.setOnRefreshListener(() -> viewModel.update());
        final LaunchListAdapter adapter = new LaunchListAdapter(flightNumber -> viewLaunch(flightNumber));
        binding.recycler.setAdapter(adapter);
        subscribe(viewModel, adapter);
    }

    private void subscribe(final LaunchListViewModel viewModel, LaunchListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getItems() != null;
            binding.refresh.setEnabled(!state.isLoading());
            if (!state.isLoading()) binding.refresh.setRefreshing(false);
            binding.recycler.setVisibility(Utils.visibleOrGone(isSuccess));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.loading.setVisibility(Utils.visibleOrGone(state.isLoading()));

            binding.error.setText(state.getErrorMessage());
            if (isSuccess) {
                List<ItemLaunchEntity> reversed = new ArrayList<>(state.getItems());
                Collections.reverse(reversed);
                adapter.updateData(reversed);
            }

        });
    }

    private void viewLaunch(@NonNull String flightNumber) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(
                R.id.action_launchListFragment_to_launchFragment,
                LaunchFragment.getBundle(flightNumber)
        );
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
