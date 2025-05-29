package com.example.spacex.ui.launches_list;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.LaunchRepositoryImpl;
import com.example.spacex.domain.entity.ItemLaunchEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.launch.GetLaunchListUseCase;

import java.util.List;

public class LaunchListViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();

    public final LiveData<State> stateLiveData = mutableLiveData;

    private final GetLaunchListUseCase getLaunchListUseCase = new GetLaunchListUseCase(
            LaunchRepositoryImpl.getInstance()
    );

    public LaunchListViewModel() {
        update();
    }

    public void update() {
        mutableLiveData.setValue(new State(null, null, true));
        getLaunchListUseCase.execute(status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<ItemLaunchEntity>> status) {
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
        private final List<ItemLaunchEntity> items;

        private final boolean isLoading;

        public State(
                @Nullable String errorMessage,
                @Nullable List<ItemLaunchEntity> items,
                boolean isLoading
        ) {
            this.errorMessage = errorMessage;
            this.items = items;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public List<ItemLaunchEntity> getItems() {
            return items;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
