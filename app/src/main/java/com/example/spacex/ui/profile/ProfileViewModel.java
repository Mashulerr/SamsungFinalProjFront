package com.example.spacex.ui.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.UserRepositoryImpl;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.domain.sign.LogoutUseCase;
import com.example.spacex.domain.user.GetUserByIdUseCase;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final MutableLiveData<Void> mutableLogoutLiveData = new MutableLiveData<>();
    public final LiveData<Void> logoutLiveData = mutableLogoutLiveData;

    /* UseCases */
    private final GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCase(
            UserRepositoryImpl.getInstance()
    );

    private final LogoutUseCase logoutUseCase = new LogoutUseCase(
            UserRepositoryImpl.getInstance()
    );
    /* UseCases */

    private State fromStatus(Status<UserEntity> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public void load(@NonNull String id) {
        mutableLiveData.setValue(new State(null, null, true));
        getUserByIdUseCase.execute(id, status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    public void logout() {
        logoutUseCase.execute();
        mutableLogoutLiveData.postValue(null);
    }

    public class State{

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
