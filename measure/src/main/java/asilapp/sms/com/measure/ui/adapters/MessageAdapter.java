package asilapp.sms.com.measure.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import asilapp.sms.com.measure.R;
import asilapp.sms.com.measure.model.AsilRoom;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<AsilRoom> mDataSet = new ArrayList<>();

    public void setAdapter(List<AsilRoom> messages){
        mDataSet = messages;
    }
    private void sort(List<AsilRoom> mDataSet){
        Collections.sort(mDataSet, new Comparator<AsilRoom>() {
            @Override
            public int compare(AsilRoom asilRoom, AsilRoom t1) {
                return asilRoom.compareTo(t1);
            }
        });



    }
    public void add(AsilRoom chat) {
        mDataSet.add(chat);
        notifyItemInserted(mDataSet.size() - 1);
        sort(mDataSet);
    }
    public void clear(){
        mDataSet.clear();
    }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());


        return  new MessageViewHolder(layoutInflater.inflate(R.layout.item_message,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

            AsilRoom asilRoom = mDataSet.get(position);
            holder.messageTextView.setText(asilRoom.getMessage());
            holder.authorTextView.setText(asilRoom.getSender());
    }



    @Override
    public int getItemCount() {
        if (mDataSet==null){
            return 0;
        }
        return mDataSet.size();

    }
     class MessageViewHolder extends RecyclerView.ViewHolder{
        //ImageView photoImageView;
        TextView messageTextView;
        TextView authorTextView;
         MessageViewHolder(View itemView) {
            super(itemView);
            //photoImageView=itemView.findViewById(R.id.photoImageView);
            messageTextView=(TextView)itemView.findViewById(R.id.messageTextView);
            authorTextView= (TextView)itemView.findViewById(R.id.nameTextView);
        }

    }
}
