package com.example.newschool.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newschool.R;
import com.example.newschool.activity.ClassbeginActivity;
import com.example.newschool.activity.EditInfoActivity;
import com.example.newschool.activity.ShareListActivity;
import com.example.newschool.bean.CourseInfo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassCourseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassCourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView courseName, courseLoaction, courseTime;
    private TextView open, edit;
    private TextView classmateShare;
    private CardView teacherInfo;
    private TextView teacherName, phone, email;
    private String courseId;
    private LinearLayout linearLayout;

    public ClassCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassCourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassCourseFragment newInstance(String param1, String param2) {
        ClassCourseFragment fragment = new ClassCourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_classcourse, container, false);
        initView(view);
        addData();
        addListener();
        return view;
    }

    private void addListener() {
        teacherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);

                } else {
                    linearLayout.setVisibility(View.GONE);
                }

            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ObsoleteSdkInt")
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cn = null;
                if (Build.VERSION.SDK_INT >= 8) {
                    cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
                } else {
                    cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
                }
                i.setComponent(cn);
                startActivity(i);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Context) mListener, EditInfoActivity.class);
                intent.putExtra("courseId", courseId);
                ClassCourseFragment.this.startActivityForResult(intent, 101);
            }
        });
        classmateShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent((Context) mListener, ShareListActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case 101:
                if (resultCode == 102) {
                    ((Activity) mListener).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (null != data.getStringExtra("time"))
                                if (!"".equals(data.getStringExtra("time")))
                                    courseTime.setText("课程时间：" + data.getStringExtra("time"));
                            if (null != data.getStringExtra("location"))
                                if (!"".equals(data.getStringExtra("location")))
                                    courseLoaction.setText("教室地点：" + data.getStringExtra("location"));
                        }
                    });
                }

                break;

        }

    }

    private void addData() {
        BmobQuery<CourseInfo> query = new BmobQuery<>();
        query.include("teacherInfo");
        query.getObject(courseId, new QueryListener<CourseInfo>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void done(CourseInfo object, BmobException e) {
                if (e == null) {
                    if (null != object.getTime())
                        if (!"".equals(object.getTime()))
                            courseTime.setText("课程时间：" + object.getTime());
                    if (null != object.getLocation())
                        if (!"".equals(object.getLocation()))
                            courseLoaction.setText("课程地点：" + object.getLocation());
                    teacherName.setText("教师姓名：" + object.getTeacherInfo().getTeacherName());
                    phone.setText("教师手机：" + object.getTeacherInfo().getMobilePhoneNumber());
                    email.setText("教师邮箱：" + object.getTeacherInfo().getEmail());

                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }

        });

    }

    private void initView(View view) {
        linearLayout = view.findViewById(R.id.fragment_about_layout);
        courseId = ((ClassbeginActivity) mListener).getIntent().getStringExtra("invitedCode");
        courseName = view.findViewById(R.id.fragment_about_title);
        courseName.setText(((ClassbeginActivity) mListener).getIntent().getStringExtra("courseName"));
        courseLoaction = view.findViewById(R.id.fragment_about_location);
        courseTime = view.findViewById(R.id.fragment_about_time);
        open = view.findViewById(R.id.fragment_about_calendar);
        classmateShare = view.findViewById(R.id.fragment_about_share);
        teacherName = view.findViewById(R.id.fragment_about_name);
        phone = view.findViewById(R.id.fragment_about_phone);
        email = view.findViewById(R.id.fragment_about_email);
        edit = view.findViewById(R.id.fragment_about_edit);
        teacherInfo = view.findViewById(R.id.fragment_about_cardView);
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
