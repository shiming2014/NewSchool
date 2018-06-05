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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.newschool.R;
import com.example.newschool.activity.StuHomeworkActivity;
import com.example.newschool.adapter.StuHomeworkListAdapter;
import com.example.newschool.bean.HomeworkInfo;
import com.example.newschool.bean.StuHomework;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.BmobConstants.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeworkFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeworkFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;
    private List<StuHomework> stuHomeworks;
    private String courseId, homeworkId;

    public HomeworkFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeworkFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeworkFragment2 newInstance(String param1, String param2) {
        HomeworkFragment2 fragment = new HomeworkFragment2();
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
        View view = inflater.inflate(R.layout.fragment_homework_fragment2, container, false);
        initView(view);
        addData();
        return view;
    }

    private void initView(View view) {
        Intent intent = ((StuHomeworkActivity) mListener).getIntent();
        homeworkId = intent.getStringExtra("homeworkId");
        courseId = intent.getStringExtra("courseId");
        stuHomeworks = new ArrayList<>();
        recyclerView = view.findViewById(R.id.HomeworkFragment2_recyclerView);
        refreshLayout = view.findViewById(R.id.HomeworkFragment2_refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addData();
            }
        });
        StuHomeworkListAdapter adapter = new StuHomeworkListAdapter(stuHomeworks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void addData() {
        HomeworkInfo homeworkInfo = new HomeworkInfo();
        homeworkInfo.setObjectId(homeworkId);
        BmobQuery<StuHomework> query = new BmobQuery<StuHomework>();

        query.addWhereEqualTo("homeworkInfo", homeworkInfo);
        query.include("studentInfo");
        query.order("updatedAt");
        query.setLimit(100);

        query.findObjects(new FindListener<StuHomework>() {
            @Override
            public void done(List<StuHomework> object, BmobException e) {
                if (e == null) {
                    Log.i(TAG, "done: 查询成功：共" + object.size() + "条数据。");
                    StuHomeworkListAdapter adapter = new StuHomeworkListAdapter(object);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setRefreshing(false);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String done, String not) {
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
