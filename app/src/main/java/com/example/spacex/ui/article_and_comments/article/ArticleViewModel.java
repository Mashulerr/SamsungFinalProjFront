package com.example.spacex.ui.article_and_comments.article;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.spacex.data.repository.ArticleRepositoryImpl;
import com.example.spacex.data.repository.FavouritesRepositoryImpl;
import com.example.spacex.data.repository.ReactionRepositoryImpl;
import com.example.spacex.domain.article.GetArticleByIdUseCase;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.ReactionEntity;
import com.example.spacex.domain.entity.ReactionType;
import com.example.spacex.domain.entity.Status;
import com.example.spacex.domain.favourites.AddToFavouritesUseCase;
import com.example.spacex.domain.favourites.RemoveFromFavouritesUseCase;
import com.example.spacex.domain.reaction.AddReactionUseCase;
import com.example.spacex.domain.reaction.DeleteReactionUseCase;
import com.example.spacex.domain.reaction.GetReactionByIdUseCase;

public class ArticleViewModel extends ViewModel {

    private final MutableLiveData<State> mutableLiveData = new MutableLiveData<>();
    public final LiveData<State> stateLiveData = mutableLiveData;

    private final MutableLiveData<ReactionType> reactionLiveData = new MutableLiveData<>(ReactionType.none);
    public LiveData<ReactionType> getReactionLiveData() {
        return reactionLiveData;
    }

    private final MutableLiveData<Boolean> isFavouriteLiveData = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsFavouriteLiveData() {
        return isFavouriteLiveData;
    }

    private String currentArticleId;
    private String currentReactionId;

    //* UseCases *//
    private final GetArticleByIdUseCase getArticleByIdUseCase = new GetArticleByIdUseCase(
            ArticleRepositoryImpl.getInstance()
    );

    private final AddReactionUseCase addReactionUseCase = new AddReactionUseCase(
            ReactionRepositoryImpl.getInstance()
    );

    private final DeleteReactionUseCase deleteReactionUseCase = new DeleteReactionUseCase(
            ReactionRepositoryImpl.getInstance()
    );

    private final RemoveFromFavouritesUseCase removeFromFavouritesUseCase = new RemoveFromFavouritesUseCase(
            FavouritesRepositoryImpl.getInstance()
    );

    private final AddToFavouritesUseCase addToFavouritesUseCase = new AddToFavouritesUseCase(
            FavouritesRepositoryImpl.getInstance()
    );

    private final GetReactionByIdUseCase getReactionByIdUseCase = new GetReactionByIdUseCase(
            ReactionRepositoryImpl.getInstance()
    );
    //* UseCases *//

    public void load(@NonNull String articleId, @NonNull String userId) {
        currentArticleId = articleId;
        mutableLiveData.setValue(new State(null, null, true));
        getArticleByIdUseCase.execute(articleId, status -> {
            State newState = fromStatus(status);
            mutableLiveData.postValue(newState);
            if (newState.article != null) {
                isFavouriteLiveData.postValue(newState.article.isFavourite());
                getReactionByIdUseCase.execute(userId, articleId, reactionStatus -> {
                    if (reactionStatus.getError() != null) {
                        currentReactionId = null;
                        reactionLiveData.postValue(ReactionType.none);
                    } else {
                        ReactionEntity reaction = reactionStatus.getValue();
                        if (reaction != null) {
                            currentReactionId = reaction.getId();
                            reactionLiveData.postValue(reaction.getType());
                        } else {
                            currentReactionId = null;
                            reactionLiveData.postValue(ReactionType.none);
                        }
                    }
                });

            } else {
                isFavouriteLiveData.postValue(false);
                currentReactionId = null;
                reactionLiveData.postValue(ReactionType.none);
            }
        });
    }

    private State fromStatus(Status<FullArticleEntity> status) {
        return new State(
                status.getError() != null ? status.getError().getLocalizedMessage() : null,
                status.getValue(),
                false
        );
    }

    public void like(@NonNull String articleId, @NonNull String userId) {
        getReactionByIdUseCase.execute(userId, articleId, status -> {
            if (status.getError() != null) {
                addReaction(articleId, userId, ReactionType.like);
                return;
            }

            ReactionEntity reaction = status.getValue();
            ReactionType currentReaction = (reaction != null) ? reaction.getType() : ReactionType.none;

            if (currentReaction == ReactionType.like) {
                deleteReaction(articleId, userId);
            } else if (currentReaction == ReactionType.dislike) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        currentReactionId = null;
                        addReaction(articleId, userId, ReactionType.like);
                    }
                });
            } else {
                addReaction(articleId, userId, ReactionType.like);
            }
        });
    }

    public void dislike(@NonNull String articleId, @NonNull String userId) {
        getReactionByIdUseCase.execute(userId, articleId, status -> {
            if (status.getError() != null) {
                addReaction(articleId, userId, ReactionType.dislike);
                return;
            }

            ReactionEntity reaction = status.getValue();
            ReactionType currentReaction = (reaction != null) ? reaction.getType() : ReactionType.none;

            if (currentReaction == ReactionType.dislike) {
                deleteReaction(articleId, userId);
            } else if (currentReaction == ReactionType.like) {
                deleteReactionUseCase.execute(reaction.getId(), delStatus -> {
                    if (delStatus.getError() == null) {
                        currentReactionId = null;
                        addReaction(articleId, userId, ReactionType.dislike);
                    }
                });
            } else {
                addReaction(articleId, userId, ReactionType.dislike);
            }
        });
    }

    private void addReaction(String articleId, String userId, ReactionType type) {
        reactionLiveData.postValue(type);
        addReactionUseCase.execute(articleId, userId, type.name(), status -> {
            if (status.getError() == null) {
                reactionLiveData.postValue(type);
                load(articleId, userId);
            }
        });
    }

    private void deleteReaction(String articleId, String userId) {
        if (currentReactionId == null) {
            reactionLiveData.postValue(ReactionType.none);
            return;
        }

        deleteReactionUseCase.execute(currentReactionId, status -> {
            if (status.getError() == null) {
                reactionLiveData.postValue(ReactionType.none);
                currentReactionId = null;
                load(articleId, userId);
            }
        });
    }

    public void addToFavourites() {
        Boolean current = isFavouriteLiveData.getValue();
        boolean newValue = current == null || !current;

        isFavouriteLiveData.setValue(newValue);

        if (newValue) {
            addToFavouritesUseCase.execute(currentArticleId, status -> {
                if (status.getError() != null) {
                    isFavouriteLiveData.postValue(false);
                }
            });
        } else {
            removeFromFavouritesUseCase.execute(currentArticleId, status -> {
                if (status.getError() != null) {
                    isFavouriteLiveData.postValue(true);
                }
            });
        }
    }

    public static class State {
        @Nullable
        private final String errorMessage;

        @Nullable
        private final FullArticleEntity article;

        private final boolean isLoading;

        public State(@Nullable String errorMessage, @Nullable FullArticleEntity article, boolean isLoading) {
            this.errorMessage = errorMessage;
            this.article = article;
            this.isLoading = isLoading;
        }

        @Nullable
        public String getErrorMessage() {
            return errorMessage;
        }

        @Nullable
        public FullArticleEntity getArticle() {
            return article;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }
}
