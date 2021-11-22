package com.zth.backoffice.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zth.backoffice.Adapter.CoursesAdapter;
import com.zth.backoffice.Common.Common;
import com.zth.backoffice.Interface.RetrofitServiceCourses;
import com.zth.backoffice.Model.Course;
import com.zth.backoffice.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoursesFragment extends Fragment {

    RecyclerView recyclerCourseList;
    RetrofitServiceCourses mService;
    RecyclerView.LayoutManager layoutManager;
    CoursesAdapter adapter;
    ProgressBar progressBar;
    TextView waiting;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_course, container, false);
        mService = Common.getCourses();
        recyclerCourseList = view.findViewById(R.id.rv_courses);
        recyclerCourseList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerCourseList.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.pb_courses);
        waiting = view.findViewById(R.id.waitingCourses);
        getAllCourseList(view);
        return view;
    }

    public void getAllCourseList(View view) {
        mService.getCourseList().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                progressBar.setVisibility(View.GONE);
                waiting.setVisibility(View.GONE);
                adapter = new CoursesAdapter(getContext(), response.body());
                adapter.notifyDataSetChanged();
                recyclerCourseList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {

            }
        });

    }

}