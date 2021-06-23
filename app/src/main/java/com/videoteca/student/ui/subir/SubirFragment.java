package com.videoteca.student.ui.subir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.student.R;

public class SubirFragment extends Fragment {

    private SubirViewModel subirViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subirViewModel =
                new ViewModelProvider(this).get(SubirViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subir, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        subirViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}