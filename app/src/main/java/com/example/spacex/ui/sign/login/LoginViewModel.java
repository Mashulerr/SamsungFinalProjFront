package com.example.spacex.ui.sign.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.spacex.data.repository.UserRepositoryImpl;
import com.example.spacex.data.source.CredentialsDataSource;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.domain.sign.IsUserExistUseCase;
import com.example.spacex.domain.sign.LoginUserUseCase;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;

    private final MutableLiveData<Void> mutableOpenRegisterLiveData = new MutableLiveData<>();
    public final LiveData<Void> openRegisterLiveData = mutableOpenRegisterLiveData;

    private final MutableLiveData<Boolean> mutableOpenEventsLiveData = new MutableLiveData<>();
    public final LiveData<Boolean> openEventsLiveData = mutableOpenEventsLiveData;

    /* UseCases */
    private final IsUserExistUseCase isUserExistUseCase = new IsUserExistUseCase(
            UserRepositoryImpl.getInstance()
    );
    private final LoginUserUseCase loginUserUseCase = new LoginUserUseCase(
            UserRepositoryImpl.getInstance()
    );
    /* UseCases */

    private String username;
    private String password;

    private final SharedPreferences sharedPreferences;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        loadSavedUser();
    }

    public void changeUsername(@NonNull String username) {
        this.username = username;
    }

    public void changePassword(@NonNull String password) {
        this.password = password;
    }

    public void login() {
        if (username == null || username.isEmpty()) {
            mutableErrorLiveData.postValue("Username cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()) {
            mutableErrorLiveData.postValue("Password cannot be empty");
            return;
        }

        isUserExistUseCase.execute(username, status -> {
            if (status.getValue() == null || status.getError() != null) {
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
                return;
            }
            if (!status.getValue()) {
                mutableErrorLiveData.postValue("User does not exist. Please register");
            } else {
                loginUser(username, password);
            }
        });
    }

    private void loginUser(@NonNull final String currentUsername, @NonNull final String currentPassword) {
        loginUserUseCase.execute(currentUsername, currentPassword, status -> {
            if (status.getStatusCode() == 200 && status.getError() == null) {
                UserEntity user = status.getValue();
                if (user != null) {
                    CredentialsDataSource.getInstance().updateLogin(currentUsername, currentPassword);

                    saveUserData(currentUsername, currentPassword);
                    saveFullUserData(user);
                    mutableOpenEventsLiveData.postValue(true);
                } else {
                    mutableErrorLiveData.postValue("Invalid username or password");
                }
            } else if (status.getStatusCode() == 401) {
                mutableErrorLiveData.postValue("Invalid password");
            } else {
                String errorMessage = status.getError() != null
                        ? status.getError().getMessage()
                        : "Something went wrong. Try again later";
                mutableErrorLiveData.postValue(errorMessage);
            }
        });
    }

    private void saveUserData(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }

    private void saveUserId(String userId) {
        sharedPreferences.edit().putString("userId", userId).apply();
    }

    private void saveUserPhoto(String photoUrl) {
        sharedPreferences.edit().putString("photoUrl", photoUrl).apply();
    }

    private void saveUserLoggedIn(boolean isLoggedIn) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply();
    }

    public void saveFullUserData(@NonNull UserEntity user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getId());
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("email", user.getEmail());
        editor.putString("phone", user.getPhone());
        editor.putString("photoUrl", user.getPhotoUrl());
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        this.username = user.getUsername();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    public void loadSavedUser() {
        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);
        if (savedUsername != null) changeUsername(savedUsername);
        if (savedPassword != null) changePassword(savedPassword);
        if (savedUsername != null && savedPassword != null) {
            CredentialsDataSource.getInstance().updateLogin(savedUsername, savedPassword);
        }
    }
}
