package asilapp.sms.com.measure.ui.adapters;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import asilapp.sms.com.measure.model.BloodPressureMeasurement;



public class GraphAdapter {
    private List<BloodPressureMeasurement> mMeasurements = new ArrayList<>();

    public void setDataPoint(List<BloodPressureMeasurement> measurements){
        mMeasurements = measurements;

    }
    public BarGraphSeries<DataPoint> getScalePressureMax(){
        if(mMeasurements!=null){
            return new BarGraphSeries<>(generateDataMaxPressure());

        }
        return new BarGraphSeries<>();

    }
    public BarGraphSeries<DataPoint> getScalePressureMin(){
        if(mMeasurements!=null){
            return new BarGraphSeries<>(generateDataMinPressure());
        }
        return new BarGraphSeries<>();

    }




    private DataPoint[] generateDataMaxPressure() {
        int count = mMeasurements.size();
        mMeasurements.sort(new Comparator<BloodPressureMeasurement>() {
            @Override
            public int compare(BloodPressureMeasurement bloodPressureMeasurement, BloodPressureMeasurement t1) {
                return Timestamp.valueOf(bloodPressureMeasurement.getTimestamp()).compareTo(Timestamp.valueOf(t1.getTimestamp()));
            }
        });
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            //Date x = Timestamp.valueOf(mMeasurements.get(i).getTimestamp());
            int x =i;
            int y =mMeasurements.get(i).getMaxPressure();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    private DataPoint[] generateDataMinPressure() {
        int count = mMeasurements.size();
        mMeasurements.sort(new Comparator<BloodPressureMeasurement>() {
            @Override
            public int compare(BloodPressureMeasurement bloodPressureMeasurement, BloodPressureMeasurement t1) {
                return Timestamp.valueOf(bloodPressureMeasurement.getTimestamp()).compareTo(Timestamp.valueOf(t1.getTimestamp()));
            }
        });
        DataPoint[] values = new DataPoint[count];
        for (int i=0; i<count; i++) {
            //Date x = Timestamp.valueOf(mMeasurements.get(i).getTimestamp());
            int x= i;
            double y =mMeasurements.get(i).getMinPressure();
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


}
