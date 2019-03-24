package asilapp.sms.com.measure.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.UUID;

import ai.api.AIDataService;
import ai.api.AIServiceContext;
import ai.api.AIServiceContextBuilder;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import asilapp.sms.com.measure.BuildConfig;
import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.core.chat.ChatRepository;
import asilapp.sms.com.measure.model.AsilRoom;
import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.ui.fragments.AddMeasurementFragment;
import asilapp.sms.com.measure.ui.fragments.ChatFragment;
import asilapp.sms.com.measure.ui.fragments.MeasurementsFragment;
import asilapp.sms.com.measure.ui.fragments.ProfileFragment;
import asilapp.sms.com.measure.utils.ConstantsChatRoom;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MeasurementsFragment.OnFabAddMeasurementClicked, AddMeasurementFragment.OnSaveClicked{

    private static final String TAG =MainActivity.class.getSimpleName() ;
    private TextView mTextMessage;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // Android client
    private AIRequest aiRequest;
    private AIDataService aiDataService;
    private AIServiceContext customAIServiceContext;

    private String mUsername;
    private String uuid = UUID.randomUUID().toString();
    private FirebaseUser mFirebaseUser;


    private static final int RC_SIGN_IN = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth=FirebaseAuth.getInstance();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        loadFragment(MeasurementsFragment.newInstance());
        ImageButton signOut = (ImageButton)findViewById(R.id.imageSignout);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getApplicationContext());
            }
        });
        initLogin();
        navigation.setOnNavigationItemSelectedListener(this);
        initChatbot();
    }
    private boolean loadFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,fragment).addToBackStack(null).commit();
            return true;
        }

        return false;

    }
    private void initChatbot() {
        final AIConfiguration config = new AIConfiguration(ConstantsChatRoom.ACCESS_TOKEN,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(config);
        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
        aiRequest = new AIRequest();
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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        mTextMessage = (TextView) findViewById(R.id.message);
        switch (item.getItemId()) {
            case R.id.navigation_profile:
                fragment = ProfileFragment.newInstance();
                //mTextMessage.setText(R.string.title_profile);
                return loadFragment(fragment);
            case R.id.navigation_diary:
                fragment = MeasurementsFragment.newInstance();
               // mTextMessage.setText(R.string.title_diary);
                return loadFragment(fragment);
            case R.id.navigation_chat:
                fragment =  ChatFragment.newInstance();
                //mTextMessage.setText(R.string.title_notifications);
                return loadFragment(fragment);
        }
        return loadFragment(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity.this,"Signed in!",Toast.LENGTH_SHORT).show();
            }else if(resultCode==RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Signed in canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    private void initLogin(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if(mFirebaseUser !=null){
                    // usere signed in
                    onSignedInInitialize(mFirebaseUser.getDisplayName());
                }else{
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
    }

    private void onSignedInInitialize(String displayName) {
    }





    @Override
    public void onFabClicked() {
        AddMeasurementFragment measurementsFragment = AddMeasurementFragment.newInstance();
       getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,measurementsFragment).commit();
    }

    @Override
    public void onSaveClicked() {
        MeasurementsFragment measurementsFragment = MeasurementsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,measurementsFragment).commit();

    }

    public void callback(AIResponse aiResponse) {
        if (aiResponse != null) {
            // process aiResponse here
            String botReply = aiResponse.getResult().getFulfillment().getSpeech();
            Log.d(TAG, "Bot Reply: " + botReply);
            Repository.ChatRepository chatRepository= new ChatRepository(mFirebaseAuth.getUid());
            chatRepository.sendMessage(new AsilRoom(mFirebaseAuth.getCurrentUser().getDisplayName(),
                    "bot",
                    "",
                    mFirebaseAuth.getUid(),
                    botReply,
                    System.currentTimeMillis()));
        } else {
            Log.d(TAG, "Bot Reply: Null");

        }
    }


}
