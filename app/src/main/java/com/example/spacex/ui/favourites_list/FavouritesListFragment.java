package com.example.spacex.ui.favourites_list;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentFavouritesListBinding;
import com.example.spacex.ui.article_and_comments.SharedScreenFragment;
import com.example.spacex.ui.favourites_list.factory.FavouritesListViewModelFactory;
import com.example.spacex.ui.utils.Utils;

public class FavouritesListFragment extends Fragment {

    private FragmentFavouritesListBinding binding;
    private FavouritesListViewModel viewModel;

    public FavouritesListFragment() {
        super(R.layout.fragment_favourites_list);
    }

    private String getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("userId", null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentFavouritesListBinding.bind(view);
        String userId = getUserId();
        if (userId == null) {
            return;
        }
        viewModel = new ViewModelProvider(this, new FavouritesListViewModelFactory(userId))
                .get(FavouritesListViewModel.class);

        final FavouritesListAdapter adapter = new FavouritesListAdapter(
                this::viewArticle,
                viewModel::removeFromFavourites,
                viewModel::like,
                viewModel::dislike
        );

        binding.recycler.setAdapter(adapter);

        binding.refresh.setOnRefreshListener(viewModel::update);

        subscribe(viewModel, adapter);
    }

    private void subscribe(final FavouritesListViewModel viewModel, FavouritesListAdapter adapter) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean hasItems = state.getItems() != null && !state.getItems().isEmpty();
            boolean isSuccess = !state.isLoading() && state.getErrorMessage() == null && state.getItems() != null;

            binding.refresh.setEnabled(!state.isLoading());
            if (!state.isLoading()) binding.refresh.setRefreshing(false);

            binding.recycler.setVisibility(Utils.visibleOrGone(hasItems));

            if (isSuccess && !hasItems) {
                binding.error.setVisibility(View.VISIBLE);
                binding.error.setText(R.string.no_articles_in_favourites_yet);
            } else {
                binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
                binding.error.setText(state.getErrorMessage());
            }

            binding.loading.setVisibility(Utils.visibleOrGone(state.isLoading()));

            if (hasItems) {
                adapter.updateData(state.getItems());
                viewModel.getReactionMapLiveData().observe(getViewLifecycleOwner(), adapter::setReactionMap);
            }
        });
    }

    private void viewArticle(@NonNull String articleId) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(
                R.id.action_favouritesListFragment_to_articleScreenFragment,
                SharedScreenFragment.getBundle(articleId)
        );
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
