package com.android.proyectorestaurantes.ui.principal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.android.proyectorestaurantes.databinding.FragmentPrincipalBinding;
import com.android.proyectorestaurantes.ui.principal.PrincipalViewModel;

public class PrincipalFragment extends Fragment {

    private FragmentPrincipalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrincipalViewModel principalViewModel =
                new ViewModelProvider(this).get(PrincipalViewModel.class);

        binding = FragmentPrincipalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textPrincipal;
        principalViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
