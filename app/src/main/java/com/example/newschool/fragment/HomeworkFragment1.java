package com.example.newschool.fragment;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.newschool.R;
import com.example.newschool.activity.StuHomeworkActivity;
import com.example.newschool.adapter.ClassmateNotHomeworkAdapter;
import com.example.newschool.adapter.ClassmateNotSignAdapter;
import com.example.newschool.adapter.FileListAdapter;
import com.example.newschool.adapter.FileListAdapter2;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.StuHomework;
import com.example.newschool.bean.StudentInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.b.V;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeworkFragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeworkFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView issueTime, title, deadline, subTitle, textDone, textNot;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView, recyclerViewNotDone;
    private OnFragmentInteractionListener mListener;
    private String homeworkId, courseId;
    private List<String> strings;
    private List<String> stuNoDone, stuNoNot;

    public HomeworkFragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeworkFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeworkFragment1 newInstance(String param1, String param2) {
        HomeworkFragment1 fragment = new HomeworkFragment1();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework_fragment1, container, false);
        initView(view);
        addData();
        return view;
    }

    private Boolean compareDate(String a) {
        Date date1 = null, date2 = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date1 = sdf.parse(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1.after(date2);
    }

    private void addData() {
        BmobQuery<HomeworkInfo> query = new BmobQuery<>();
        query.getObject(homeworkId, new QueryListener<HomeworkInfo>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void done(HomeworkInfo object, BmobException e) {
                if (e == null) {
                    issueTime.setText("作业发布于：" + object.getCreatedAt());
                    title.setText(object.getTitle());
                    deadline.setText("提交期限：" + object.getDeadline().getDate());
                    if (!compareDate(object.getDeadline().getDate()))
                        deadline.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                    subTitle.setText(object.getDescribe());

                    strings = new ArrayList<>();
                    for (BmobFile a : object.getFiles()
                            ) {
                        strings.add(a.getUrl());
                    }

                    FileListAdapter2 fileListAdapter = new FileListAdapter2(strings);

                    fileListAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(fileListAdapter);

                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

        BmobQuery<StuHomework> find = new BmobQuery<StuHomework>();
        HomeworkInfo homeworkInfo = new HomeworkInfo();
        homeworkInfo.setObjectId(homeworkId);

        find.addWhereEqualTo("homeworkInfo", homeworkInfo);
        find.include("studentInfo");
        find.setLimit(100);

        find.findObjects(new FindListener<StuHomework>() {
            @Override
            public void done(List<StuHomework> object, BmobException e) {
                if (e == null) {
                    Log.i("done: ", "查询成功：StuHomework共" + object.size() + "条数据。");
                    stuNoDone = new ArrayList<>();
                    for (StuHomework f : object
                            ) {
                        stuNoDone.add(f.getStudentInfo().getObjectId());
                    }
                    findJoinedCourse(String.valueOf(object.size()));

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }

    private void findJoinedCourse(final String done) {
        BmobQuery<JoinedCourse> find = new BmobQuery<JoinedCourse>();
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseId);
        find.addWhereEqualTo("courses", courseInfo);
        find.include("studentInfo");
        find.setLimit(100);

        find.findObjects(new FindListener<JoinedCourse>() {
            @Override
            public void done(List<JoinedCourse> object, BmobException e) {
                if (e == null) {
                    Log.i("done: ", "查询成功：JoinedCourse共" + object.size() + "条数据。");
                    List<StudentInfo> infos = new ArrayList<>();
                    stuNoNot = new ArrayList<>();
                    for (JoinedCourse f : object
                            ) {
//                        infos.add(f.getStudentInfo());
                        stuNoNot.add(f.getStudentInfo().getObjectId());
                    }
                    stuNoNot.removeAll(stuNoDone);
                    for (JoinedCourse f : object
                         ) {
                        for (String s:stuNoNot
                             ) {
                            if(s.equals(f.getStudentInfo().getObjectId()))
                                infos.add(f.getStudentInfo());
                        }
                    }
                    setTextDone(done, String.valueOf(stuNoNot.size()));
                    ClassmateNotHomeworkAdapter signAdapter = new ClassmateNotHomeworkAdapter(infos);

                    signAdapter.notifyDataSetChanged();
                    recyclerViewNotDone.setAdapter(signAdapter);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void initView(View view) {
        Intent intent = ((StuHomeworkActivity) mListener).getIntent();
        homeworkId = intent.getStringExtra("homeworkId");
        courseId = intent.getStringExtra("courseId");
        refreshLayout = view.findViewById(R.id.HomeworkFragment1_freshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        recyclerView = view.findViewById(R.id.HomeworkFragment1_recyclerView);
        issueTime = view.findViewById(R.id.HomeworkFragment1_issueTime);
        title = view.findViewById(R.id.HomeworkFragment1_title);
        deadline = view.findViewById(R.id.HomeworkFragment1_deadline);
        subTitle = view.findViewById(R.id.HomeworkFragment1_subTitle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        strings = new ArrayList<>();
        FileListAdapter2 fileListAdapter = new FileListAdapter2(strings);
        recyclerView.setAdapter(fileListAdapter);
        refreshLayout.setRefreshing(false);
        textDone = view.findViewById(R.id.HomeworkFragment1_textDone);
        textNot = view.findViewById(R.id.HomeworkFragment1_textNot);
        textNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewNotDone.getVisibility()==View.VISIBLE){
                    recyclerViewNotDone.setVisibility(View.GONE);
                }else {
                    recyclerViewNotDone.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerViewNotDone = view.findViewById(R.id.HomeworkFragment1_recyclerViewNotDone);
        LinearLayoutManager layoutManager = new LinearLayoutManager((Context) mListener);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewNotDone.setLayoutManager(layoutManager);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void setTextDone(String done, String not) {
        if (mListener != null) {
            mListener.onFragmentInteraction(done, not);
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
        void onFragmentInteraction(String done, String not);
    }
}
