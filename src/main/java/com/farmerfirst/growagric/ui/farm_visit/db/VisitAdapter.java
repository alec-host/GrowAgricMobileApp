package com.farmerfirst.growagric.ui.farm_visit.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;

import java.util.ArrayList;
import java.util.List;

public class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.PersonViewHolder>{
    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Visit visit);
    }
    public interface PersonAdapterListener{
        void onPersonClick(String person_uuid);
    }

    private List<Visit> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private PersonAdapterListener personAdapterListener;
    protected Context context;
    private String transaction_uuid;

    public VisitAdapter(@NonNull Context context, OnDeleteButtonListener listener, PersonAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.personAdapterListener= adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_course,parent,false);

        return new PersonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position){
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Visit> newData){
        if(data!=null){
            PersonDiffCallback personDiffCallback = new PersonDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(personDiffCallback);
            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class PersonViewHolder extends RecyclerView.ViewHolder{
        PersonViewHolder(@NonNull View view){
            super(view);
        }
        void bind(final Visit visit){
            if(visit != null){

            }
        }
    }

    class PersonDiffCallback extends DiffUtil.Callback{
        private final List<Visit> oldVisits, newVisits;

        PersonDiffCallback(List<Visit> oldVisits, List<Visit> newVisits){
            this.oldVisits = oldVisits;
            this.newVisits = newVisits;
        }

        @Override
        public int getOldListSize() {
            return oldVisits.size();
        }

        @Override
        public int getNewListSize() {
            return newVisits.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldVisits.get(oldItemPosition).getVisit_uuid().equals(newVisits.get(newItemPosition).getVisit_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldVisits.get(oldItemPosition).equals(newVisits.get(newItemPosition));
        }
    }
}
