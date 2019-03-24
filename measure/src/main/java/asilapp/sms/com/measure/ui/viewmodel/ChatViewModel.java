package asilapp.sms.com.measure.ui.viewmodel;

import android.arch.lifecycle.ViewModel;

import asilapp.sms.com.measure.core.Repository;

import asilapp.sms.com.measure.core.chat.ChatRepository;
import asilapp.sms.com.measure.model.AsilRoom;
import asilapp.sms.com.measure.utils.FireStoreQueryLivedata;

public class ChatViewModel extends ViewModel {
    private final FireStoreQueryLivedata<AsilRoom> mMeasssages;
    private final Repository.ChatRepository mRepository;

    public ChatViewModel(String uid ){
        mRepository=new ChatRepository(uid);
        mMeasssages = mRepository.getMessages();
    }

    public FireStoreQueryLivedata<AsilRoom> getMessages() {
        return mMeasssages;
    }

    public Repository.ChatRepository getRepository() {
        return mRepository;
    }





}
