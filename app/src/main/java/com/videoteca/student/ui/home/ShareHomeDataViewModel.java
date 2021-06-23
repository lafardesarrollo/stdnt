package com.videoteca.student.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.videoteca.student.data.model.realm.SessionData;

public class ShareHomeDataViewModel  extends ViewModel {
    private final MutableLiveData<SessionData> sessionDataMutableLiveData = new MutableLiveData<>();

    public void sendSessionData(SessionData sessionData){
        sessionDataMutableLiveData.setValue(sessionData);
    }
    public MutableLiveData<SessionData> getSessionDataMutableLiveData() {
        return sessionDataMutableLiveData;
    }
}
