package com.example.spacex.ui.article_and_comments.comments_list;


import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentCommentsListBinding;
import com.example.spacex.domain.entity.UserEntity;
import com.example.spacex.ui.article_and_comments.comments_list.factory.CommentListViewModelFactory;
import com.example.spacex.ui.service.UserSessionManager;
import com.example.spacex.ui.utils.Utils;
import com.squareup.picasso.Picasso;

public class CommentListFragment extends Fragment {

    private FragmentCommentsListBinding binding;
    private static final String KEY_ARTICLE_ID = "articleId";

    public CommentListFragment() {
        super(R.layout.fragment_comments_list);
    }

    public static CommentListFragment newInstance(@NonNull String articleId) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCommentsListBinding.bind(view);

        String articleId = getArguments() != null ? getArguments().getString(KEY_ARTICLE_ID) : null;
        if (articleId == null) throw new IllegalStateException("Article Id cannot be null");

        CommentListViewModelFactory factory = new CommentListViewModelFactory(requireActivity().getApplication(), articleId);
        CommentListViewModel viewModel = new ViewModelProvider(this, factory).get(CommentListViewModel.class);

        CommentListAdapter adapter = new CommentListAdapter();
        binding.recycler.setAdapter(adapter);

        UserSessionManager sessionManager = new UserSessionManager(requireContext());
        UserEntity user = sessionManager.getUser();
        String photoUrl = user != null ? user.getPhotoUrl() : null;

        if (photoUrl != null && !photoUrl.isEmpty()) {
            Picasso.get().load(photoUrl).into(binding.userAvatar);
        } else {
            binding.userAvatar.setImageResource(R.drawable.ic_default_user_avatar);
        }

        binding.refresh.setOnRefreshListener(viewModel::update);
        subscribe(viewModel, adapter);

        binding.send.setOnClickListener(v -> {
            String comment = binding.editContent.getText().toString().trim();
            if (!comment.isEmpty()) {
                viewModel.changeContent(comment);
                viewModel.addComment();
                binding.editContent.setText("");
            } else {
                Toast.makeText(getContext(), "Comment cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        binding.editContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                return true;
            }
            return false;
        });

        viewModel.update();
    }

    private void subscribe(final CommentListViewModel viewModel, CommentListAdapter adapter) {
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );

        viewModel.stateLiveData.observe(getViewLifecycleOwner(), state -> {
            boolean isSuccess = !state.isLoading()
                    && state.getErrorMessage() == null
                    && state.getItems() != null;

            binding.refresh.setEnabled(!state.isLoading());
            if (!state.isLoading()) binding.refresh.setRefreshing(false);

            binding.recycler.setVisibility(Utils.visibleOrGone(isSuccess));
            binding.constraintComment.setVisibility(Utils.visibleOrGone(isSuccess));
            binding.error.setVisibility(Utils.visibleOrGone(state.getErrorMessage() != null));
            binding.loading.setVisibility(Utils.visibleOrGone(state.isLoading()));

            binding.error.setText(state.getErrorMessage());
            if (isSuccess) {
                adapter.updateData(state.getItems());
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
