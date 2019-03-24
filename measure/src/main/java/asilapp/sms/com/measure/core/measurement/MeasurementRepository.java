package asilapp.sms.com.measure.core.measurement;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;

import javax.annotation.Nonnull;

import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.model.BloodPressureMeasurement;
import asilapp.sms.com.measure.model.ECGMeasurement;
import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.utils.ConstantsMeasurement;
import asilapp.sms.com.measure.utils.FireStoreDocumentLivedata;
import asilapp.sms.com.measure.utils.FireStoreQueryLivedata;


public class MeasurementRepository implements Repository.MeasurementRepository<BloodPressureMeasurement>{
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference MEASUREMENT;
    private Query query;
    private final static String TAG = MeasurementRepository.class.getSimpleName();
    private static final CollectionReference USER_REF = FirebaseFirestore.getInstance().collection(ConstantsMeasurement.USER);
    public MeasurementRepository(String uid){

        MEASUREMENT= USER_REF.document(uid).collection(ConstantsMeasurement.USER_MEASUREMENTS);

    }
    private Class[] measurementTypes = {
        BloodPressureMeasurement.class,
        ECGMeasurement.class
    };
    public FireStoreQueryLivedata<BloodPressureMeasurement> getMeasurements(){
        FirebaseFirestore.getInstance();
        query= MEASUREMENT;
        return  new FireStoreQueryLivedata<BloodPressureMeasurement>(query,BloodPressureMeasurement.class);
    }

    private void initFirestoreTimestampSettings() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    public FireStoreDocumentLivedata<BloodPressureMeasurement> getMeasurementById(String id,String type){
        FirebaseFirestore.getInstance();
        return new FireStoreDocumentLivedata<BloodPressureMeasurement>( MEASUREMENT.document(id),BloodPressureMeasurement.class);
    }
    public void addMeasurement(Measurement measurement){
        FirebaseFirestore.getInstance();
         MEASUREMENT.add(measurement).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
             @Override
             public void onSuccess(DocumentReference documentReference) {
                 Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                 documentReference.update(ConstantsMeasurement.MEASUREMENT_ID,documentReference.getId());
             }
         }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@Nonnull Exception e) {
                         Log.w(TAG, "Error adding document", e);
                     }
                 });
    }
    public void deleteMeasurement(String measurementId){
        FirebaseFirestore.getInstance();
        MEASUREMENT.document(measurementId).delete();

    }
    public void updateMeasurement(String measurementId,Measurement measurement){
        FirebaseFirestore.getInstance();
        DocumentReference doc = MEASUREMENT.document(measurementId);
        doc.update(ConstantsMeasurement.MEASUREMENT_DESCRIPTION, measurement.getDescription());
        doc.update(ConstantsMeasurement.MEASUREMENT_TIMESTAMP,measurement.getTimestamp());
        doc.update(ConstantsMeasurement.MEASUREMENT_SENSOR,measurement.getSensor());
        if(measurement instanceof BloodPressureMeasurement){
            doc.update(ConstantsMeasurement.MEASUREMENT_PRESSSURE_MIN,
                    ((BloodPressureMeasurement) measurement).getMinPressure());
            doc.update(ConstantsMeasurement.MEASUREMENT_PRESSURE_MAX,
                    ((BloodPressureMeasurement) measurement).getMaxPressure());
        }else if(measurement instanceof ECGMeasurement){

        }

    }


}
