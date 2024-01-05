package com.farmerfirst.growagric.ui.finance.recyclerview.db;

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

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder> {

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Finance finance);
    }
    public interface FinanceAdapterListener{
        void onFinanceClick(String application_uuid);
    }

    private List<Finance> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private FinanceAdapterListener financeAdapterListener;
    protected Context context;
    private String application_uuid;

    public FinanceAdapter(@NonNull Context context,OnDeleteButtonListener listener,FinanceAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.financeAdapterListener = adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public FinanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_finance,parent,false);
        return new FinanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FinanceViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Finance> newData){
        if(data!=null){
            FinanceDiffCallback financeDiffCallback = new FinanceDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(financeDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class FinanceViewHolder extends RecyclerView.ViewHolder{
        TextView tvApplicationUUID,tvFinanceAmount,tvChickCost,tvFeedsCost,tvBroodingCost,tvMedicineVaccineCost,tvDateRequired,tvApplicationstatus;
        FinanceViewHolder(@NonNull View view){
            super(view);
            tvApplicationUUID = view.findViewById(R.id.textApplicationUUID);
            tvFinanceAmount = view.findViewById(R.id.textFinanceAmount);
            tvChickCost = view.findViewById(R.id.textChickCost);
            tvFeedsCost = view.findViewById(R.id.textFeedsCost);
            tvBroodingCost = view.findViewById(R.id.textBroodingCost);
            tvMedicineVaccineCost = view.findViewById(R.id.textMedicineVaccineCost);
            tvDateRequired = view.findViewById(R.id.textDateRequired);
            tvApplicationstatus = view.findViewById(R.id.textApplicationStatus);
        }
        void bind(final Finance finance){
            if (finance != null) {
                application_uuid = finance.getApplication_uuid();
                tvApplicationUUID.setText(":"+application_uuid);
                tvFinanceAmount.setText(Utils.formatWithCommas(finance.getLoan_amount()));
                tvChickCost.setText(Utils.formatWithCommas(finance.getChick_cost()));
                tvFeedsCost.setText(Utils.formatWithCommas(finance.getFeed_cost()));
                tvBroodingCost.setText(Utils.formatWithCommas(finance.getBrooding_cost()));
                tvMedicineVaccineCost.setText(Utils.formatWithCommas(finance.getVaccine_medicine_cost()));
                if(!finance.getDate_required().equalsIgnoreCase("") || !finance.getDate_required().equalsIgnoreCase(null)) {
                    tvDateRequired.setText(finance.getDate_required().split("T")[0].toString());
                }
                tvApplicationstatus.setText(finance.getApplication_status());
            }
        }
    }

    class FinanceDiffCallback extends DiffUtil.Callback{

        private final List<Finance> oldFinances, newFinances;

        FinanceDiffCallback(List<Finance> oldFinances, List<Finance> newFinances){
            this.oldFinances = oldFinances;
            this.newFinances = newFinances;
        }

        @Override
        public int getOldListSize() {
            return oldFinances.size();
        }

        @Override
        public int getNewListSize() {
            return newFinances.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldFinances.get(oldItemPosition).getApplication_uuid().equals(newFinances.get(newItemPosition).getApplication_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldFinances.get(oldItemPosition).equals(newFinances.get(newItemPosition));
        }
    }
    public String searchFinanceFilter(){
        return application_uuid;
    }
}