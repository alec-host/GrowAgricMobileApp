package com.farmerfirst.growagric.ui.message.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.message.chat.db.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Chat chat);
    }
    public interface ChatAdapterListener{
        void onChatClick(String farmer_uuid);
    }
    private List<Chat> data;
    private LayoutInflater layoutInflater;
    private ChatAdapter.OnDeleteButtonListener onDeleteButtonListener;
    private ChatAdapterListener chatAdapterListener;
    protected Context context;
    private String farmer_uuid;

    public ChatAdapter(@NonNull Context context,ChatAdapter.OnDeleteButtonListener listener,ChatAdapterListener chatAdapterListener){
        this.data = new ArrayList<>();
        this.context=context;
        this.onDeleteButtonListener = listener;
        this.chatAdapterListener = chatAdapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_chat,parent,false);
        return new ChatViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(data.get(position));
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Chat> newData){
        if(data!=null){
            ChatDiffCallback chatDiffCallback = new ChatDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(chatDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView tvMessageOut,tvMessageIn;
        ConstraintLayout constraintLayoutOut,constraintLayoutIn;
        LinearLayout llMessageIn,llMessageOut;
        ChatViewHolder(@NonNull View view){
            super(view);
            tvMessageOut = view.findViewById(R.id.tvMessageTextOut);
            tvMessageIn = view.findViewById(R.id.tvMessageTextIn);
            constraintLayoutOut = view.findViewById(R.id.constraintLayoutOut);
            constraintLayoutIn = view.findViewById(R.id.constraintLayoutIn);
            llMessageIn = view.findViewById(R.id.llMessageIn);
            llMessageOut = view.findViewById(R.id.llMessageOut);
        }
        void bind(final Chat chat){
            farmer_uuid = chat.getFarmer_uuid();
            if(chat != null){
                if(chat.getMessage_origin() == 0) {
                    llMessageOut.setVisibility(View.VISIBLE);
                    llMessageIn.setVisibility(View.INVISIBLE);
                    tvMessageOut.setText(chat.getMessage());
                }else if(chat.getMessage_origin() == 1) {
                    llMessageOut.setVisibility(View.INVISIBLE);
                    llMessageIn.setVisibility(View.VISIBLE);
                    tvMessageIn.setText(chat.getMessage());
                }
            }else{
                System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS  EMPTY");
            }
        }
    }

    class ChatDiffCallback extends DiffUtil.Callback{
        private final List<Chat> oldChats,newChats;

        ChatDiffCallback(List<Chat> oldChats, List<Chat> newChats){
            this.oldChats = oldChats;
            this.newChats = newChats;
        }

        @Override
        public int getOldListSize() {
            return oldChats.size();
        }

        @Override
        public int getNewListSize() {
            return newChats.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldChats.get(oldItemPosition).getChat_uuid().equals(newChats.get(newItemPosition).getChat_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldChats.get(oldItemPosition).equals(newChats.get(newItemPosition));
        }
    }
}