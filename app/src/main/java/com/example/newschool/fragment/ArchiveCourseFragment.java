package com.example.newschool.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.newschool.R;
import com.example.newschool.adapter.ArchiveCourseAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.TeacherInfo;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArchiveCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArchiveCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchiveCourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private List<CourseInfo> courseInfos;
    private ArchiveCourseAdapter archiveCourseAdapter;
    private SwipeRefreshLayout swipeRefresh;


    private OnFragmentInteractionListener mListener;

    public ArchiveCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchiveCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchiveCourseFragment newInstance(String param1, String param2) {
        ArchiveCourseFragment fragment = new ArchiveCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive_course, container, false);
        initView(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.fragment_ArchiveCourse_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefresh = view.findViewById(R.id.fragment_ArchiveCourse_RefreshLayout);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData2();
            }
        });


        addData();

    }

    private void addData() {
        BmobQuery<CourseInfo> query = new BmobQuery<CourseInfo>();
        query.addWhereEqualTo("teacherInfo", BmobUser.getCurrentUser(TeacherInfo.class));
        query.order("-updatedAt");
        boolean isCache = query.hasCachedResult(CourseInfo.class);
        if (isCache) {//此为举个例子，并不一定按这种方式来设置缓存策略
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.addWhereEqualTo("status", 0);
        query.setLimit(100);
        query.findObjects(new FindListener<CourseInfo>() {

            @Override
            public void done(List<CourseInfo> object, BmobException e) {
                if (e == null) {
                    courseInfos = object;
                    Log.e("查询结果", "done: " + courseInfos.toString());

                    archiveCourseAdapter = new ArchiveCourseAdapter(courseInfos);
                    archiveCourseAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(archiveCourseAdapter);
                } else {

                }
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_archivecourse, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Log.e("点击0下", "onOptionsItemSelected: "+item.getItemId());
        switch (item.getItemId()) {
            case R.id.menu_archiveCourse_refresh:
                Log.e("点击一下", "onOptionsItemSelected: ");
                addData2();
                break;

        }
        return false;
    }

    private void addData2() {
        BmobQuery<CourseInfo> query = new BmobQuery<CourseInfo>();
        query.order("-updatedAt");
        query.addWhereEqualTo("teacherInfo", BmobUser.getCurrentUser(TeacherInfo.class));
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);

        query.addWhereEqualTo("status", 0);
        query.setLimit(100);
        query.findObjects(new FindListener<CourseInfo>() {

            @Override
            public void done(List<CourseInfo> object, BmobException e) {
                if (e == null) {
                    courseInfos = object;
                    Log.e("查询结果", "done: " + courseInfos.toString());
                    archiveCourseAdapter = new ArchiveCourseAdapter(courseInfos);
                    archiveCourseAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(archiveCourseAdapter);
                    swipeRefresh.setRefreshing(false);
                } else {

                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
            case 2:
                if (resultCode == RESULT_OK) {
                    addData2();
                }
                break;

        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
