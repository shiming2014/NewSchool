package com.example.newschool.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
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
import com.example.newschool.activity.SignDetailActivity;
import com.example.newschool.adapter.ClassmateAdapter;
import com.example.newschool.bean.CourseInfo;
import com.example.newschool.bean.JoinedCourse;
import com.example.newschool.bean.SignToCourse;

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
 * {@link SignYesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignYesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignYesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private RecyclerView recyclerView;
    private List<SignToCourse> studentInCourse;
    private List<String> haveSign, notSign;
    private SwipeRefreshLayout refreshLayout;

    public SignYesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SignYesFragment newInstance(List<SignToCourse> sign) {
        SignYesFragment fragment = new SignYesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("test: ", "onCreate");
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_yes, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.SignYesFragment_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshLayout = view.findViewById(R.id.SignYesFragment_freshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        addData();

    }

    private void addData() {
        haveSign = new ArrayList<>();
        notSign = new ArrayList<>();
        studentInCourse = new ArrayList<>();
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
                    studentInCourse = object1;
                    ClassmateAdapter classmateAdapter = new ClassmateAdapter(studentInCourse);
                    classmateAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(classmateAdapter);

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

                                }
                                notSign.removeAll(haveSign);

                                setYesAndNo(String.valueOf(haveSign.size()), String.valueOf(notSign.size()));
                                refreshLayout.setRefreshing(false);


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
        Log.i("test: ", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.i("test: ", "onDetach");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("test: ", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("test", "onPause: ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
//            addData();
            Log.i("test", "setUserVisibleHint: isVisibleToUser");
            //相当于Fragment的onResume
        } else {
            Log.i("test", "setUserVisibleHint: notVisibleToUser ");
            //相当于Fragment的onPause
        }
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
