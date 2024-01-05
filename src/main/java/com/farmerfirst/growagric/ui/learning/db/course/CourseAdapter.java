package com.farmerfirst.growagric.ui.learning.db.course;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.ui.learning.LearnCourseActivity;
import com.farmerfirst.growagric.ui.learning.online_file.ViewOnlinePdfActivity;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>{

    public interface OnDeleteButtonListener{
        void onDeleteButtonClicked(Course course);
    }
    public interface CourseAdapterListener{
        void onCourseClick(String course_uuid);
    }

    private List<Course> data;
    private LayoutInflater layoutInflater;
    private OnDeleteButtonListener onDeleteButtonListener;
    private CourseAdapterListener courseAdapterListener;
    protected Context context;
    private String course_uuid;

    public CourseAdapter(@NonNull Context context, OnDeleteButtonListener listener, CourseAdapterListener adapterListener){
        this.data = new ArrayList<>();
        this.context = context;
        this.onDeleteButtonListener = listener;
        this.courseAdapterListener = adapterListener;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = layoutInflater.inflate(R.layout.list_item_course,parent,false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Course> newData){
        if(data!=null){
            CourseDiffCallback courseDiffCallback = new CourseDiffCallback(data,newData);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(courseDiffCallback);

            data.clear();
            data.addAll(newData);
            diffResult.dispatchUpdatesTo(this);
        }else{
            data = newData;
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder{
        TextView tvCourseTopic,tvCourseDescription;
        CardView cardCourseView;
        ImageView imgViewBookMarked;
        CourseViewHolder(@NonNull View view){
            super(view);

            tvCourseTopic = view.findViewById(R.id.tvCourseTopic);
            tvCourseDescription = view.findViewById(R.id.tvCourseDescription);
            cardCourseView = view.findViewById(R.id.cardCourseView);
            imgViewBookMarked = view.findViewById(R.id.imgViewBookMarked);
        }

        void bind(final Course course){
            if(course != null){
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
                        openPDFView(course.path,course.filename,course.course_uuid,course.course_name);
                    }
                });

                if(course.is_downloaded != null){
                    if (Integer.parseInt(course.is_downloaded) == 1) {
                        imgViewBookMarked.setVisibility(View.VISIBLE);
                    } else {
                        imgViewBookMarked.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    class CourseDiffCallback extends DiffUtil.Callback{

        private final List<Course> oldCourses, newCourses;

        CourseDiffCallback(List<Course> oldCourses, List<Course> newCourses){
            this.oldCourses = oldCourses;
            this.newCourses = newCourses;
        }

        @Override
        public int getOldListSize() {
            return oldCourses.size();
        }

        @Override
        public int getNewListSize() {
            return newCourses.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldCourses.get(oldItemPosition).getCourse_uuid().equals(newCourses.get(newItemPosition).getCourse_uuid());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldCourses.get(oldItemPosition).equals(newCourses.get(newItemPosition));
        }
    }

    private void openPDFView(String path,String file_name,String course_uuid,String course_name){
        Intent intent = new Intent(context,ViewOnlinePdfActivity.class);
        intent.putExtra("FILE_PATH",path);
        intent.putExtra("FILE_NAME",file_name);
        intent.putExtra("COURSE_UUID",course_uuid);
        intent.putExtra("COURSE_NAME",course_name);
        context.startActivity(intent);
    }

    public String searchCourseFilter(){
        return course_uuid;
    }
}
