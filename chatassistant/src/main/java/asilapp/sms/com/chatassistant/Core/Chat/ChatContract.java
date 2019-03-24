package asilapp.sms.com.chatassistant.Core.Chat;
import android.content.Context;

import asilapp.sms.com.chatassistant.model.AsilRoom;

public interface ChatContract {
    interface View {
        void onSendMessageSuccess();
        void onSendMessageFail(String message);
        void onGetMessageSuccess(AsilRoom room);
        void onGetMessageFailure(String message);
    }
    interface Presenter{
        void sendMessage(Context context, AsilRoom chat);
        void getMessage(String senderUid, String receiverUid);
        void removeListener();

    }
    interface Interactor {
        void sendMessageToFirebaseUser(Context context, AsilRoom chat);
        void getMessageFromFirebaseUser(String senderUid, String receiverUid);
        void removeListenerFromFirebaseChild();

    }
    interface OnSendMessageListener {
        void onSendMessageSuccess();

        void onSendMessageFailure(String message);
    }

    interface OnGetMessagesListener {
        void onGetMessagesSuccess(AsilRoom room);

        void onGetMessagesFailure(String message);
    }


}
