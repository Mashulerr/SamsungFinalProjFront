package com.example.spacex.ui.create_article;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.spacex.R;
import com.example.spacex.databinding.FragmentCreateArticleBinding;
import com.example.spacex.ui.utils.OnChangeText;

public class CreateArticleFragment extends Fragment {

    private FragmentCreateArticleBinding binding;

    private CreateArticleViewModel viewModel;

    public CreateArticleFragment() {
        super(R.layout.fragment_create_article);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentCreateArticleBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(CreateArticleViewModel.class);

        binding.etArticleTitle.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeTitle(s.toString());
            }
        });

        binding.etArticleContent.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeContent(s.toString());
            }
        });
        binding.create.setOnClickListener(v -> viewModel.create());
        subscribe(viewModel);
    }

    private void subscribe(CreateArticleViewModel viewModel) {
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );
        viewModel.openArticlesListLiveData.observe(getViewLifecycleOwner(), unused -> openArticlesList());
    }

    private void openArticlesList() {
        final View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_createArticleFragment_to_articlesListFragment);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
