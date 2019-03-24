package asilapp.sms.com.measure.model;

import java.sql.Timestamp;
import java.util.Date;

/**
 * this class represents the data Measurement, he is used for identify and model a  measurement
 */

public abstract  class Measurement {
    /**
     * Unique id of measurement
     */
    private  String measurementId ;
    /**
     * description of measurement
     */
    private String description;

    /**
     * date of measurement
     */
    private String timestamp;
    /**
     * Sensor that detect this measurement
     */
    private String sensor;

    /**
     * Unique user id
     */
    private String uid;

    /**
     * Type of Measurement
     */
    private String typeOfMeasurement;




    public Measurement(){

    }

    public Measurement(String measureId, String timestamp, String sensor, String description,String typeOfMeasurement) {
        this.description=description;
        this.measurementId = measureId;
        this.timestamp=timestamp;
        this.sensor=sensor;
        this.typeOfMeasurement=typeOfMeasurement;

    }


    public abstract Measurement decode(Object o);
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTypeOfMeasurement(String typeOfMeasurement) {
        this.typeOfMeasurement = typeOfMeasurement;
    }

    public String getTypeOfMeasurement() {
        return typeOfMeasurement;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public void setMeasureId(String measureId) {
        this.measurementId = measureId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getMeasureId() {
        return measurementId;
    }

    public String getDescription() {
        return description;
    }

    public String getSensor() {
        return sensor;
    }







}
