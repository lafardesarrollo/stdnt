package com.videoteca.student.ui.cursos;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.videoteca.student.data.CursosRepository;

import org.jetbrains.annotations.NotNull;

public class CursosViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @NotNull
    @Override
    public <T extends ViewModel> T create(@NonNull @NotNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CursosViewModel.class)) {
            Log.e("Entra", "Factory Curso");
            return (T) new CursosViewModel(CursosRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
