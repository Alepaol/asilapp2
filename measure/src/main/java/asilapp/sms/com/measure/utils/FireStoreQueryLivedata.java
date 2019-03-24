package asilapp.sms.com.measure.utils;

import android.arch.lifecycle.LiveData;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import asilapp.sms.com.measure.model.Measurement;

public class FireStoreQueryLivedata<T > extends LiveData<List<T>> implements EventListener<QuerySnapshot> {
    private static final String TAG = FireStoreQueryLivedata.class.getSimpleName();

    private final Query query;
    private  final Class<T> type;
    private ListenerRegistration listenerRegistration;
    private final Handler handler = new Handler();
    private boolean listenerRemovePending = false;


    public FireStoreQueryLivedata(Query query,Class<T> type) {
        this.query = query;
        this.type = type;
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
            listenerRegistration = query.addSnapshotListener(this);
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
    public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null){
            Log.e(TAG, "Can't listen to query snapshots: " + documentSnapshots + ":::" + e.getMessage());
            return;
        }

        setValue(documentToList(documentSnapshots));
    }



    @NonNull
    private List<T>documentToList(QuerySnapshot querySnapshot){
        final List<T> objects = new ArrayList<>();
        if(querySnapshot.isEmpty()){
            return objects;
        }
        for(DocumentSnapshot document: querySnapshot.getDocuments()){

            objects.add(document.toObject(type));
        }
        return objects;
    }

}
