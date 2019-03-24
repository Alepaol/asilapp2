package asilapp.sms.com.measure.core;

import com.firebase.ui.auth.data.model.User;

import asilapp.sms.com.measure.model.AsilRoom;
import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.utils.FireStoreDocumentLivedata;
import asilapp.sms.com.measure.utils.FireStoreQueryLivedata;

public interface Repository {
    interface MeasurementRepository<T extends Measurement>{
        FireStoreQueryLivedata<T> getMeasurements();
        FireStoreDocumentLivedata<T> getMeasurementById(String id, String type);
        void addMeasurement(Measurement measurement);
        void deleteMeasurement(String measurementId);
        void updateMeasurement(String measurementId,Measurement measurement);

    }

    interface ProfileRepository {
        FireStoreDocumentLivedata<User> getMeasurementById(String id, String type);
        void addProfile(User user);
        void deleteProfile(String userId);
        void updateProfile(String UserId,Measurement measurement);

    }
    interface ChatRepository{
        void sendMessage(AsilRoom chat);
        FireStoreQueryLivedata<AsilRoom> getMessages();

    }
}
