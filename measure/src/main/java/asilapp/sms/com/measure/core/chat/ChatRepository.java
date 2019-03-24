package asilapp.sms.com.measure.core.chat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;

import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.model.AsilRoom;
import asilapp.sms.com.measure.utils.ConstantsChatRoom;
import asilapp.sms.com.measure.utils.ConstantsMeasurement;
import asilapp.sms.com.measure.utils.FireStoreQueryLivedata;

public class ChatRepository implements Repository.ChatRepository {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final CollectionReference CHATROOM;
    private Query query;
    private final static String TAG = ChatRepository.class.getSimpleName();
    private static final CollectionReference USER_REF = FirebaseFirestore.getInstance().collection(ConstantsMeasurement.USER);
    public ChatRepository(String uid){

        CHATROOM= USER_REF.document(uid).collection(ConstantsChatRoom.CHAT_ROOM);

    }
    @Override
    public void sendMessage(AsilRoom chat) {
        // Firebase initialization
        FirebaseFirestore.getInstance();
        CHATROOM.add(chat);

    }


    @Override
    public FireStoreQueryLivedata<AsilRoom> getMessages() {
        query= CHATROOM;
        FirebaseFirestore.getInstance();
        return new FireStoreQueryLivedata<AsilRoom>(query, AsilRoom.class);
    }

}
