package com.example.spacex.ui.update_profile;


import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.spacex.data.repository.UserRepositoryImpl;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.domain.user.GetUserByIdUseCase;
import com.example.spacex.domain.user.UpdateUserUseCase;
import com.example.spacex.ui.service.CloudinaryConfig;
import com.example.spacex.ui.service.UserSessionManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class UpdateProfileViewModel extends ViewModel {

    //* UseCases *//
    private final UpdateUserUseCase updateUserUseCase = new UpdateUserUseCase(
            UserRepositoryImpl.getInstance()
    );
    private final GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCase(
            UserRepositoryImpl.getInstance()
    );
    //* UseCases *//

    private final MutableLiveData<State> mutableStateLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableStateLiveData;

    private final MutableLiveData<Void> mutableOpenProfileLiveData = new MutableLiveData<>();
    public final LiveData<Void> openProfileLiveData = mutableOpenProfileLiveData;

    private final UserSessionManager userSessionManager;

    private UserEntity currentUser;

    private String newName;
    private String newEmail;
    private String newPhone;
    private String newPhotoUrl = null;

    public UpdateProfileViewModel(UserSessionManager userSessionManager) {
        this.userSessionManager = userSessionManager;
    }

    public void load(@NonNull String userId) {
        mutableStateLiveData.postValue(new State(null, null, true));

        getUserByIdUseCase.execute(userId, status -> {
            if (status.getStatusCode() == 200 && status.getValue() != null) {
                currentUser = status.getValue();
                mutableStateLiveData.postValue(new State(null, currentUser, false));
            } else {
                mutableStateLiveData.postValue(new State("Failed to load user", null, false));
            }
        });
    }

    public void changeName(String name) {
        this.newName = name;
    }

    public void changeEmail(String email) {
        this.newEmail = email;
    }

    public void changePhone(String phone) {
        this.newPhone = phone;
    }

    public void save(@NonNull String userId) {
        if (currentUser == null) {
            mutableStateLiveData.postValue(new State("Cannot save: user not loaded", null, false));
            return;
        }

        performUserUpdate(currentUser.getPhotoUrl());
    }

    private void performUserUpdate(String existingPhotoUrl) {
        if (currentUser == null) {
            mutableStateLiveData.postValue(new State("Cannot update: user not loaded", null, false));
            return;
        }

        String finalName = (newName == null || newName.isEmpty()) ? currentUser.getName() : newName;
        String finalEmail = (newEmail == null || newEmail.isEmpty()) ? currentUser.getEmail() : newEmail;
        String finalPhone = (newPhone == null || newPhone.isEmpty()) ? currentUser.getPhone() : newPhone;
        String finalUsername = currentUser.getUsername();

        String finalPhotoUrl = (newPhotoUrl != null) ? newPhotoUrl : existingPhotoUrl;

        if (finalUsername == null || finalUsername.isEmpty()) {
            mutableStateLiveData.postValue(new State("Username is null or empty before update", null, false));
            return;
        }

        updateUserUseCase.execute(
                currentUser.getId(),
                finalName,
                finalUsername,
                finalEmail,
                finalPhone,
                finalPhotoUrl,
                status -> {
                    if (status.getStatusCode() == 200) {
                        UserEntity updatedUser = new UserEntity(
                                currentUser.getId(),
                                finalPhone,
                                finalName,
                                finalUsername,
                                finalEmail,
                                finalPhotoUrl
                        );
                        userSessionManager.saveUser(updatedUser);
                        currentUser = updatedUser;
                        newPhotoUrl = null;

                        mutableOpenProfileLiveData.postValue(null);
                    } else if (status.getStatusCode() == 500) {
                        mutableStateLiveData.postValue(new State("Email or phone is already used", null, false));
                    } else {
                        mutableStateLiveData.postValue(new State("Failed to save. Try again later", null, false));
                    }
                }
        );

    }

    public void uploadAvatar(Uri croppedImageUri, ContentResolver contentResolver) {
        mutableStateLiveData.postValue(new State(null, currentUser, true));
        new Thread(() -> {
            try {
                InputStream inputStream = contentResolver.openInputStream(croppedImageUri);
                if (inputStream == null) {
                    postError("Failed to open image stream");
                    return;
                }
                byte[] imageData = getBytes(inputStream);
                Cloudinary cloudinary = CloudinaryConfig.getInstance();
                Map uploadResult = cloudinary.uploader().upload(imageData, ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");
                if (imageUrl == null || imageUrl.isEmpty()) {
                    postError("Failed to upload image to Cloudinary");
                    return;
                }
                newPhotoUrl = imageUrl;

                if (currentUser != null) {
                    UserEntity updatedUser = new UserEntity(
                            currentUser.getId(),
                            currentUser.getPhone(),
                            currentUser.getName(),
                            currentUser.getUsername(),
                            currentUser.getEmail(),
                            imageUrl
                    );

                    currentUser = updatedUser;
                    userSessionManager.saveUser(updatedUser);
                    mutableStateLiveData.postValue(new State(null, updatedUser, false));
                }

            } catch (Exception e) {
                postError("Failed to upload avatar: " + e.getMessage());
            }
        }).start();
    }


    private void postError(String message) {
        mutableStateLiveData.postValue(new State(message, currentUser, false));
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    public static class State {

        @Nullable
        private final String errorMessage;
        @Nullable
        private final UserEntity user;
        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable UserEntity user, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.user = user;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public UserEntity getUser() {
            return user;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
