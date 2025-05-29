package com.example.spacex.ui.sign.register;

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
import com.example.spacex.databinding.FragmentRegisterBinding;
import com.example.spacex.ui.utils.OnChangeText;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel viewModel;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentRegisterBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding.etNameRegister.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeName(s.toString());
            }
        });
        binding.etUsernameRegister.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeUsername(s.toString());
            }
        });
        binding.etEmailRegister.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changeEmail(s.toString());
            }
        });
        binding.etPasswordRegister.addTextChangedListener(new OnChangeText() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                viewModel.changePassword(s.toString());
            }
        });
        binding.register.setOnClickListener(v -> viewModel.register());
        binding.tvDoYouHaveAccount.setOnClickListener(v -> openLogin());

        subscribe(viewModel);
    }

    private void subscribe(RegisterViewModel viewModel) {
        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error ->
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show());

        viewModel.openLoginLiveData.observe(getViewLifecycleOwner(), unused ->
                openLogin());
    }

    private void openLogin() {
        final View view = getView();
        if (view == null) return;
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}

