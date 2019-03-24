package asilapp.sms.com.chatassistant.Core.Chat;


import android.content.Context;

import asilapp.sms.com.chatassistant.model.AsilRoom;

public class ChatPresenter implements ChatContract.Presenter,ChatContract.OnGetMessagesListener,ChatContract.OnSendMessageListener {
    private ChatContract.View mView;
    private ChatInteractor mChatInteractor;
    public ChatPresenter(ChatContract.View view) {
        this.mView = view;
        mChatInteractor = new ChatInteractor(this,this);
    }

    @Override
    public void sendMessage(Context context, AsilRoom chat) {
        mChatInteractor.sendMessageToFirebaseUser(context,chat);

    }

    @Override
    public void getMessage(String senderUid, String receiverUid) {
        mChatInteractor.getMessageFromFirebaseUser(senderUid,receiverUid);
    }

    @Override
    public void removeListener() {
        mChatInteractor.removeListenerFromFirebaseChild();
    }


    @Override
    public void onGetMessagesSuccess(AsilRoom message) {
        mView.onGetMessageSuccess(message);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        mView.onGetMessageFailure(message);
    }

    @Override
    public void onSendMessageSuccess() {
        mView.onSendMessageSuccess();

    }

    @Override
    public void onSendMessageFailure(String message) {
        mView.onSendMessageFail(message);

    }
}
