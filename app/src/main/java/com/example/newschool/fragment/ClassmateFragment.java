package com.example.newschool.fragment;

import android.content.Context;
import android.graphics.CornerPathEffect;
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

import com.example.newschool.R;
import com.example.newschool.activity.ClassbeginActivity;
import com.example.newschool.adapter.ClassmateNotSignAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.StudentInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassmateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassmateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassmateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private String courseId;
    private List<StudentInfo> studentInfos;

    public ClassmateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassmateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassmateFragment newInstance(String param1, String param2) {
        ClassmateFragment fragment = new ClassmateFragment();
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
        View view = inflater.inflate(R.layout.fragment_classmate, container, false);
        initView(view);
        addData();
        return view;
    }

    private void addData() {
        studentInfos = new ArrayList<>();
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseId);
        BmobQuery<JoinedCourse> query = new BmobQuery<JoinedCourse>();
        query.addWhereEqualTo("courses", courseInfo);// 查询当前用户的所有帖子
        query.order("-updatedAt");
        query.include("studentInfo");
//执行查询方法
        query.findObjects(new FindListener<JoinedCourse>() {
            @Override
            public void done(List<JoinedCourse> object, BmobException e) {
                if (e == null) {
                    Log.i("classmateFragment", "查询成功：共" + object.size() + "条数据。");
                    for (JoinedCourse joinedCourse : object) {
                        studentInfos.add(joinedCourse.getStudentInfo());
                    }

                    ClassmateNotSignAdapter classmateAdapter = new ClassmateNotSignAdapter(studentInfos);
                    classmateAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(classmateAdapter);
                    refreshLayout.setRefreshing(false);

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void initView(View view) {
        studentInfos = new ArrayList<>();
        courseId = ((ClassbeginActivity) mListener).getIntent().getStringExtra("invitedCode");
        refreshLayout = view.findViewById(R.id.classmateFragment_freshLayout);
        recyclerView = view.findViewById(R.id.classmateFragment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        ClassmateNotSignAdapter classmateAdapter = new ClassmateNotSignAdapter(studentInfos);
        recyclerView.setAdapter(classmateAdapter);
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
