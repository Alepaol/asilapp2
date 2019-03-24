package asilapp.sms.com.measure.utils;

import android.arch.lifecycle.ViewModel;

import com.google.firebase.firestore.CollectionReference;

import asilapp.sms.com.measure.model.Measurement;

class AddMeasurementViewModel extends ViewModel {
    FireStoreDocumentLivedata <Measurement> measurements;
    public AddMeasurementViewModel(String mId, CollectionReference mCollectionReference) {
        //measurements =mCollectionReference.document(mId).;

    }

    public FireStoreDocumentLivedata<Measurement> getMeasurements() {
        return measurements;
    }
}
