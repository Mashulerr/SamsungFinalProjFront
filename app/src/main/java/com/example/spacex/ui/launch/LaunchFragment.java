package com.example.spacex.ui.launch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentLaunchBinding;
import com.example.spacex.domain.entity.FullLaunchEntity;
import com.example.spacex.ui.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LaunchFragment extends Fragment {

    private FragmentLaunchBinding binding;
    private LaunchViewModel viewModel;
    private static final String KEY_FLIGHT_NUMBER = "flight_number";

    public LaunchFragment() {
        super(R.layout.fragment_launch);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLaunchBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LaunchViewModel.class);
        subscribe(viewModel);

        String flightNumber = getArguments() != null ? getArguments().getString(KEY_FLIGHT_NUMBER) : null;
        if (flightNumber == null) throw new IllegalStateException("Flight number cannot be null");
        viewModel.load(flightNumber);
    }

    private void subscribe(final LaunchViewModel viewModel) {
        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getLaunch() != null;

            binding.progressbar.setVisibility(Utils.visibleOrGone(state.isLoading()));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.error.setText(state.getErrorMessage());
            binding.constraintParent.setVisibility(Utils.visibleOrGone(isSuccess));

            if (isSuccess) {
                FullLaunchEntity entity = state.getLaunch();
                binding.missionName.setText(entity.getMissionName());
                binding.rocketName.setText(entity.getRocketName());
                binding.rocketType.setText(entity.getRocketType());
                binding.status.setText(entity.isSuccess() ? "Success" : "Failed");

                if (entity.getMissionPatch() != null) {
                    Picasso.get().load(entity.getMissionPatch()).into(binding.missionImage);
                }

                if (entity.getDetails() != null){
                    binding.details.setText(entity.getDetails());
                } else {
                    binding.details.setText(R.string.no_information_available);
                }
                binding.dataUtc.setText(formatUtcDate(entity.getLaunchDateUtc()));

                binding.learnMore.setVisibility(Utils.visibleOrGone(
                        entity.getWikipediaLink() != null
                                || entity.getVideoLink() != null
                ));
                binding.youtubeLogo.setVisibility(Utils.visibleOrGone(entity.getVideoLink() != null));
                binding.wikipediaLogo.setVisibility(Utils.visibleOrGone(entity.getWikipediaLink() != null));

                binding.wikipedia.setVisibility(Utils.visibleOrGone(entity.getWikipediaLink() != null));
                binding.wikipedia.setClickable(entity.getWikipediaLink() != null);
                binding.wikipedia.setFocusable(entity.getWikipediaLink() != null);

                binding.youtube.setVisibility(Utils.visibleOrGone(entity.getVideoLink() != null));
                binding.youtube.setClickable(entity.getVideoLink() != null);
                binding.youtube.setFocusable(entity.getVideoLink() != null);

                binding.wikipedia.setOnClickListener(v -> {
                    if (entity.getWikipediaLink() != null) openLink(entity.getWikipediaLink());
                });

                binding.youtube.setOnClickListener(v -> {
                    if (entity.getVideoLink() != null) openLink(entity.getVideoLink());
                });
            }
        });
    }

    private String formatUtcDate(String utcDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault()
        );
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat outputFormat = new SimpleDateFormat(
                "yyyy MMMM dd",
                Locale.getDefault()
        );
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

    public static Bundle getBundle(@NonNull String flightNumber) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FLIGHT_NUMBER, flightNumber);
        return bundle;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}