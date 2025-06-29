package com.example.spacex.ui.events_list;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentEventsListBinding;
import com.example.spacex.ui.event.EventFragment;
import com.example.spacex.ui.utils.Utils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class EventListFragment extends Fragment {

    private FragmentEventsListBinding binding;

    private EventListViewModel viewModel;

    public EventListFragment(){
        super(R.layout.fragment_events_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEventsListBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(EventListViewModel.class);
        binding.refresh.setOnRefreshListener(() -> viewModel.update());
        final EventListAdapter adapter = new EventListAdapter(id -> viewEvent(id));
        binding.recycler.setAdapter(adapter);
        subscribe(viewModel, adapter);
    }

    private void subscribe(final EventListViewModel viewModel, final EventListAdapter adapter) {
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
                adapter.updateData(new ArrayList<>(state.getItems()).stream()
                        .sorted((e1, e2) -> e2.getDataUtc().compareTo(e1.getDataUtc()))
                        .collect(Collectors.toList()));

            }
        });
    }

    private void viewEvent(@NonNull String id) {
        View view = getView();
        if (view == null) return;
      Navigation.findNavController(view).navigate(
               R.id.action_eventListFragment_to_eventFragment,
                EventFragment.getBundle(id)
      );
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
