package asilapp.sms.com.chatassistant.Core.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import asilapp.sms.com.chatassistant.Utils.Constants;
import asilapp.sms.com.chatassistant.model.AsilRoom;

public class ChatInteractor implements ChatContract.Interactor {

    private final static String  TAG = ChatInteractor.class.getSimpleName();
    private ValueEventListener listener;
    private ChatContract.OnSendMessageListener mOnSendMessageListener;

    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;
    private DatabaseReference roomDatabaseReference;

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener){
        this.mOnSendMessageListener = onSendMessageListener;
    }
    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    @Override
    public void sendMessageToFirebaseUser(Context context,final AsilRoom chat) {
        //Initializaton of room threads
        final String room_type_1 = chat.getSenderUid() + "_"+ chat.getReceiverUid();
        final String room_type_2 = chat.getReceiverUid() + "_"+ chat.getSenderUid();
        // Firebase initialization
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        roomDatabaseReference = firebaseDatabase.getReference().child(Constants.CHAT_ROOM);

         listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    roomDatabaseReference.child(room_type_1).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                } else if (dataSnapshot.hasChild((room_type_2))) {
                    roomDatabaseReference.child(room_type_2).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                } else {
                    roomDatabaseReference.child(room_type_1).child(String.valueOf(chat.getTimestamp())).setValue(chat);
                    getMessageFromFirebaseUser(chat.getSenderUid(),chat.getReceiverUid());
                }
                // Todo Manage notification events
                // sendPushNotificationToReceiver(chat.sender,chat.message,chat.senderUid, new SharedPrefUtil(context).getString(Constants.ARG_FIREBASE_TOKEN),receiverFirebaseToken);
                //
                mOnSendMessageListener.onSendMessageSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnSendMessageListener.onSendMessageFailure("Unable to send message: " + databaseError.getMessage());

            }
        };
        roomDatabaseReference.getRef().addListenerForSingleValueEvent(listener);

    }

    @Override
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        //Initialization of room threads
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;
        // Firebase initialization
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference roomDatabaseReference = firebaseDatabase.getReference().child(Constants.CHAT_ROOM);


        roomDatabaseReference.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageToFirebaseUser: " + room_type_1 + " exists");
                    attachMessage(room_type_1);
                }
                if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageToFirebaseUser: " + room_type_2 + " exists");
                    attachMessage(room_type_2);
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());

            }
        });

    }

    @Override
    public void removeListenerFromFirebaseChild() {
        if(listener!=null) {
            roomDatabaseReference.removeEventListener(listener);
            listener=null;
        }
    }


    private void attachMessage( String roomType) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.CHAT_ROOM)
                .child(roomType).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AsilRoom asilRoom = dataSnapshot.getValue(AsilRoom.class);
                mOnGetMessagesListener.onGetMessagesSuccess(asilRoom);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }
}
