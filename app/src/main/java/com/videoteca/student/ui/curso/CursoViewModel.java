package com.videoteca.student.ui.curso;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.videoteca.student.data.CursoRepository;
import com.videoteca.student.data.model.retrofit.Video;

import java.util.List;

public class CursoViewModel extends ViewModel {
    private CursoRepository cursoRepository;
    private MutableLiveData<List<Video>> listVideoData = new MutableLiveData<>();

    public CursoViewModel(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public void getVideos(Activity activity, String authorization) {
        cursoRepository.getVideos(authorization).observe((LifecycleOwner)activity, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                listVideoData.setValue(videos);
            }
        });
    }

    public MutableLiveData<List<Video>> getListVideoData() {
        return listVideoData;
    }
}
