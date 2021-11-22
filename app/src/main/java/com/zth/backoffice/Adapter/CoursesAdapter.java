package com.zth.backoffice.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.zth.backoffice.Fragments.RoundActivity;
import com.zth.backoffice.Model.Course;
import com.zth.backoffice.R;

import java.util.ArrayList;
import java.util.List;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.MyViewHolder> {
    Context context;
    List<Course> courseList;
    List<Course> courseListFull;

    public CoursesAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
        courseListFull = new ArrayList<>(courseList);
    }

    @NonNull
    @Override
    public CoursesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_courses, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.MyViewHolder holder, int position) {

        Picasso.get()
                .load(courseList.get(position).getCoursePhotoURL())
                .placeholder(R.drawable.waiting)
                .error(R.drawable.error)
                .into(holder.courseImage);

        holder.courseTitle.setText(courseList.get(position).getTitle());
        holder.courseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), RoundActivity.class);
                i.putExtra("courseId", courseList.get(position).getCourseId());
                i.putExtra("title",courseList.get(position).getTitle());
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseTitle;
        ImageView courseImage;
        CardView courseCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.iv_courses);
            courseTitle = itemView.findViewById(R.id.tv_courses);
            courseCard = itemView.findViewById(R.id.cv_courses);
        }
    }

}
