package asilapp.sms.com.measure.ui.viewmodel;

import android.arch.lifecycle.ViewModel;

import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.core.measurement.MeasurementRepository;
import asilapp.sms.com.measure.model.BloodPressureMeasurement;
import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.utils.FireStoreQueryLivedata;

public class MeasurementViewModel extends ViewModel {
    private final FireStoreQueryLivedata<BloodPressureMeasurement> mMeasurements;
    private final Repository.MeasurementRepository<BloodPressureMeasurement> mRepository;

    public MeasurementViewModel(String uid ){
        mRepository=new MeasurementRepository(uid);
        mMeasurements = mRepository.getMeasurements();
    }

    public FireStoreQueryLivedata<BloodPressureMeasurement> getMeasurements() {
        return mMeasurements;
    }

    public Repository.MeasurementRepository getRepository() {
        return mRepository;
    }





}
