package com.farmerfirst.growagric.ui.record_keeping.db.person;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.record_keeping.input.AddEmployeeActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Person person);
    }
    public interface PersonAdapterListener{
        void onPersonClick(String person_uuid);
    }

    private List<Person> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private PersonAdapterListener personAdapterListener;
    protected Context context;
    private String transaction_uuid;

    public PersonAdapter(@NonNull Context context,OnDeleteButtonListener listener,PersonAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.personAdapterListener= adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_person,parent,false);

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

    public void setData(List<Person> newData){
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
        TextView tvName,tvPhone,tvGeneral;
        ImageButton buttonDelete;
        PersonViewHolder(@NonNull View view){
            super(view);
            view.setOnClickListener(v-> personAdapterListener.onPersonClick(data.get(getLayoutPosition()).getPerson_name()));
            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvGeneral = view.findViewById(R.id.tvGeneral);
            buttonDelete = view.findViewById(R.id.buttonDelete);
        }

        void bind(final Person person){
            if(person != null){
                tvName.setText(person.getPerson_name());
                tvPhone.setText(person.getPhone_number());
                tvGeneral.setText(person.getId_number());

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onDeleteButtonListener.onDeleteButtonClicked(person);
                    }
                });
            }
        }
    }

    class PersonDiffCallback extends DiffUtil.Callback{
        private final List<Person> oldPerson,newPerson;

        PersonDiffCallback(List<Person> oldPerson,List<Person> newPerson){
            this.oldPerson=oldPerson;
            this.newPerson=newPerson;
        }

        @Override
        public int getOldListSize() {
            return oldPerson.size();
        }

        @Override
        public int getNewListSize() {
            return newPerson.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPerson.get(oldItemPosition).getPerson_uuid().equals(newPerson.get(newItemPosition).getPerson_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldPerson.get(oldItemPosition).equals(newPerson.get(newItemPosition));
        }
    }
}
