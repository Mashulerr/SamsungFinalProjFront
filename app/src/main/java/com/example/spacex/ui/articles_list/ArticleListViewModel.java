package com.example.spacex.ui.articles_list;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.ArticleRepositoryImpl;
import com.example.spacex.data.repository.FavouritesRepositoryImpl;
import com.example.spacex.data.repository.ReactionRepositoryImpl;
import com.example.spacex.domain.article.GetArticleListUseCase;
import com.example.spacex.domain.entity.ItemArticleEntity;
import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.ReactionType;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.favourites.AddToFavouritesUseCase;
import com.example.spacex.domain.favourites.RemoveFromFavouritesUseCase;
import com.example.spacex.domain.reaction.AddReactionUseCase;
import com.example.spacex.domain.reaction.DeleteReactionUseCase;
import com.example.spacex.domain.reaction.GetReactionByIdUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleListViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final MutableLiveData<Boolean> isFavouriteLiveData = new MutableLiveData<>();
    public LiveData<Boolean> getIsFavouriteLiveData() {
        return isFavouriteLiveData;
    }

    private final MutableLiveData<Map<String, ReactionType>> reactionMapLiveData = new MutableLiveData<>(new HashMap<>());
    public LiveData<Map<String, ReactionType>> getReactionMapLiveData() {
        return reactionMapLiveData;
    }

    private final Map<String, ReactionType> reactionMap = new HashMap<>();

    private String currentArticleId;

    private String userId;

    public ArticleListViewModel(String userId) {
        this.userId = userId;
        update();
    }

    private String getUserId() {
        return userId;
    }

    //* UseCases *//
    private final GetArticleListUseCase getArticleListUseCase = new GetArticleListUseCase(
            ArticleRepositoryImpl.getInstance()
    );

    private final AddToFavouritesUseCase addToFavouritesUseCase = new AddToFavouritesUseCase(
            FavouritesRepositoryImpl.getInstance()
    );

    private final RemoveFromFavouritesUseCase removeFromFavouritesUseCase = new RemoveFromFavouritesUseCase(
            FavouritesRepositoryImpl.getInstance()
    );

    private final AddReactionUseCase addReactionUseCase = new AddReactionUseCase(
            ReactionRepositoryImpl.getInstance()
    );

    private final DeleteReactionUseCase deleteReactionUseCase = new DeleteReactionUseCase(
            ReactionRepositoryImpl.getInstance()
    );

    private final GetReactionByIdUseCase getReactionByIdUseCase = new GetReactionByIdUseCase(
            ReactionRepositoryImpl.getInstance()
    );
    //* UseCases *//
    private State fromStatus(Status<List<ItemArticleEntity>> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public void update() {
        mutableLiveData.setValue(new State(null, null, true));
        getArticleListUseCase.execute(status -> {
            State state = fromStatus(status);
            mutableLiveData.postValue(state);
            if (state.getItems() != null) {
                fetchUserReactions(state.getItems());
            }
        });
    }

    private void fetchUserReactions(List<ItemArticleEntity> articles) {
        String userId = getUserId();
        if (userId == null) return;

        Map<String, ReactionType> newReactionMap = new HashMap<>();

        for (ItemArticleEntity article : articles) {
            getReactionByIdUseCase.execute(userId, article.getId(), status -> {
                ReactionEntity reaction = status.getValue();
                if (reaction != null) {
                    newReactionMap.put(article.getId(), reaction.getType());
                } else {
                    newReactionMap.put(article.getId(), ReactionType.none);
                }
                reactionMapLiveData.postValue(new HashMap<>(newReactionMap));
            });
        }
    }

    public void addToFavourites() {
        Boolean current = isFavouriteLiveData.getValue();
        boolean newValue = current == null || !current;
        isFavouriteLiveData.setValue(newValue);

        if (currentArticleId == null) return;

        if (newValue) {
            addToFavouritesUseCase.execute(currentArticleId, status -> {
                if (status.getError() != null) {
                    isFavouriteLiveData.postValue(false);
                } else {
                    update();
                }
            });
        } else {
            removeFromFavouritesUseCase.execute(currentArticleId, status -> {
                if (status.getError() != null) {
                    isFavouriteLiveData.postValue(true);
                } else {
                    update();
                }
            });
        }
    }

    public void like(String articleId) {
        String userId = getUserId();
        if (userId == null) return;
        addLike(articleId, userId);
    }

    public void dislike(String articleId) {
        String userId = getUserId();
        if (userId == null) return;
        addDislike(articleId, userId);
    }


    private void addLike(String articleId, String userId) {
        getReactionByIdUseCase.execute(userId, articleId, status -> {
            if (status.getError() != null) {
                addReaction(articleId, userId, ReactionType.like);
                return;
            }

            ReactionEntity reaction = status.getValue();
            ReactionType currentReaction = (reaction != null) ? reaction.getType() : ReactionType.none;

            if (currentReaction == ReactionType.like) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        reactionMap.put(articleId, ReactionType.none);
                        reactionMapLiveData.postValue(new HashMap<>(reactionMap));
                        update();
                    }
                });
            } else if (currentReaction == ReactionType.dislike) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        addReaction(articleId, userId, ReactionType.like);
                    }
                });
            } else {
                addReaction(articleId, userId, ReactionType.like);
            }
        });
    }

    private void addDislike(String articleId, String userId) {
        getReactionByIdUseCase.execute(userId, articleId, status -> {
            if (status.getError() != null) {
                addReaction(articleId, userId, ReactionType.dislike);
                return;
            }

            ReactionEntity reaction = status.getValue();
            ReactionType currentReaction = (reaction != null) ? reaction.getType() : ReactionType.none;

            if (currentReaction == ReactionType.dislike) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        reactionMap.put(articleId, ReactionType.none);
                        reactionMapLiveData.postValue(new HashMap<>(reactionMap));
                        update();
                    }
                });
            } else if (currentReaction == ReactionType.like) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        addReaction(articleId, userId, ReactionType.dislike);
                    }
                });
            } else {
                addReaction(articleId, userId, ReactionType.dislike);
            }
        });
    }


    private void addReaction(String articleId, String userId, ReactionType type) {
        addReactionUseCase.execute(articleId, userId, type.name(), status -> {
            if (status.getError() == null) {
                reactionMap.put(articleId, type);
                reactionMapLiveData.postValue(new HashMap<>(reactionMap));
                update();
            }
        });
    }

    public void setCurrentArticle(String articleId, boolean isFavourite) {
        currentArticleId = articleId;
        isFavouriteLiveData.setValue(isFavourite);
    }

    public static class State {

        @Nullable
        private final String errorMessage;

        @Nullable
        private final List<ItemArticleEntity> items;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable List<ItemArticleEntity> items, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.items = items;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public List<ItemArticleEntity> getItems() {
            return items;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
