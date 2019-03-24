package asilapp.sms.com.measure.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.model.BloodPressureMeasurement;
import asilapp.sms.com.measure.model.Measurement;

public class MeasurementRecyclerAdapter extends RecyclerView.Adapter<MeasurementRecyclerAdapter.MeasurementViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private Context mContext;
    private List<BloodPressureMeasurement> mMeasurements = new ArrayList<>();
    public MeasurementRecyclerAdapter(Context context) {
        mContext = context;
    }


    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.measurement_item_layout, parent, false);

        return new MeasurementViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        Measurement measurement = mMeasurements.get(position);
        String typeOfMeasurement = measurement.getTypeOfMeasurement();
        Timestamp timestamp = Timestamp.valueOf(measurement.getTimestamp());
        String date = dateFormat.format(timestamp);

        //set Values
        holder.typeOfMeasurementView.setText(typeOfMeasurement);
        holder.dateView.setText(date);
    }

    @Override
    public int getItemCount() {
        if (mMeasurements == null) {
            return 0;
        }
        return mMeasurements.size();
    }
    public List<BloodPressureMeasurement> getMeasurements() {
        return mMeasurements;
    }
    /**
     * When data changes, this method updates the list of measurements
     * and notifies the adapter to use the new values on it
     */
    public void setMeasurements(List<BloodPressureMeasurement> measurements) {
        mMeasurements = measurements;
        notifyDataSetChanged();
    }

    class MeasurementViewHolder extends RecyclerView.ViewHolder{
        TextView dateView;
        TextView typeOfMeasurementView;
        MeasurementViewHolder(View itemView){
            super(itemView);
            dateView =itemView.findViewById(R.id.measuremantDate);
            typeOfMeasurementView = itemView.findViewById(R.id.typeOfMeasurements);

        }
    }
}
