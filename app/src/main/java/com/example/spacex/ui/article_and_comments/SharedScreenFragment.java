package com.example.spacex.ui.article_and_comments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentArticleScreenBinding;
import com.example.spacex.ui.article_and_comments.article.ArticleFragment;
import com.example.spacex.ui.article_and_comments.comments_list.CommentListFragment;

public class SharedScreenFragment extends Fragment {

    private FragmentArticleScreenBinding binding;
    private static final String KEY_ARTICLE_ID = "articleId";

    public SharedScreenFragment() {
        super(R.layout.fragment_article_screen);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentArticleScreenBinding.bind(view);

        String articleId = getArguments() != null ? getArguments().getString(KEY_ARTICLE_ID) : null;
        if (articleId == null || articleId.isEmpty()) throw new IllegalStateException("Article Id cannot be null or empty");

        ArticleFragment articleFragment = ArticleFragment.newInstance(articleId);
        CommentListFragment commentListFragment = CommentListFragment.newInstance(articleId);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.article_section, articleFragment)
                .commitNow();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.comments_section, commentListFragment)
                .commitNow();
    }

    public static Bundle getBundle(@NonNull String articleId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ARTICLE_ID, articleId);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
