package asilapp.sms.com.measure.model;

import android.media.Image;

import java.sql.Timestamp;
import java.util.Date;

public class ECGMeasurement extends Measurement {
    private Image graph;
    public ECGMeasurement(String measureId, String timestamp, String sensor, String description, String typeOfMeasurement) {
        super(measureId, timestamp, sensor,description, typeOfMeasurement);
        this.graph=graph;
    }

    @Override
    public Measurement decode(Object o) {
        return null;
    }
}
