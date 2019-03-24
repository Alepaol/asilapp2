package asilapp.sms.com.measure.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class ChatViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private String mId;
    public ChatViewModelFactory(String measurementId) {
        mId=measurementId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new ChatViewModel(mId);
    }



}
