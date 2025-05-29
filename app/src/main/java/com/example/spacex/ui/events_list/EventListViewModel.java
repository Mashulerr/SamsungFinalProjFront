package com.example.spacex.ui.events_list;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.EventRepositoryImpl;
import com.example.spacex.domain.event.GetEventListUseCase;
import com.example.spacex.domain.entity.ItemEventEntity;
import com.example.spacex.domain.entity.Status;

import java.util.List;

public class EventListViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();

    public final LiveData<State> stateLiveData = mutableLiveData;

    private final GetEventListUseCase getEventListUseCase = new GetEventListUseCase(
            EventRepositoryImpl.getInstance()
    );

    public EventListViewModel(){
        update();
    }

    public void update() {
        mutableLiveData.setValue(new State(null, null, true));
        getEventListUseCase.execute(status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemEventEntity>> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public class State {

        @Nullable
        private final String errorMessage;

        @Nullable
        private final List<ItemEventEntity> items;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable List<ItemEventEntity> items, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.items = items;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public List<ItemEventEntity> getItems() {
            return items;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
