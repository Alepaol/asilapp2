package asilapp.sms.com.measure.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.sql.Timestamp;

import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.core.measurement.MeasurementRepository;
import asilapp.sms.com.measure.core.Repository;
import asilapp.sms.com.measure.model.BloodPressureMeasurement;
import asilapp.sms.com.measure.model.Measurement;
import asilapp.sms.com.measure.ui.viewmodel.AddMeasurementViewModel;


public class AddMeasurementFragment extends Fragment {
    public final static String TAG =AddMeasurementFragment.class.getSimpleName();

    private AddMeasurementViewModel mViewModel;
    EditText txtDescription;
    int mDisatolic;
    int mSystolic;
    FloatingActionButton fab ;
    OnSaveClicked mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public static AddMeasurementFragment newInstance() {
        return new AddMeasurementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.add_measurement_fragment, container, false);
        txtDescription = (TextInputEditText) rootView.findViewById(R.id.txt_description);
        NumberPicker npDiastolic = rootView.findViewById(R.id.np_diastolic);
        NumberPicker npSystolic = rootView.findViewById(R.id.np_systolic);
        npDiastolic.setMaxValue(200);
        npDiastolic.setMinValue(30);
        npSystolic.setMaxValue(200);
        npSystolic.setMinValue(30);
        npDiastolic.computeScroll();
        npSystolic.computeScroll();
        npDiastolic.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mDisatolic=numberPicker.getValue();
            }
        });
        npSystolic.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mSystolic=numberPicker.getValue();
            }
        });
        fab =(FloatingActionButton)rootView.findViewById(R.id.fab_saveMeasure);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // try {
                    dummyInsert(txtDescription.getText().toString(),mDisatolic,mSystolic);
                    mCallback.onSaveClicked();
               // }catch (Exception e){
                   // Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                //}

            }
        });

        //mViewModel = ViewModelProviders.of(this).get(AddMeasurementViewModel.class);
        // TODO: Use the ViewModel
    }



    private void dummyInsert(String description,int minPressure,int maxPressure) {
        FirebaseAuth firebaseAuth =FirebaseAuth.getInstance();

        Repository.MeasurementRepository measurementRepository = new MeasurementRepository(firebaseAuth.getUid());
        Measurement measurement = new BloodPressureMeasurement();
        measurement.setDescription(description);
        ((BloodPressureMeasurement) measurement).setMaxPressure(maxPressure);
        ((BloodPressureMeasurement) measurement).setMinPressure(minPressure);
        measurement.setTypeOfMeasurement(BloodPressureMeasurement.class.getSimpleName());
        measurement.setTimestamp(String.valueOf(new Timestamp(System.currentTimeMillis())));
        measurementRepository.addMeasurement(measurement);
        Log.i(TAG,"misura inserita");
    }
    public interface OnSaveClicked{
        public void onSaveClicked();

    }


}
