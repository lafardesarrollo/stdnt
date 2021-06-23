package com.videoteca.student.ui.cursos;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.videoteca.student.data.model.retrofit.RespCursos;
import com.videoteca.student.data.CursosRepository;

public class CursosViewModel extends ViewModel {

    private CursosRepository cursosRepository;
    private MutableLiveData<RespCursos> respCursosMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mText;

    public CursosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public CursosViewModel(CursosRepository cursosRepository){
        this.cursosRepository = cursosRepository;
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void getCursos(Activity activity, String authorization){
        cursosRepository.getCursos(authorization).observe((LifecycleOwner) activity, new Observer<RespCursos>() {
            @Override
            public void onChanged(RespCursos respCursos) {
                respCursosMutableLiveData.setValue(respCursos);
            }
        });
    }

    public LiveData<RespCursos> getCursosData(){
        return respCursosMutableLiveData;
    }
}