package com.example.newschool.fragment;

import android.annotation.SuppressLint;
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
import com.example.newschool.activity.SignDetailActivity;
import com.example.newschool.adapter.ClassmateAdapter;
import com.example.newschool.adapter.ClassmateNotSignAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.SignToCourse;
import com.example.newschool.bean.StudentInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignNoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignNoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignNoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    private List<StudentInfo> studentInfos;
    private List<String> haveSign, notSign;
    private SwipeRefreshLayout refreshLayout;

    public SignNoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignNoFragment newInstance(List<StudentInfo> studentInfos) {
        SignNoFragment fragment = new SignNoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_no, container, false);
        initView(view);
        addData();
        return view;
    }

    private void addData() {
        haveSign = new ArrayList<>();
        notSign = new ArrayList<>();
        studentInfos = new ArrayList<>();
        BmobQuery<SignToCourse> query = new BmobQuery<>();
        final CourseInfo courseInfo = new CourseInfo();
        courseInfo.setObjectId(((SignDetailActivity) mListener).getIntent().getStringExtra("courseID"));

        try {
            query.addWhereLessThanOrEqualTo("issueTime", new BmobDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(((SignDetailActivity) mListener).getIntent().getStringExtra("issueTime"))));
            query.addWhereGreaterThanOrEqualTo("issueTime", new BmobDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(((SignDetailActivity) mListener).getIntent().getStringExtra("issueTime"))));
            Log.i("addData: ", ((SignDetailActivity) mListener).getIntent().getStringExtra("issueTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        query.addWhereEqualTo("courseInfo", courseInfo);
        query.include("studentInfo");
        query.findObjects(new FindListener<SignToCourse>() {

            @Override
            public void done(final List<SignToCourse> object1, BmobException e) {
                if (e == null) {
                    for (SignToCourse s : object1
                            ) {
                        haveSign.add(s.getStudentInfo().getObjectId());

                    }
                    BmobQuery<JoinedCourse> query2 = new BmobQuery<JoinedCourse>();
                    query2.addWhereEqualTo("courses", courseInfo);// 查询当前用户的所有帖子
                    query2.order("-updatedAt");
                    query2.include("studentInfo");// 希望在查询帖子信息的同时也把发布人的信息查询出来
                    query2.findObjects(new FindListener<JoinedCourse>() {

                        @Override
                        public void done(List<JoinedCourse> object, BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "成功");
                                for (JoinedCourse p : object
                                        ) {

                                    notSign.add(p.getStudentInfo().getObjectId());
                                    Log.i("done:chengggg ", p.getStudentInfo().getObjectId());

                                }
                                notSign.removeAll(haveSign);

                                BmobQuery<StudentInfo> query3 = new BmobQuery<StudentInfo>();
                                query3.addWhereContainedIn("objectId", notSign);
                                query3.order("-updatedAt");
                                query3.findObjects(new FindListener<StudentInfo>() {

                                    @Override
                                    public void done(List<StudentInfo> object,BmobException e) {
                                        if(e==null){
                                            Log.i("bmob12344","成功");
                                            ClassmateNotSignAdapter classmateAdapter = new ClassmateNotSignAdapter(object);
                                            classmateAdapter.notifyDataSetChanged();
                                            recyclerView.setAdapter(classmateAdapter);


                                            refreshLayout.setRefreshing(false);
                                        }else{
                                            Log.i("bmob12344","失败："+e.getMessage());
                                        }
                                    }

                                });




                                setYesAndNo(String.valueOf(haveSign.size()), String.valueOf(notSign.size()));


                            } else {
                                Log.i("bmob", "失败：" + e.getMessage());
                            }
                        }

                    });


                    Log.i("bmob", "查询个数：" + object1.size());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }

        });


    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.SignNoFragment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout = view.findViewById(R.id.SignNoFragment_freshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void setYesAndNo(String yes, String no) {
        if (mListener != null) {
            mListener.onFragmentInteraction(yes, no);
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
        void onFragmentInteraction(String yes, String no);
    }
}
