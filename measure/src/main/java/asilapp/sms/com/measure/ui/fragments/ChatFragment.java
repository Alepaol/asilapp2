package asilapp.sms.com.measure.ui.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ai.api.model.AIResponse;
import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.core.chat.ChatRepository;
import asilapp.sms.com.measure.model.AsilRoom;
import asilapp.sms.com.measure.ui.adapters.MessageAdapter;
import asilapp.sms.com.measure.ui.viewmodel.ChatViewModel;
import asilapp.sms.com.measure.ui.viewmodel.ChatViewModelFactory;
import asilapp.sms.com.measure.utils.ConstantsChatRoom;

public class ChatFragment extends Fragment  {
    public static ChatFragment newInstance(){
        return new ChatFragment();
    }

    private static final String TAG = ChatFragment.class.getSimpleName();
    private RecyclerView mMessageRecyclerView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private Button mSendButton;

    private FirebaseAuth mFirebaseAuth;

    private EditText mMessageEditText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth= FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Initialize references to view
        View mRootView = inflater.inflate(R.layout.fragment_chat_assistant,container,false);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
        mSendButton = (Button) mRootView.findViewById(R.id.sendButton);
        mMessageRecyclerView = (RecyclerView) mRootView.findViewById(R.id.messageRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mMessageRecyclerView.setLayoutManager(layoutManager);
        mMessageRecyclerView.setHasFixedSize(true);
        mMessageAdapter = new MessageAdapter();
        mMessageRecyclerView.setAdapter(mMessageAdapter);


        mMessageEditText = (EditText) mRootView.findViewById(R.id.messageEditText);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        //mSendButton = (Button) findViewById(R.id.sendButton);





        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Enable Send button when there's text to send
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();

                // Clear input box
                mMessageEditText.setText("");
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
        initViewModel();
    }

    private void sendMessage() {
        ChatViewModel  viewModel;
        String message = mMessageEditText.getText().toString();
        String senderUid = mFirebaseAuth.getCurrentUser().getUid();
        String sender = mFirebaseAuth.getCurrentUser().getDisplayName();
        String receiverUid = ConstantsChatRoom.ASILAPP_UID;
        String receiver = ConstantsChatRoom.ASILAPP_ASSISTANT;
        // add token variable
        AsilRoom room = new AsilRoom(sender,
                receiver,
                senderUid,
                receiverUid,
                message,
                System.currentTimeMillis());
        Repository.ChatRepository chatRepository = new ChatRepository(mFirebaseAuth.getUid());
        chatRepository.sendMessage(room);
        //just for testing


    }

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
    }

    private void initViewModel() {
        ChatViewModel viewModel;
        ChatViewModelFactory chatViewModelFactory = new ChatViewModelFactory(mFirebaseAuth.getUid());
        viewModel = ViewModelProviders.of(this,chatViewModelFactory).get(ChatViewModel.class);
        viewModel.getMessages().observe(this, new Observer<List<AsilRoom>>() {
            @Override
            public void onChanged(@Nullable List<AsilRoom> asilRooms) {
                mMessageAdapter.setAdapter(asilRooms);
            }
        });
    }




}
