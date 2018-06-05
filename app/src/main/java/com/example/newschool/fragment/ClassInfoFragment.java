package com.example.newschool.fragment;

import android.content.Context;
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
import com.example.newschool.adapter.InfoStreamAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.SignToCourse;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassInfoFragment extends Fragment {
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
    private List<HomeworkInfo> homeworkInfoList;
private InfoStreamAdapter infoStreamAdapter;
    public ClassInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassInfoFragment newInstance(String param1, String param2) {
        ClassInfoFragment fragment = new ClassInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_classinfo, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeworkInfoList = new ArrayList<>();
        refreshLayout = view.findViewById(R.id.classInfoFragment_freshLayout);
        recyclerView = view.findViewById(R.id.classInfoFragment_recyclerView);
        courseId = ((ClassbeginActivity) mListener).getIntent().getStringExtra("invitedCode");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });

        infoStreamAdapter = new InfoStreamAdapter(homeworkInfoList);
        recyclerView.setAdapter(infoStreamAdapter);
        addData();
    }

    private void addData() {
        homeworkInfoList = new ArrayList<>();
        final CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(courseId);
        BmobQuery<HomeworkInfo> query = new BmobQuery<HomeworkInfo>();
        query.addWhereEqualTo("courseInfo", courseInfo);
        query.setLimit(100);
        query.include("teacherInfo,courseInfo");
        query.order("-createdAt");
        query.findObjects(new FindListener<HomeworkInfo>() {
            @Override
            public void done(List<HomeworkInfo> object, BmobException e) {
                if (e == null) {
                    Log.i("done: ", "查询成功：共" + object.size() + "条数据。");
                    for (HomeworkInfo h : object
                            ) {
                        if (null != h.getFiles()) {
                            h.setDone(String.valueOf(h.getFiles().size()));
                            homeworkInfoList.add(h);
                            Log.i("done: ", String.valueOf(h.getFiles().size()));
                        }

                    }


//                    BmobQuery<JoinedCourse> query = new BmobQuery<JoinedCourse>();
//                    query.addWhereEqualTo("courses", courseInfo);
//                    query.count(JoinedCourse.class, new CountListener() {
//                        @Override
//                        public void done(Integer count, BmobException e) {
//                            if(e==null){
//                                Log.i("done: ","count对象个数为："+count);
//                            }else{
//                                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                            }
//                        }
//                    });

                 //   InfoStreamAdapter
                    infoStreamAdapter = new InfoStreamAdapter(object);
                    infoStreamAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(infoStreamAdapter);
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


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
