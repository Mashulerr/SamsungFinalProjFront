package com.example.spacex.ui.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.EventRepositoryImpl;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.event.GetEventByIdUseCase;
import com.example.spacex.domain.entity.FullEventEntity;

public class EventViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final GetEventByIdUseCase getEventByIdUseCase = new GetEventByIdUseCase(
            EventRepositoryImpl.getInstance()
    );

    public void load(@NonNull String id) {
        mutableLiveData.setValue(new State(null, null, true));
        getEventByIdUseCase.execute(id, status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<FullEventEntity> status) {
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
        private final FullEventEntity event;
        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable FullEventEntity event, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.event = event;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public FullEventEntity getEvent() {
            return event;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}