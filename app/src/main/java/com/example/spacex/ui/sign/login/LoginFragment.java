package com.example.spacex.ui.sign.login;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.spacex.databinding.FragmentLoginBinding;
import com.example.spacex.ui.utils.OnChangeText;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    private LoginViewModel viewModel;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loadUserData();

        binding.etEmailLogin.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeUsername(s.toString());
            }
        });

        binding.etPasswordLogin.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changePassword(s.toString());
            }
        });

        binding.login.setOnClickListener(v -> viewModel.login());
        binding.tvDontHaveAccount.setOnClickListener(v -> openRegister());

        if (viewModel.isUserLoggedIn()) {
            openEvents();
            return;
        }

        binding.login.setOnClickListener(v -> viewModel.login());

        subscribe(viewModel);
    }

    private void subscribe(LoginViewModel viewModel) {
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show()
        );

        viewModel.openEventsLiveData.observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                openEvents();
            }
        });

        viewModel.openRegisterLiveData.observe(getViewLifecycleOwner(), unused -> openRegister());
    }

    private void openRegister() {
        final View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void openEvents() {
        final View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_fragment_list);
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty()) {
            binding.etEmailLogin.setText(savedUsername);
            binding.etPasswordLogin.setText(savedPassword);
            viewModel.changeUsername(savedUsername);
            viewModel.changePassword(savedPassword);
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}