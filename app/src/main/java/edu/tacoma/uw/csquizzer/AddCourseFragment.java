package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.tacoma.uw.csquizzer.helper.ServiceHandler;

public class AddCourseFragment extends Fragment {
    ImageButton tvBackToList;
    EditText tvCourseName;
    Button btnAddCourse;
    Button btnCancelCourse;
    Context context;

    public AddCourseFragment(Context mContext) {
        this.context = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_course, container, false);
        tvCourseName = rootView.findViewById(R.id.et_CourseName);
        btnAddCourse = rootView.findViewById(R.id.btn_AddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseName = tvCourseName.getText().toString();
                if(courseName.length() == 0) {
                    Toast.makeText(getContext(), "Please input the course name", Toast.LENGTH_SHORT)
                            .show();
                } else{
                    String[] args = new String[]{courseName};
                    AddCourse task = new AddCourse(context, courseName, new MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                            if (result == true) {
                                Toast.makeText(context, "Update course successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Update course unsuccessfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    task.execute();
                }
            }
        });
        btnCancelCourse = rootView.findViewById(R.id.btn_CancelCourse);
        btnCancelCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCourseName.setText("");
            }
        });
        tvBackToList = rootView.findViewById(R.id.imb_back_to_list);
        tvBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseFragment courseFragment =  new CourseFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, courseFragment);
                ft.commit();
            }
        });
        return rootView;
    }

    public interface MyInterface {
        public void myMethod(boolean result);
    }

    private class AddCourse extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String courseName;

        public AddCourse(Context mContext, String mCourseName, MyInterface listener) {
            this.context = mContext;
            this.courseName = mCourseName;
            this.mListener  = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("name", courseName);
            String jsonCourse = jsonParser.makeServiceCall(
                    getString((R.string.add_courses)), ServiceHandler.POST,mapConditions);
            if (jsonCourse != null) {
                try {
                    JSONObject jsonCourseObj = new JSONObject(jsonCourse);
                    if(jsonCourseObj.getBoolean("success")) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (mListener != null)
                mListener.myMethod(result);
        }
    }
}