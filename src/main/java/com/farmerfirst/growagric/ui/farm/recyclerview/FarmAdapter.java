package com.farmerfirst.growagric.ui.farm.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.farm.recyclerview.db.Farm;

import java.util.ArrayList;
import java.util.List;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {
    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Farm farm);
    }
    public interface FarmAdapterListener{
        void onFarmClick(String farm_uuid);
    }

    private List<Farm> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private FarmAdapterListener farmAdapterListener;
    protected Context context;
    private String farm_uuid;

    public FarmAdapter(@NonNull Context context,OnDeleteButtonListener listener,FarmAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context=context;
        this.onDeleteButtonListener = listener;
        this.farmAdapterListener = adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_farm,parent,false);
        return new FarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Farm> newData){
        if(data!=null){
            FarmDiffCallback farmDiffCallback = new FarmDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(farmDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class FarmViewHolder extends RecyclerView.ViewHolder{
        TextView tvFarmedItem,tvYearFarmingSelectedItem,tvHouseCapacity,tvMortalityRate,tvInsurance,tvNumberOfEmployees,tvFarmedItemLabel,tvLocation,tvMortalityRateLabel;
        FarmViewHolder(@NonNull View view){
            super(view);
            tvLocation = view.findViewById(R.id.textLocation);
            tvFarmedItem = view.findViewById(R.id.textFarmedItem);
            tvFarmedItemLabel = view.findViewById(R.id.textFarmedItemLabel);
            tvYearFarmingSelectedItem = view.findViewById(R.id.textYearFarmingItem);
            tvHouseCapacity = view.findViewById(R.id.textChickHouseCapacity);
            tvMortalityRate = view.findViewById(R.id.textMortalityRate);
            tvMortalityRateLabel = view.findViewById(R.id.textMortalityRateLabel);
            tvInsurance = view.findViewById(R.id.textInsurance);
            tvNumberOfEmployees = view.findViewById(R.id.textNumberOfEmployees);
        }
        void bind(final Farm farm){
            if (farm != null) {
                farm_uuid = farm.getFarm_uuid();
                tvLocation.setText("Farm: "+farm.getCounty()+", "+farm.getSub_county());
                tvFarmedItem.setText(farm.getItem_farmed());
                tvFarmedItemLabel.setText(tvFarmedItemLabel.getText().toString().replace("{0}",farm.getItem_farmed()));
                tvYearFarmingSelectedItem.setText(farm.getNum_of_years());
                tvHouseCapacity.setText(farm.getHouse_capacity());
                if(farm.getMortality_rate().equalsIgnoreCase("0")){
                    tvMortalityRate.setVisibility(View.GONE);
                    tvMortalityRateLabel.setVisibility(View.GONE);
                }else{
                    tvMortalityRate.setVisibility(View.VISIBLE);
                    tvMortalityRateLabel.setVisibility(View.VISIBLE);
                }
                tvMortalityRate.setText(farm.getMortality_rate());
                if(farm.getIs_insured().equalsIgnoreCase("0")){
                    tvInsurance.setText("None");
                }else{
                    tvInsurance.setText(farm.getIs_insured());
                }
                tvNumberOfEmployees.setText(farm.getNum_of_employees());
            }
        }
    }

    class FarmDiffCallback extends DiffUtil.Callback{

        private final List<Farm> oldFarms, newFarms;

        FarmDiffCallback(List<Farm> oldFarms, List<Farm> newFarms){
            this.oldFarms = oldFarms;
            this.newFarms = newFarms;
        }

        @Override
        public int getOldListSize() {
            return oldFarms.size();
        }

        @Override
        public int getNewListSize() {
            return newFarms.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldFarms.get(oldItemPosition).getFarm_uuid().equals(newFarms.get(newItemPosition).getFarm_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldFarms.get(oldItemPosition).equals(newFarms.get(newItemPosition));
        }
    }
    public String searchFarmFilter(){
        return farm_uuid;
    }
}