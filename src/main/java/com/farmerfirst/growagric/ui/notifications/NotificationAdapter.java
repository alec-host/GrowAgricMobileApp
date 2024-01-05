package com.farmerfirst.growagric.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.message.chat.db.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Notification notification);
    }

    public interface NotificationAdapterListener{
        void onNotificationClick();
    }

    private List<Notification> data;
    private LayoutInflater layoutInflater;
    private NotificationAdapter.OnDeleteButtonListener onDeleteButtonListener;
    private NotificationAdapterListener notificationAdapterListener;
    protected Context context;
    private String farmer_uuid;

    public NotificationAdapter(@NonNull Context context, OnDeleteButtonListener listener, NotificationAdapterListener notificationAdapterListener){
        this.data = new ArrayList<>();
        this.context=context;
        this.onDeleteButtonListener = listener;
        this.notificationAdapterListener = notificationAdapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_notification,parent,false);
        return new NotificationViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(data.get(position));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Notification> newData){
        if(data!=null){
            NotificationDiffCallback notificationDiffCallback = new NotificationDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(notificationDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView tvSubject,tvMessage,tvDateTime;
        NotificationViewHolder(@NonNull View view){
            super(view);
            tvSubject = view.findViewById(R.id.tvSubject);
            tvMessage = view.findViewById(R.id.tvMessage);
            tvDateTime = view.findViewById(R.id.tvDateTime);
        }
        void bind(final Notification notification){
            if(notification != null){
                tvSubject.setText(notification.getSubject());
                tvMessage.setText(notification.getMessage());
                tvDateTime.setText(notification.getDate_created());
            }
        }
    }

    class NotificationDiffCallback extends DiffUtil.Callback{
        private final List<Notification> oldNotifications,newNotifications;

        NotificationDiffCallback(List<Notification> oldNotifications, List<Notification> newNotifications){
            this.oldNotifications = oldNotifications;
            this.newNotifications = newNotifications;
        }

        @Override
        public int getOldListSize() {
            return oldNotifications.size();
        }

        @Override
        public int getNewListSize() {
            return newNotifications.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldNotifications.get(oldItemPosition).getNotification_uuid().equals(newNotifications.get(newItemPosition).getNotification_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldNotifications.get(oldItemPosition).equals(newNotifications.get(newItemPosition));
        }
    }
}
