package asilapp.sms.com.measure.utils;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;


public class FireStoreDocumentLivedata<T> extends LiveData<T> implements EventListener<DocumentSnapshot> {
    private static final String TAG = FireStoreQueryLivedata.class.getSimpleName();
    private  final Class<T> type;
    private ListenerRegistration listenerRegistration;
    private final Handler handler = new Handler();
    private boolean listenerRemovePending = false;
    private final DocumentReference mDocumentReference;

    public FireStoreDocumentLivedata(DocumentReference documentReference,Class<T> type) {
        this.type = type;
        this.mDocumentReference = documentReference;
    }

    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            listenerRegistration.remove();
            listenerRemovePending = false;
        }
    };
    @Override
    protected void onActive() {
        super.onActive();

        Log.d(TAG, "onActive");
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            listenerRegistration = mDocumentReference.addSnapshotListener(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        Log.d(TAG, "onInactive: ");
        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (e != null){
            Log.e(TAG, "Can't listen to query snapshots: " + documentSnapshot + ":::" + e.getMessage());
            return;
        }

        setValue(documentSnapshot.toObject(type));
    }
}
