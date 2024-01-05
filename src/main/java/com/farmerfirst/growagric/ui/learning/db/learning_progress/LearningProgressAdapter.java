package com.farmerfirst.growagric.ui.learning.db.learning_progress;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.learning.online_file.ViewOnlinePdfActivity;

import java.util.ArrayList;
import java.util.List;

public class LearningProgressAdapter extends RecyclerView.Adapter<LearningProgressAdapter.LearningViewHolder>{

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(LearningProgress learningProgress);
    }
    public interface LearningProgressAdapterListener {
        void onLearningProgressClick(String progress_uuid);
    }

    private List<LearningProgress> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private LearningProgressAdapterListener learningProgressAdapterListener;
    protected Context context;
    private String progesss_uuid;

    public LearningProgressAdapter(@NonNull Context context,OnDeleteButtonListener listener,LearningProgressAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.learningProgressAdapterListener = adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public LearningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_course,parent,false);
        return new LearningViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<LearningProgress> newData){
        if(data!=null){
            LearningProgressDiffCallback learningProgressDiffCallback = new LearningProgressDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(learningProgressDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class LearningViewHolder extends RecyclerView.ViewHolder{
        //TextView tvCourseTopic,tvCourseDescription;
        //CardView cardCourseView;
        LearningViewHolder(@NonNull View view){
            super(view);

            //tvCourseTopic = view.findViewById(R.id.tvCourseTopic);
            //tvCourseDescription = view.findViewById(R.id.tvCourseDescription);
            //cardCourseView = view.findViewById(R.id.cardCourseView);
        }

        void bind(final LearningProgress learningProgress){
            if(learningProgress != null){
                /*
                if(course.course_name.trim().length() <= 18) {
                    tvCourseTopic.setText(course.course_name.substring(0, course.course_name.trim().length()));
                }else{
                    tvCourseTopic.setText(course.course_name.substring(0, 18)+"...");
                }
                tvCourseDescription.setText(course.description);

                cardCourseView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(),course.path,Toast.LENGTH_SHORT).show();
                        openPDFView(course.path,course.filename);
                    }
                });
                */
            }
        }
    }

    class LearningProgressDiffCallback extends DiffUtil.Callback{

        private final List<LearningProgress> oldLearningProgress, newLearningProgress;

        LearningProgressDiffCallback(List<LearningProgress> oldLearningProgress, List<LearningProgress> newLearningProgress){
            this.oldLearningProgress = oldLearningProgress;
            this.newLearningProgress = newLearningProgress;
        }

        @Override
        public int getOldListSize() {
            return oldLearningProgress.size();
        }

        @Override
        public int getNewListSize() {
            return newLearningProgress.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldLearningProgress.get(oldItemPosition).getProgress_uuid().equals(newLearningProgress.get(newItemPosition).getProgress_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldLearningProgress.get(oldItemPosition).equals(newLearningProgress.get(newItemPosition));
        }
    }

    public String searchLearningProgressFilter(){
        return progesss_uuid;
    }
}
