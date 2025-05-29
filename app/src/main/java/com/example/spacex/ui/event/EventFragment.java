package com.example.spacex.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentEventBinding;
import com.example.spacex.domain.entity.FullEventEntity;
import com.example.spacex.ui.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class EventFragment extends Fragment {

    private FragmentEventBinding binding;
    private EventViewModel viewModel;
    private static final String KEY_ID = "id";

    public EventFragment() {
        super(R.layout.fragment_event);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentEventBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(EventViewModel.class);

        subscribe(viewModel);

        String id = getArguments() != null ? getArguments().getString(KEY_ID) : null;
        if (id == null) throw new IllegalStateException("ID cannot be null");
        viewModel.load(id);
    }

    private void subscribe(final EventViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getEvent() != null;

            binding.progressbar.setVisibility(Utils.visibleOrGone(state.isLoading()));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.error.setText(state.getErrorMessage());
            binding.constraintParent.setVisibility(Utils.visibleOrGone(isSuccess));

            if (isSuccess) {
                FullEventEntity entity = state.getEvent();
                binding.tvEventTitle.setText(entity.getTitle());
                binding.tvEventDetails.setText(entity.getEventDetails());
                binding.tvEventData.setText(formatUtcDate(entity.getEventDateUtc()));

                binding.learnMore.setVisibility(Utils.visibleOrGone(
                        entity.getWikipediaLink() != null
                                || entity.getRedditLink() != null
                                || entity.getArticleLink() != null
                ));

                binding.wikipediaLogo.setVisibility(Utils.visibleOrGone(entity.getWikipediaLink() != null));
                binding.articleLogo.setVisibility(Utils.visibleOrGone(entity.getArticleLink() != null));
                binding.redditLogo.setVisibility(Utils.visibleOrGone(entity.getRedditLink() != null));

                binding.wikipedia.setVisibility(Utils.visibleOrGone(entity.getWikipediaLink() != null));
                binding.wikipedia.setClickable(entity.getWikipediaLink() != null);
                binding.wikipedia.setFocusable(entity.getWikipediaLink() != null);

                binding.article.setVisibility(Utils.visibleOrGone(entity.getArticleLink() != null));
                binding.article.setClickable(entity.getArticleLink() != null);
                binding.article.setFocusable(entity.getArticleLink() != null);

                binding.reddit.setVisibility(Utils.visibleOrGone(entity.getRedditLink() != null));
                binding.reddit.setClickable(entity.getRedditLink() != null);
                binding.reddit.setFocusable(entity.getRedditLink() != null);

                binding.wikipedia.setOnClickListener(v -> {
                    if (entity.getWikipediaLink() != null) openLink(entity.getWikipediaLink());
                });

                binding.article.setOnClickListener(v -> {
                    if (entity.getArticleLink() != null) openLink(entity.getArticleLink());
                });

                binding.reddit.setOnClickListener(v -> {
                    if (entity.getRedditLink() != null) openLink(entity.getRedditLink());
                });
            }
        });
    }

    private String formatUtcDate(String utcDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy MMMM dd", Locale.getDefault());
        try {
            Date date = utcFormat.parse(utcDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return "Unknown Date";
        }
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    public static Bundle getBundle(@NonNull String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ID, id);
        return bundle;
    }
}