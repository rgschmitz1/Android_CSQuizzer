package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.tacoma.uw.csquizzer.model.Course;
import edu.tacoma.uw.csquizzer.model.Topic;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {
    private List<Course> lCourses = new ArrayList<>();
    private List<Course> queryCourses = new ArrayList<>();
    private Context mContext;
    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;

    public CourseAdapter(Context mContext, List<Course> lCourses) {
        this.mContext = mContext;
        this.lCourses = lCourses;
        this.queryCourses.addAll(lCourses);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return TYPE2;
        else
            return TYPE1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType)
        {
            case TYPE1:
                view = inflater.inflate(R.layout.item_courses_white, parent, false);
                break;
            case TYPE2:
                view = inflater.inflate(R.layout.item_courses_purple, parent, false);
                break;
        }

        return new CourseAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Course course = lCourses.get(position);
        holder.tvCourseId.setText(course.getCourseId() + ".");
        holder.tvCourseName.setText(course.getCourseName());
    }

    @Override
    public int getItemCount() {
        return lCourses.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lCourses.clear();
        if (charText.length() == 0) {
            lCourses.addAll(queryCourses);
        } else {
            for (Course course : queryCourses) {
                if (course.getCourseName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lCourses.add(course);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCourseId;
        private TextView tvCourseName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvCourseId = itemView.findViewById(R.id.tv_CourseId);
            this.tvCourseName = itemView.findViewById(R.id.tv_CourseName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    EditCourseFragment editCourseFragment = new EditCourseFragment(mContext, tvCourseId.getText().toString(), tvCourseName.getText().toString());
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, editCourseFragment)
                            .commit();
                }
            });
        }
    }
}
