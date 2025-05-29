package com.example.spacex.ui.launch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.LaunchRepositoryImpl;
import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.launch.GetLaunchByFlightNumberUseCase;

public class LaunchViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final GetLaunchByFlightNumberUseCase getLaunchByFlightNumberUseCase = new GetLaunchByFlightNumberUseCase(
            LaunchRepositoryImpl.getInstance()
    );

    public void load(@NonNull String flightNumber) {
        mutableLiveData.setValue(new State(null, null, true));
        getLaunchByFlightNumberUseCase.execute(flightNumber, status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<FullLaunchEntity> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public static class State {

        @Nullable
        private final String errorMessage;

        @Nullable
        private final FullLaunchEntity launch;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable FullLaunchEntity launch, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.launch = launch;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public FullLaunchEntity getLaunch() {
            return launch;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}