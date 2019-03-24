package asilapp.sms.com.measure.model;

import android.icu.util.Measure;

import java.sql.Timestamp;
import java.util.Date;

import static android.icu.util.MeasureUnit.INCH_HG;

public class BloodPressureMeasurement extends Measurement {
    /**
     * pressure max
    **/
    private int maxPressure;
    /**
     * pressure min
     */
    private int minPressure;
    public BloodPressureMeasurement(){

    }
    public BloodPressureMeasurement(String measurementId, String timestamp, String sensor,String typeOfMeasurement, int maxPressure, int minPressure, String description) {
        super(measurementId, timestamp,description, sensor,typeOfMeasurement);
        this.maxPressure =maxPressure;
        this.minPressure=minPressure;

    }



    public void setMaxPressure(int maxPressure) {
        this.maxPressure = maxPressure;
    }
    public void setMinPressure(int value) {
        this.minPressure =value;
    }



    public int getMaxPressure() {
        return maxPressure;
    }

    public int getMinPressure() {
        return minPressure;
    }

    @Override
    public Measurement decode(Object o) {
        return null;
    }
}
