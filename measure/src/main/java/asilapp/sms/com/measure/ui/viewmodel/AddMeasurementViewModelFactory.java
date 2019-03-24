package asilapp.sms.com.measure.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.ui.viewmodel.AddMeasurementViewModel;

public class AddMeasurementViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    LiveData<Measurement> measurement ;
    public <T extends ViewModel> T create(Class<T> modelClass){
        return (T) new AddMeasurementViewModel();
    }

}
