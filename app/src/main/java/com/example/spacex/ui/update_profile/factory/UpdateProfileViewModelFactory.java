package com.example.spacex.ui.update_profile.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.ui.update_profile.UpdateProfileViewModel;
import com.example.spacex.ui.service.UserSessionManager;

public class UpdateProfileViewModelFactory implements ViewModelProvider.Factory {
    private final UserSessionManager userSessionManager;

    public UpdateProfileViewModelFactory(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UpdateProfileViewModel.class)) {
            return (T) new UpdateProfileViewModel(userSessionManager);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

