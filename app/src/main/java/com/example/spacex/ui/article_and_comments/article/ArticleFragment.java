package com.example.spacex.ui.article_and_comments.article;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentArticleBinding;
import com.example.spacex.domain.entity.FullArticleEntity;
import com.example.spacex.domain.entity.ReactionType;
import com.example.spacex.ui.utils.Utils;
import com.squareup.picasso.Picasso;

public class ArticleFragment extends Fragment {

    private FragmentArticleBinding binding;
    private ArticleViewModel viewModel;
    private static final String KEY_ARTICLE_ID = "articleId";

    public ArticleFragment() {
        super(R.layout.fragment_article);
    }

    public static ArticleFragment newInstance(@NonNull String articleId) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    private String getUserId() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return prefs.getString("userId", null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentArticleBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(ArticleViewModel.class);
        String articleId = getArguments() != null ? getArguments().getString(KEY_ARTICLE_ID) : null;
        if (articleId == null || articleId.isEmpty()) {
            throw new IllegalStateException("Article Id cannot be null or empty");
        }


        viewModel.load(articleId, getUserId());
        subscribe(viewModel);

        binding.likeButton.setOnClickListener(v -> {
            viewModel.like(articleId, getUserId());
        });

        binding.dislikeButton.setOnClickListener(v -> {
            viewModel.dislike(articleId, getUserId());
        });

        viewModel.getReactionLiveData().observe(getViewLifecycleOwner(), this::updateReactionButtons);

        binding.favouritesButton.setOnClickListener(v -> viewModel.addToFavourites());
        viewModel.getIsFavouriteLiveData().observe(getViewLifecycleOwner(), isFavourite -> {
            if (isFavourite != null && isFavourite) {
                binding.favouritesButton.setImageResource(R.drawable.ic_favourites_yellow);
            } else {
                binding.favouritesButton.setImageResource(R.drawable.ic_favourites_transparent);
            }
        });

    }

    private void updateReactionButtons(ReactionType reaction) {
        if (reaction == ReactionType.like) {
            binding.likeButton.setImageResource(R.drawable.ic_like_gray);
            binding.dislikeButton.setImageResource(R.drawable.ic_dislike_transparent);
        } else if (reaction == ReactionType.dislike) {
            binding.likeButton.setImageResource(R.drawable.ic_like_transparent);
            binding.dislikeButton.setImageResource(R.drawable.ic_dislike_gray);
        } else {
            binding.likeButton.setImageResource(R.drawable.ic_like_transparent);
            binding.dislikeButton.setImageResource(R.drawable.ic_dislike_transparent);
        }
    }


    private void subscribe(final ArticleViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getArticle() != null;
            binding.progressbar.setVisibility(Utils.visibleOrGone(state.isLoading()));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.error.setText(state.getErrorMessage());
            binding.constraintParent.setVisibility(Utils.visibleOrGone(isSuccess));


            if (isSuccess) {
                FullArticleEntity article = state.getArticle();
                binding.articleTitle.setText(article.getTitle());
                binding.userNickname.setText(article.getUsername());
                binding.articleContent.setText(article.getContent());
                binding.amountOfDislikes.setText(article.getDislikes().toString());
                binding.amountOfLikes.setText(article.getLikes().toString());
                if (article.getPhotoUrl() != null) {
                    Picasso.get().load(article.getPhotoUrl()).into(binding.userImage);
                } else {
                    binding.userImage.setImageResource(R.drawable.ic_default_user_avatar);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
