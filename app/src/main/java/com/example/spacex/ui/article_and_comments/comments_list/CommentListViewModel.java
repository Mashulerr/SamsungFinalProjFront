package com.example.spacex.ui.article_and_comments.comments_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.CommentRepositoryImpl;
import com.example.spacex.domain.comment.AddCommentUseCase;
import com.example.spacex.domain.comment.GetCommentListUseCase;
import com.example.spacex.domain.entity.CommentEntity;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.ui.service.UserSessionManager;

import java.util.List;

public class CommentListViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final MutableLiveData<String> mutableErrorLiveData = new MutableLiveData<>();
    public final LiveData<String> errorLiveData = mutableErrorLiveData;

    private final String articleId;
    private String content;

    private final Application application;

    //* UseCases *//
    private final GetCommentListUseCase getCommentListUseCase = new GetCommentListUseCase(
            CommentRepositoryImpl.getInstance()
    );

    private final AddCommentUseCase addCommentUseCase = new AddCommentUseCase(
            CommentRepositoryImpl.getInstance()
    );
    //* UseCases *//

    public CommentListViewModel(@NonNull Application application, @NonNull String articleId) {
        this.application = application;
        this.articleId = articleId;
        update();
    }

    public void update() {
        mutableLiveData.setValue(new State(null, null, true));
        getCommentListUseCase.execute(articleId, status -> {
            mutableLiveData.postValue(fromStatus(status));
        });
    }

    private State fromStatus(Status<List<CommentEntity>> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void addComment() {
        if (content == null || content.trim().isEmpty()) {
            mutableErrorLiveData.postValue("Content cannot be empty");
            return;
        }

        UserSessionManager sessionManager = new UserSessionManager(application);
        UserEntity user = sessionManager.getUser();

        if (user == null || user.getUsername() == null || user.getId() == null) {
            mutableErrorLiveData.postValue("User not logged in");
            return;
        }

        String username = user.getUsername();
        String photoUrl = user.getPhotoUrl();
        String userId = String.valueOf(user.getId());

        addCommentUseCase.execute(
                articleId,
                username,
                photoUrl,
                content,
                userId,
                status -> {
                    if (status.getError() != null) {
                        mutableErrorLiveData.postValue("Failed to add comment: " + status.getError().getLocalizedMessage());
                        return;
                    }
                    update();
                }
        );
    }

    public static class State {

        @Nullable
        private final String errorMessage;

        @Nullable
        private final List<CommentEntity> items;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable List<CommentEntity> items, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.items = items;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public List<CommentEntity> getItems() {
            return items;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
