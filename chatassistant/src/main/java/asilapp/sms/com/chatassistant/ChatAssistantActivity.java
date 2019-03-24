package asilapp.sms.com.chatassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import asilapp.sms.com.chatassistant.Core.Chat.ChatContract;
import asilapp.sms.com.chatassistant.Core.Chat.ChatPresenter;
import asilapp.sms.com.chatassistant.Utils.Constants;

import asilapp.sms.com.chatassistant.model.AsilRoom;

import ai.api.android.AIDataService;
import ai.api.model.AIRequest;

import static asilapp.sms.com.chatassistant.Utils.Constants.ANONYMOUS;
import static asilapp.sms.com.chatassistant.Utils.Constants.DEFAULT_MSG_LENGTH_LIMIT;



public class ChatAssistantActivity extends AppCompatActivity implements ChatContract.View, TextView.OnEditorActionListener {


    private static final String TAG = ChatAssistantActivity.class.getSimpleName();
    private static final int RC_PHOTO_PICKER = 2;
    private static final int RC_SIGN_IN = 1;



    private RecyclerView mMessageRecyclerView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
   // private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    //firebase
   // private FirebaseStorage mFirebaseStorage;
   // private StorageReference mImagesStorageReference;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    private String mUsername;
    private AIDataService aiDataService;
    private AIRequest aiRequest;
    private UUID uuid;
    private FirebaseUser mFirebaseUser;

    private ChatPresenter mChatPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_assistant);
        //initialize Firebase Components

        //mFirebaseStorage= FirebaseStorage.getInstance();
        mFirebaseAuth=FirebaseAuth.getInstance();

        mChatPresenter = new ChatPresenter(this);


         mUsername=ANONYMOUS;




        // Initialize references to view
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMessageRecyclerView.setLayoutManager(layoutManager);
        mMessageRecyclerView.setHasFixedSize(true);


        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        //mSendButton = (Button) findViewById(R.id.sendButton);



        // Enable Send button when there's text to send
        mMessageEditText.setOnEditorActionListener(this);
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});
        //get messages list
       // mChatPresenter.getMessage(mFirebaseAuth.getCurrentUser().getUid(),Constants.ASILAPP_UID);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Send button sends a message and clears the EditText

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser !=null){
                    // usere signed in
                    onSignedInInitialize(mFirebaseUser.getDisplayName());
                }else{
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
       // initChatbot();

    }

    private void sendMessage() {
        String message = mMessageEditText.getText().toString();
        String senderUid = mFirebaseAuth.getCurrentUser().getUid();
        String sender = mFirebaseAuth.getCurrentUser().getDisplayName();
        String receiverUid = Constants.ASILAPP_UID;
        String receiver = Constants.ASILAPP_ASSISTANT;
        // add token variable
        AsilRoom room = new AsilRoom(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis());

        mChatPresenter.sendMessage(getApplicationContext(),room);
        //just for testing
        mChatPresenter.sendMessage(getApplicationContext(),new AsilRoom(receiver,
                sender,
                receiverUid,
                senderUid,
                "risposta",
                System.currentTimeMillis()+100));
    }

    private void onSignedOutCleanup() {
    detachDatabaseListener();
        if( mMessageAdapter != null) {
            mMessageAdapter.clear();
        }
    }

    private void detachDatabaseListener() {

       mChatPresenter.removeListener();
    }

    private void onSignedInInitialize(String username) {
        mUsername= username;
        mChatPresenter.getMessage(mFirebaseAuth.getCurrentUser().getUid(),Constants.ASILAPP_UID);
    }

    @Override
    public void onSendMessageSuccess() {
        // Clear input box
        mMessageEditText.setText("");
        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onGetMessageSuccess(AsilRoom room) {
        if (mMessageAdapter == null) {
            mMessageAdapter = new MessageAdapter(new ArrayList<AsilRoom>());
            mMessageRecyclerView.setAdapter(mMessageAdapter);
        }
        mMessageAdapter.add(room);
        mMessageRecyclerView.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);

    }

    @Override
    public void onGetMessageFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener!= null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseListener();
        if( mMessageAdapter != null) {
             mMessageAdapter.clear();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(ChatAssistantActivity.this,"Signed in!",Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED) {
                Toast.makeText(ChatAssistantActivity.this, "Signed in canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }
}
