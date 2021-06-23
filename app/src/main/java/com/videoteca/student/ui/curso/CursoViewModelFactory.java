package com.videoteca.student.ui.curso;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.student.data.CursoRepository;

import org.jetbrains.annotations.NotNull;

public class CursoViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CursoViewModel.class)) {
            Log.e("Entra", "Factory Curso Activity");
            return (T) new CursoViewModel(CursoRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
