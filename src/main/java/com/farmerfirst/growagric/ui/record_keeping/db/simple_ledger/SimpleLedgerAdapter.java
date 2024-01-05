package com.farmerfirst.growagric.ui.record_keeping.db.simple_ledger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SimpleLedgerAdapter extends RecyclerView.Adapter<SimpleLedgerAdapter.SimpleLedgerViewHolder>{

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(SimpleLedger simpleLedger);
    }
    public interface SimpleLedgerAdapterListener{
        void onSimpleLegerClick(String transaction_uuid);
    }

    private List<SimpleLedger> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private SimpleLedgerAdapterListener simpleLedgerAdapterListener;
    protected Context context;
    private String transaction_uuid;

    public SimpleLedgerAdapter(@NonNull Context context,OnDeleteButtonListener listener,SimpleLedgerAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.simpleLedgerAdapterListener= adapterListener;
        this.layoutInflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public SimpleLedgerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_table,parent,false);

        return new SimpleLedgerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleLedgerViewHolder holder, int position){
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<SimpleLedger> newData){
        if(data!=null){
            SimpleLedgerDiffCallback simpleLedgerDiffCallback = new SimpleLedgerDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(simpleLedgerDiffCallback);
            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class SimpleLedgerViewHolder extends RecyclerView.ViewHolder{
        TextView txtTransactionID,txtRunningBal,txtDescription,txtNotes,txtEntryDate;
        SimpleLedgerViewHolder(@NonNull View view){
            super(view);
            //txtTransactionID = view.findViewById(R.id.txtTransactionID);
            txtRunningBal = view.findViewById(R.id.txtRunningBal);
            txtDescription = view.findViewById(R.id.txtDescription);
            //txtNotes = view.findViewById(R.id.txtNotes);
            txtEntryDate = view.findViewById(R.id.txtEntryDate);
        }

        void bind(final SimpleLedger simpleLedger){
            if(simpleLedger != null){
                //txtTransactionID.setText(simpleLedger);
                txtRunningBal.setText(Utils.formatWithCommas(simpleLedger.getRunning_balance()));
                txtDescription.setText(simpleLedger.getDescription());
                //txtNotes.setText(simpleLedger.getRecord_type().toString());
                txtEntryDate.setText(simpleLedger.getEntry_date());
                System.out.println("XXXXXX "+simpleLedger.getDescription());
            }
        }
    }

    class SimpleLedgerDiffCallback extends DiffUtil.Callback{
        private final List<SimpleLedger> oldSimpleLedger,newSimpleLedger;

        SimpleLedgerDiffCallback(List<SimpleLedger> oldSimpleLedger,List<SimpleLedger> newSimpleLedger){
            this.oldSimpleLedger=oldSimpleLedger;
            this.newSimpleLedger=newSimpleLedger;
        }

        @Override
        public int getOldListSize() {
            return oldSimpleLedger.size();
        }

        @Override
        public int getNewListSize() {
            return newSimpleLedger.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldSimpleLedger.get(oldItemPosition).getTransaction_uuid().equals(newSimpleLedger.get(newItemPosition).getTransaction_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldSimpleLedger.get(oldItemPosition).equals(newSimpleLedger.get(newItemPosition));
        }
    }
}
