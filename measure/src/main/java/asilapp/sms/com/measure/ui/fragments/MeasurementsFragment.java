package asilapp.sms.com.measure.ui.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.model.BloodPressureMeasurement;
import asilapp.sms.com.measure.ui.adapters.GraphAdapter;
import asilapp.sms.com.measure.ui.viewmodel.MeasurementViewModel;
import asilapp.sms.com.measure.ui.viewmodel.MeasurementViewModelFactory;
import asilapp.sms.com.measure.ui.adapters.MeasurementRecyclerAdapter;

import static android.widget.LinearLayout.VERTICAL;


/**
 * A placeholder fragment containing a simple view.
 */
public class MeasurementsFragment extends Fragment  {
    private final static String TAG=MeasurementsFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    GraphView graph ;
    private MeasurementRecyclerAdapter mAdapter;
    private MeasurementViewModelFactory viewModelFactory;
    private MeasurementViewModel viewModel;
    GraphAdapter mGraphAdapter;
    OnFabAddMeasurementClicked mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnFabAddMeasurementClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public interface OnFabAddMeasurementClicked{
        public void onFabClicked();
    }


    public static MeasurementsFragment newInstance() {
        //Bundle args = new Bundle();

        //fragment.setArguments(args);
        return  new MeasurementsFragment();
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_measurements, container, false);
        graph = (GraphView) rootView.findViewById(R.id.graph);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(200);

        // enable scaling and scrolling
        graph.getViewport().setScrollable(true);
       // graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        //initialization of recycler + adapter
        mRecyclerView = rootView.findViewById(R.id.recyclerMeasurements);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MeasurementRecyclerAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        //graph
        mGraphAdapter = new GraphAdapter();
        FloatingActionButton  fabAddMeasurement = rootView.findViewById(R.id.fab_add_measurement);
        fabAddMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFabClicked();
            }
        });

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        Log.i(TAG,"Fragment ok");
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initGraph();
        initViewModel();
    }

    private void initGraph() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();

    }

    private void initViewModel() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        viewModelFactory= new MeasurementViewModelFactory(firebaseAuth.getUid());
        viewModel=ViewModelProviders.of(this,viewModelFactory).get(MeasurementViewModel.class);


        viewModel.getMeasurements().observe(this, new Observer<List<BloodPressureMeasurement>>() {
            @Override
            public void onChanged(@Nullable List<BloodPressureMeasurement> measurements) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                mAdapter.setMeasurements(measurements);
                mGraphAdapter.setDataPoint(measurements);
                BarGraphSeries<DataPoint> seriesDiastolic =mGraphAdapter.getScalePressureMax();
                BarGraphSeries<DataPoint> seriesSystolic = mGraphAdapter.getScalePressureMin();
                graph.addSeries(seriesDiastolic);
                graph.addSeries(seriesSystolic);
                seriesDiastolic.setDrawValuesOnTop(false);
                seriesDiastolic.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb(0,255,255);
                    }
                });
                seriesSystolic.setDrawValuesOnTop(true);
                seriesSystolic.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb(0,127,255);
                    }
                });
                seriesSystolic.setSpacing(50);
                seriesSystolic.setSpacing(50);
                seriesDiastolic.setValuesOnTopSize(50);
                seriesSystolic.setValuesOnTopSize(50);

                //seriesDiastolic.
            }
        });
    }


}
