package com.example.spacex.ui.sign.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.UserRepositoryImpl;
import com.example.spacex.domain.sign.CreateUserUseCase;
import com.example.spacex.domain.sign.IsUserExistUseCase;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;

    private final MutableLiveData<Void> mutableOpenLoginLiveData = new MutableLiveData<>();
    public final LiveData<Void> openLoginLiveData = mutableOpenLoginLiveData;

    /* UseCases */

    private final CreateUserUseCase createUserUseCase = new CreateUserUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final IsUserExistUseCase isUserExistUseCase = new IsUserExistUseCase(
            UserRepositoryImpl.getInstance()
    );

    /* UseCases */


    @Nullable
    private String name = null;

    @Nullable
    private String username = null;

    @Nullable
    private String email = null;

    @Nullable
    private String password = null;


    public void changeName(@NonNull String name) {
        this.name = name;
    }

    public void changeUsername(@NonNull String username) {
        this.username = username;
    }

    public void changeEmail(@NonNull String email) {
        this.email = email;
    }

    public void changePassword(@NonNull String password) {
        this.password = password;
    }

    public void register() {
        final String currentUsername = username;

        final String currentPassword = password;

        final String currentName = name;

        final String currentEmail = email;

        if (currentUsername == null || currentUsername.isEmpty()) {
            mutableErrorLiveData.postValue("Username cannot be empty");
            return;
        }

        if (currentName == null || currentName.isEmpty()) {
            mutableErrorLiveData.postValue("Name cannot be empty");
            return;
        }

        if (currentEmail == null || currentEmail.isEmpty()) {
            mutableErrorLiveData.postValue("Email cannot be empty");
            return;
        }

        if (currentPassword == null || currentPassword.isEmpty()) {
            mutableErrorLiveData.postValue("Password cannot be empty");
            return;
        }

        isUserExistUseCase.execute(currentUsername, status -> {
            if (status.getValue() == null || status.getError() != null) {
                mutableErrorLiveData.postValue("Something went wrong. Try again later");
                return;
            }
            if (status.getValue()) {
                mutableErrorLiveData.postValue("User already exists. Please login");
            } else {
                createAccount(currentUsername, currentName, currentEmail, currentPassword);
            }
        });
    }

    private void createAccount(
            @NonNull final String currentUsername,
            @NonNull final String currentName,
            @NonNull final String currentEmail,
            @NonNull final String currentPassword
    ) {
        createUserUseCase.execute(
                currentName,
                currentUsername,
                currentEmail,
                currentPassword,
                status -> {
                    if (status.getStatusCode() == 201 && status.getError() == null) {
                        mutableOpenLoginLiveData.postValue(null);
                    } else {
                        mutableErrorLiveData.postValue("Something wrong. Try again later");
                    }
                });
    }
}
