package asilapp.sms.com.measure.ui.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.ui.fragments.AddMeasurementBluetoothFragment;

public  class LeDeviceListAdapter  extends RecyclerView.Adapter<LeDeviceListAdapter.ViewHolder> {
    Context mContext;
    AddMeasurementBluetoothFragment.Callback mCallback;
    List<BluetoothDevice > mDevices =new ArrayList<>();
    public LeDeviceListAdapter(Context context, AddMeasurementBluetoothFragment.Callback callback){
        mCallback=callback;
        mContext=context;
    }
    public void setDevices(List<BluetoothDevice> devices){
        mDevices = devices;
        notifyDataSetChanged();
    }
    public List<BluetoothDevice> getDevices(){
        return mDevices;
    }
    public void clear(){
        mDevices.clear();
    }

    @NonNull
    @Override
    public LeDeviceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    public void addDevice(BluetoothDevice device) {
        if(!mDevices.contains(device)) {
            mDevices.add(device);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull LeDeviceListAdapter.ViewHolder holder, int position) {
        final BluetoothDevice  device =mDevices.get(position);
        holder.mDeviceName.setText(device.getName());
        holder.mDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onFragmentInteractionUri(device);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mDeviceName;

        public ViewHolder(View itemView) {
            super(itemView);
            mDeviceName =(TextView)itemView.findViewById(R.id.deviece_textView);
        }
    }
}
