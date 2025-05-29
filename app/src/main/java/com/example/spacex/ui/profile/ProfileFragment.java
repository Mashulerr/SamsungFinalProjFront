package com.example.spacex.ui.profile;


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
import com.example.spacex.databinding.FragmentProfileBinding;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.ui.update_profile.UpdateProfileFragment;
import com.example.spacex.ui.utils.MyNavigator;
import com.example.spacex.ui.utils.Utils;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private ProfileViewModel viewModel;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentProfileBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        subscribe(viewModel);
        viewModel.load(getUserId());
    }

    private String getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("userId", null);
    }


    private void subscribe(final ProfileViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getUser() != null;

            binding.progressbar.setVisibility(Utils.visibleOrGone(state.isLoading()));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.error.setText(state.getErrorMessage());
            binding.constraintParent.setVisibility(Utils.visibleOrGone(isSuccess));
            if (isSuccess) {
                UserEntity user = state.getUser();
                binding.name.setText(user.getName());
                if (user.getPhone() != null){
                    binding.phone.setText(user.getPhone());
                } else {
                    binding.phone.setText(R.string.you_havent_add_your_phone_number_yet);
                }
                binding.username.setText(user.getUsername());
                binding.email.setText(user.getEmail());
                binding.linearLogout.setOnClickListener(v -> viewModel.logout());
                binding.linearFavourites.setOnClickListener(v -> viewFavourites());
                if (user.getPhotoUrl() != null) {
                    Picasso.get().load(user.getPhotoUrl()).into(binding.userImage);
                } else {
                    binding.userImage.setImageResource(R.drawable.ic_default_user_avatar);
                }
                binding.constraintProfile.setOnClickListener(v -> viewUpdateFragment(getUserId()));
            }
        });
        viewModel.logoutLiveData.observe(getViewLifecycleOwner(), unused -> {
            ((MyNavigator) requireActivity()).onLogout();
        });
    }

    private void viewUpdateFragment(@NonNull String id) {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(
                R.id.action_profileFragment_to_updateProfileFragment,
                UpdateProfileFragment.getBundle(id)
        );
    }

    private void viewFavourites() {
        View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(
                R.id.action_profileFragment_to_favouritesListFragment
        );
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
