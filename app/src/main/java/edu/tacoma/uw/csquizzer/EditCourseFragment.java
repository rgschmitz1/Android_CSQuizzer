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
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;

/**
 * The purpose of EditCourseFragment module is to edit course
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-17
 */
public class EditCourseFragment extends Fragment {
    TextView tvCourseId;
    ImageButton tvBackToList;
    EditText tvCourseName;
    Button btnSubmit;
    Button btnCancel;
    String courseId;
    String courseName;
    Context context;
    public EditCourseFragment(Context mContext, String mCourseId, String mCourseName) {
        this.context = mContext;
        this.courseId = String.valueOf(mCourseId.charAt(0));
        this.courseName = mCourseName;
    }

    /**
     * * Render components to GUI
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     *
     * @author  Phuc Pham N
     * @since   2020-08-17
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_course, container, false);
        tvCourseId = rootView.findViewById(R.id.tv_CourseId);
        tvCourseId.setText(courseId);
        tvCourseName = rootView.findViewById(R.id.et_CourseName);
        tvCourseName.setText(courseName);
        btnSubmit = rootView.findViewById(R.id.btn_SubmitCourse);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseId = tvCourseId.getText().toString();
                String courseName = tvCourseName.getText().toString();
                if(courseName.length() == 0) {
                    Toast.makeText(getContext(), "Please input the course name", Toast.LENGTH_SHORT)
                            .show();
                } else{
                    String[] args = new String[]{courseId, courseName};
                    EditCourse task = new EditCourse(context, courseId, courseName, new MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                        if (result == true) {
                            Toast.makeText(context, "Update topic successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Update topic unsuccessfully", Toast.LENGTH_LONG).show();
                        }
                        }
                    });
                    task.execute();
                }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelCourse);
        btnCancel.setOnClickListener(new View.OnClickListener() {
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

    /**
     * The EditCourse AsyncTask to edit course in database
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-17
     */
    private class EditCourse extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String courseId;
        String courseName;

        public EditCourse(Context mContext, String mCourseId, String mCourseName, MyInterface listener) {
            this.context = mContext;
            this.courseId = mCourseId;
            this.courseName = mCourseName;
            this.mListener = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("id", courseId);
            mapConditions.put("name", courseName);
            String jsonCourse = jsonParser.makeServiceCall(
                    getString((R.string.update_courses)), ServiceHandler.POST,mapConditions);
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