package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Course;
import edu.tacoma.uw.csquizzer.model.Difficulty;
import edu.tacoma.uw.csquizzer.model.Topic;
import edu.tacoma.uw.csquizzer.model.Type;

/**
 * The purpose of EditQuestionFragment module is to edit a question
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-17
 */
public class EditQuestionFragment extends Fragment {
    private TextView tvQuestionId;
    private ImageButton tvBackToList;
    private EditText tvQuestionTitle;
    private EditText tvQuestionBody;
    private Spinner spinnerCourses;
    private Spinner spinnerTopics;
    private Spinner spinnerDifficulties;
    private Spinner spinnerTypes;
    private Button btnUpdateQuestion;
    private Button btnCancel;
    private String questionId;
    private String questionTitle;
    private String questionBody;
    private String courseName;
    private String topicDescription;
    private String difficultyDescription;
    private String typeDescription;
    private Context context;
    private List<Course> lCourses;
    private List<Topic> lTopics;
    private List<Difficulty> lDifficulties;
    private List<Type> lTypes;
    ProgressDialog pDialog;

    public EditQuestionFragment(Context mContext, String mQuestionId, String mQuestionTitle,
                                String mQuestionBody, String courseName, String topicDescription,
                                String difficultyDescription, String typeDescription) {
        this.context = mContext;
        this.questionId = mQuestionId.substring(0, mQuestionId.length() - 1);
        this.questionTitle = mQuestionTitle;
        this.questionBody = mQuestionBody;
        this.courseName = courseName;
        this.topicDescription = topicDescription;
        this.difficultyDescription = difficultyDescription;
        this.typeDescription = typeDescription;
    }

    /**
     * * Render components to GUI
     *
     * @param savedInstanceState
     * @return view
     *
     * @author  Phuc Pham N
     * @since   2020-08-17
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Store list courses, topics, difficulty
        lCourses = new ArrayList<>();
        lTopics = new ArrayList<>();
        lDifficulties = new ArrayList<>();
        lDifficulties.add(new Difficulty(1, "Easy"));
        lDifficulties.add(new Difficulty(2, "Medium"));
        lDifficulties.add(new Difficulty(3, "Hard"));
        lTypes = new ArrayList<>();
        lTypes.add(new Type(1, "True/False"));
        lTypes.add(new Type(2, "Single Choice"));
        lTypes.add(new Type(3, "Multiple Choice"));
        new GetData().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_question, container, false);

        // get spinner information on GUI
        spinnerCourses = (Spinner) rootView.findViewById(R.id.sn_courses);
        spinnerTopics = (Spinner) rootView.findViewById(R.id.sn_topics);
        spinnerDifficulties = (Spinner) rootView.findViewById(R.id.sn_difficulties);
        if(difficultyDescription.equals("Easy")) {
            spinnerDifficulties.setSelection(1);
        } else if (difficultyDescription.equals("Medium")) {
            spinnerDifficulties.setSelection(2);
        } else if (difficultyDescription.equals("Hard")) {
            spinnerDifficulties.setSelection(3);
        }
        spinnerTypes = (Spinner) rootView.findViewById(R.id.sn_types);
        if(typeDescription.equals("True/False")) {
            spinnerTypes.setSelection(1);
        } else if (typeDescription.equals("Single Choice")) {
            spinnerTypes.setSelection(2);
        } else if (typeDescription.equals("Multiple Choice")) {
            spinnerTypes.setSelection(3);
        }
        tvQuestionId = rootView.findViewById(R.id.tv_QuestionId);
        tvQuestionId.setText(questionId);
        tvQuestionTitle = rootView.findViewById(R.id.et_QuestionTitle);
        tvQuestionTitle.setText(questionTitle);
        tvQuestionBody = rootView.findViewById(R.id.et_QuestionBody);
        tvQuestionBody.setText(questionBody);
        btnUpdateQuestion = rootView.findViewById(R.id.btn_UpdateQuestion);
        btnUpdateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String idQuestion = tvQuestionId.getText().toString();
                final String titleQuestion = tvQuestionTitle.getText().toString();
                final String bodyQuestion = tvQuestionBody.getText().toString();
                final String course = spinnerCourses.getSelectedItem().toString();
                final String topic = spinnerTopics.getSelectedItem().toString();
                final String difficulty = spinnerDifficulties.getSelectedItem().toString();
                final String type = spinnerTypes.getSelectedItem().toString();
                if((titleQuestion.length() != 0) && (bodyQuestion.length() != 0)
                        && !course.equals("--- Choose Course ---")
                        && !topic.equals("--- Choose Topic ---")
                        && !difficulty.equals("--- Choose Difficulty ---")
                        && !type.equals("--- Choose Type ---")) {
                    EditQuestion task = new EditQuestion(context, idQuestion, titleQuestion,
                            bodyQuestion, course, topic, difficulty, new MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                            if (result == true) {
                                if(type.equals("True/False")) {
                                    EditAnswerTrueFalseFragment editAnswerTrueFalseFragment =
                                            new EditAnswerTrueFalseFragment(
                                                    context, idQuestion, titleQuestion,
                                                    bodyQuestion, type, typeDescription);
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    //Replace current fragment with a show question fragment
                                    ft.replace(R.id.fragment_container, editAnswerTrueFalseFragment);
                                    ft.commit();
                                } else if (type.equals("Single Choice")) {
                                    EditAnswerSingleChoiceFragment editAnswerSingleChoiceFragment =
                                            new EditAnswerSingleChoiceFragment(
                                                    context, idQuestion, titleQuestion,
                                                    bodyQuestion, type, typeDescription);
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    //Replace current fragment with a show question fragment
                                    ft.replace(R.id.fragment_container, editAnswerSingleChoiceFragment);
                                    ft.commit();
                                } else if (type.equals("Multiple Choice")) {
                                    EditAnswerMultipleChoiceFragment editAnswerMultipleChoiceFragment =
                                            new EditAnswerMultipleChoiceFragment(
                                                    context, idQuestion, titleQuestion,
                                                    bodyQuestion, type, typeDescription);
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    //Replace current fragment with a show question fragment
                                    ft.replace(R.id.fragment_container, editAnswerMultipleChoiceFragment);
                                    ft.commit();
                                }
                            } else {
                                Toast.makeText(context, "Update question unsuccessfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    task.execute();
                } else {
                    Toast.makeText(context, "Please input data for a question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelQuestion);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            tvQuestionTitle.setText("");
            tvQuestionBody.setText("");
            }
        });
        tvBackToList = rootView.findViewById(R.id.imb_back_to_list);
        tvBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            QuestionFragment questionFragment =  new QuestionFragment();
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            //Replace current fragment with a show question fragment
            ft.replace(R.id.fragment_container, questionFragment);
            ft.commit();
            }
        });
        return rootView;
    }

    public interface MyInterface {
        public void myMethod(boolean result);
    }

    /**
     * The EditQuestion AsyncTask to edit a question in database
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-17
     */
    private class EditQuestion extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String questionId;
        String questionTitle;
        String questionBody;
        String CourseId;
        String TopicId;
        String DifficultyId;

        public EditQuestion(Context mContext, String mQuestionId, String mQuestionTitle,
                            String mQuestionBody, String mCourseId, String mTopicId,
                            String mDifficultyId, MyInterface listener) {
            this.context = mContext;
            this.questionId = mQuestionId;
            this.questionTitle = mQuestionTitle;
            this.questionBody = mQuestionBody;
            this.CourseId = mCourseId;
            this.TopicId = mTopicId;
            this.DifficultyId = mDifficultyId;
            this.mListener  = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("question", questionId);
            mapConditions.put("title", questionTitle);
            mapConditions.put("body", questionBody);
            String idCourse = "0";
            for(Course course : lCourses) {
                if(course.getCourseName().equals(CourseId)) {
                    idCourse = Integer.toString(course.getCourseId());
                }
            }
            mapConditions.put("course", idCourse);

            String idTopic = "0";
            for(Topic topic : lTopics) {
                if(topic.getTopicDescription().equals(TopicId)) {
                    idTopic = Integer.toString(topic.getTopicId());
                }
            }
            mapConditions.put("topic", idTopic);

            String idDifficulty = "0";
            for(Difficulty difficulty : lDifficulties) {
                if(difficulty.getDifficultiesDescription().equals(DifficultyId)) {
                    idDifficulty = Integer.toString(difficulty.getDifficultiesId());
                }
            }
            mapConditions.put("difficulty", idDifficulty);

            String jsonQuestion = jsonParser.makeServiceCall(
                    getString((R.string.update_questions)),
                    ServiceHandler.POST,mapConditions);
            if (jsonQuestion != null) {
                try {
                    JSONObject jsonQuestionObj = new JSONObject(jsonQuestion);
                    if(jsonQuestionObj.getBoolean("success")) {
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

    /**
     * The GetData class to get json data (courses' name, topics' description, difficulties' description)
     * and attach to spinners.
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    private class GetData extends AsyncTask<Void, Void, Void> {
        /**
         * Shows Progress Dialog when getting json data.
         *
         * @author  Phuc Pham N
         * @version 1.0
         * @since   2020-08-05
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching data from database..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Read json data from get_courses and add them to a list of courses.
         * Read json data from get_topics and add them to a list of topics.
         * Read json data from get_difficulties and add them to a list of difficulties.
         *
         * @param arg0 there are no argument
         * @author  Phuc Pham N
         * @version 1.0
         * @since   2020-08-05
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            // Read courses using GET METHOD
            String jsonCourse = jsonParser.makeServiceCall(
                    getString((R.string.get_courses)),
                    ServiceHandler.GET);
            // Read topics using GET METHOD
            String jsonTopic = jsonParser.makeServiceCall(
                    getString((R.string.get_topics)),
                    ServiceHandler.GET);

            if (jsonCourse != null && jsonTopic != null) {
                try {
                    //Convert courses data string to JSON
                    JSONObject jsonCourseObj = new JSONObject(jsonCourse);
                    if (jsonCourseObj != null) {
                        //Get list courses
                        JSONArray courses = jsonCourseObj.getJSONArray("names");
                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject courseObj = (JSONObject) courses.get(i);
                            //Get information a course and add to a list course
                            Course course = new Course(courseObj.getInt("courseid"),
                                    courseObj.getString("coursename"));
                            lCourses.add(course);
                        }
                    }

                    //Convert topics data string to JSON
                    JSONObject jsonTopicObj = new JSONObject(jsonTopic);
                    if (jsonTopicObj != null) {
                        //Get list topics
                        JSONArray topics = jsonTopicObj.getJSONArray("names");
                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject topicObj = (JSONObject) topics.get(i);
                            //Get information a topic and add to a list topic
                            Topic topic = new Topic(topicObj.getInt("topicid"),
                                    topicObj.getString("topicdescription"));
                            lTopics.add(topic);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
            return null;
        }

        /**
         * Finish reading json data and attach them to spinner
         * @param result
         * @author  Phuc Pham N
         * @version 1.0
         * @since   2020-08-05
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            populateSpinner();
        }
    }

    /**
     * Attach data to spinners
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    private void populateSpinner() {
        //Get list course name and attach to course spinner
        List<String> courseNames = new ArrayList<String>();
        courseNames.add("--- Choose Course ---");
        int coursePos = 0;
        for (int i = 0; i < lCourses.size(); i++) {
            if(lCourses.get(i).getCourseName().equals(courseName)) {
                coursePos = i + 1;
            }
            courseNames.add(lCourses.get(i).getCourseName());
        }

        //Get list topic description and attach to topic spinner
        List<String> topicDescriptions = new ArrayList<String>();
        topicDescriptions.add("--- Choose Topic ---");
        int topicPos = 0;
        for (int i = 0; i < lTopics.size(); i++) {
            if(lTopics.get(i).getTopicDescription().equals(topicDescription)) {
                topicPos = i + 1;
            }
            topicDescriptions.add(lTopics.get(i).getTopicDescription());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, courseNames);
        spinnerCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCourses.setAdapter(spinnerCourseAdapter);
        spinnerCourses.setSelection(coursePos);

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerTopicAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, topicDescriptions);
        spinnerTopicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerTopics.setAdapter(spinnerTopicAdapter);
        spinnerTopics.setSelection(topicPos);
    }
}