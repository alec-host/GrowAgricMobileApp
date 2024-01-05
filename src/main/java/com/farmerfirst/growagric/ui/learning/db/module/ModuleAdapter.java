package com.farmerfirst.growagric.ui.learning.db.module;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.learning.LearnCourseActivity;
import com.farmerfirst.growagric.ui.learning.LearnModuleActivity;
import com.farmerfirst.growagric.ui.learning.db.course.Course;
import com.farmerfirst.growagric.ui.learning.db.course.CourseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder>{

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Module module);
    }
    public interface ModuleAdapterListener{
        void onModuleClick(String module_uuid);
    }

    private List<Module> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private ModuleAdapterListener moduleAdapterListener;
    protected Context context;
    private String module_uuid;

    public ModuleAdapter(@NonNull Context context, OnDeleteButtonListener onDeleteButtonListener, ModuleAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = onDeleteButtonListener;
        this.moduleAdapterListener = adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_module,parent,false);
        return new ModuleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position){
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    public void setData(List<Module> newData){
        if(data!=null){
            ModuleDiffCallback moduleDiffCallback = new ModuleDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(moduleDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class ModuleViewHolder extends RecyclerView.ViewHolder{
        TextView tvModuleTopic,tvModuleCount,tvModuleDescription;
        CardView cardModuleView;
        ModuleViewHolder(@NonNull View view){
            super(view);
            tvModuleTopic = view.findViewById(R.id.tvModuleTopic);
            tvModuleCount = view.findViewById(R.id.tvModuleCount);
            tvModuleDescription = view.findViewById(R.id.tvModuleDescription);
            cardModuleView = view.findViewById(R.id.cardModuleView);
        }

        void bind(Module module){
            if(module != null){
                tvModuleTopic.setText(module.getTopic());
                tvModuleCount.setText("0");
                tvModuleDescription.setText(module.getDescription());
                cardModuleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), LearnCourseActivity.class);
                        intent.putExtra("MODULE_UUID",module.getModule_uuid());
                        intent.putExtra("MODULE_TOPIC",module.getTopic());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        ActivityCompat.startActivity(view.getContext(),intent,null);
                    }
                });
            }
        }
    }

    class ModuleDiffCallback extends DiffUtil.Callback{
        private final List<Module> oldModules, newModules;

        ModuleDiffCallback(List<Module> oldModules, List<Module> newModules){
            this.oldModules = oldModules;
            this.newModules = newModules;
        }

        @Override
        public int getOldListSize() {
            return oldModules.size();
        }

        @Override
        public int getNewListSize() {
            return newModules.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldModules.get(oldItemPosition).getModule_uuid().equals(newModules.get(newItemPosition).getModule_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldModules.get(oldItemPosition).equals(newModules.get(newItemPosition));
        }
    }

    public String searchModuleFilter(){
        return module_uuid;
    }
}