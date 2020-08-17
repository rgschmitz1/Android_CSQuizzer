package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

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


public class AddQuestionMultipleChoiceFragment extends Fragment {
    private ImageButton tvBackToList;
    private EditText etQuestionTitle;
    private EditText etQuestionBody;
    private Spinner spinnerCourses;
    private Spinner spinnerTopics;
    private Spinner spinnerDifficulties;
    private Button btnAddQuestion;
    private Button btnCancel;
    private EditText etAnswer1;
    private EditText etAnswer2;
    private EditText etAnswer3;
    private EditText etAnswer4;
    private CheckBox cbAnswer1;
    private CheckBox cbAnswer2;
    private CheckBox cbAnswer3;
    private CheckBox cbAnswer4;
    private String typeId;
    private Context context;
    private List<String> lQuestionTitle;
    private List<String> lAnswers;
    private List<Course> lCourses;
    private List<Topic> lTopics;
    private List<Difficulty> lDifficulties;
    ProgressDialog pDialog;

    public AddQuestionMultipleChoiceFragment(Context mContext, String mTypeId) {
        this.context = mContext;
        this.typeId = mTypeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Store list courses, topics, difficulty
        lQuestionTitle = new ArrayList<>();
        lAnswers = new ArrayList<>();
        lCourses = new ArrayList<>();
        lTopics = new ArrayList<>();
        lDifficulties = new ArrayList<>();
        lDifficulties.add(new Difficulty(1, "Easy"));
        lDifficulties.add(new Difficulty(2, "Medium"));
        lDifficulties.add(new Difficulty(3, "Hard"));
        new GetData().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_question_multiple_choice, container, false);
        // get spinner information on GUI
        spinnerCourses = (Spinner) rootView.findViewById(R.id.sn_courses);
        spinnerTopics = (Spinner) rootView.findViewById(R.id.sn_topics);
        spinnerDifficulties = (Spinner) rootView.findViewById(R.id.sn_difficulties);
        etQuestionTitle = rootView.findViewById(R.id.et_QuestionTitle);
        etQuestionBody = rootView.findViewById(R.id.et_QuestionBody);
        etAnswer1 = rootView.findViewById(R.id.et_answer_1);
        etAnswer2 = rootView.findViewById(R.id.et_answer_2);
        etAnswer3 = rootView.findViewById(R.id.et_answer_3);
        etAnswer4 = rootView.findViewById(R.id.et_answer_4);
        cbAnswer1 = rootView.findViewById(R.id.cb_subquestion_1);
        cbAnswer2 = rootView.findViewById(R.id.cb_subquestion_2);
        cbAnswer3 = rootView.findViewById(R.id.cb_subquestion_3);
        cbAnswer4 = rootView.findViewById(R.id.cb_subquestion_4);
        btnAddQuestion = rootView.findViewById(R.id.btn_AddQuestion);
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String titleQuestion = etQuestionTitle.getText().toString();
                boolean checkTitleMatch = true;
                for (String title : lQuestionTitle) {
                    if(title.equals(titleQuestion)) {
                        checkTitleMatch = false;
                        break;
                    }
                }
                final String bodyQuestion = etQuestionBody.getText().toString();
                final String getAnswer1 = etAnswer1.getText().toString();
                final String getAnswer2 = etAnswer2.getText().toString();
                final String getAnswer3 = etAnswer3.getText().toString();
                final String getAnswer4 = etAnswer4.getText().toString();
                final String getCourseName = spinnerCourses.getSelectedItem().toString();
                final String getTopicDescription = spinnerTopics.getSelectedItem().toString();
                final String getDifficultyDescription = spinnerDifficulties.getSelectedItem().toString();
                if(cbAnswer1.isChecked()) {
                    lAnswers.add(etAnswer1.getText().toString());
                }
                if (cbAnswer2.isChecked()) {
                    lAnswers.add(etAnswer2.getText().toString());
                }
                if (cbAnswer3.isChecked()) {
                    lAnswers.add(etAnswer3.getText().toString());
                }
                if (cbAnswer4.isChecked()) {
                    lAnswers.add(etAnswer4.getText().toString());
                }
                if(checkTitleMatch) {
                    if((titleQuestion.length() != 0) && (bodyQuestion.length() != 0)
                            && (getAnswer1.length() != 0) && (getAnswer2.length() != 0)
                            && (getAnswer3.length() != 0) && (getAnswer4.length() != 0)
                            && !getCourseName.equals("--- Choose Course ---")
                            && !getTopicDescription.equals("--- Choose Topic ---")
                            && !getDifficultyDescription.equals("--- Choose Difficulty ---")) {
                        String idCourse = "";
                        for (Course course : lCourses) {
                            if(course.getCourseName().equals(getCourseName)) {
                                idCourse = Integer.toString(course.getCourseId());
                            }
                        }
                        String idTopic = "";
                        for (Topic topic : lTopics) {
                            if(topic.getTopicDescription().equals(getTopicDescription)) {
                                idTopic = Integer.toString(topic.getTopicId());
                            }
                        }
                        String idDifficulty = "1";
                        if(getDifficultyDescription.equals("Medium")) {
                            idDifficulty = "2";
                        } else if (getDifficultyDescription.equals("Hard")) {
                            idDifficulty = "3";
                        }
                        AddQuestion task = new AddQuestion(context, titleQuestion, bodyQuestion,
                                getAnswer1, getAnswer2, getAnswer3, getAnswer4,
                                idCourse, idTopic, idDifficulty, typeId, lAnswers,
                                new MyInterface() {
                                    @Override
                                    public void myMethod(boolean result) {
                                        if (result == true) {
                                            Toast.makeText(context, "Add question successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Add question unsuccessfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        task.execute();
                    } else {
                        Toast.makeText(context, "Please input data for a question", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Title question does exist in database", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelQuestion);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etQuestionTitle.setText("");
                etQuestionBody.setText("");
                etAnswer1.setText("");
                etAnswer2.setText("");
                etAnswer3.setText("");
                etAnswer4.setText("");
                spinnerCourses.setSelection(0);
                spinnerTopics.setSelection(0);
                spinnerDifficulties.setSelection(0);
                cbAnswer1.setChecked(false);
                cbAnswer2.setChecked(false);
                cbAnswer3.setChecked(false);
                cbAnswer4.setChecked(false);
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

    private class AddQuestion extends AsyncTask<Void, Void, Boolean> {
        private MyInterface listener;
        Context context;
        String questionTitle;
        String questionBody;
        String answer1;
        String answer2;
        String answer3;
        String answer4;
        String courseId;
        String topicId;
        String difficultyId;
        String typeId;
        List<String> lAnswers = new ArrayList<>();

        public AddQuestion(Context mContext, String mQuestionTitle,  String mQuestionBody,
                           String mAnswer1, String mAnswer2, String mAnswer3, String mAnswer4,
                           String mCourseId, String mTopicId, String mDifficultyId,
                           String mTypeId, List<String> mLAnswers, MyInterface mListener) {
            this.context = mContext;
            this.questionTitle = mQuestionTitle;
            this.questionBody = mQuestionBody;
            this.answer1 = mAnswer1;
            this.answer2 = mAnswer2;
            this.answer3 = mAnswer3;
            this.answer4 = mAnswer4;
            this.courseId = mCourseId;
            this.topicId = mTopicId;
            this.difficultyId = mDifficultyId;
            this.typeId = mTypeId;
            this.lAnswers = mLAnswers;
            this.listener  = mListener;
        }



        @Override
        protected Boolean doInBackground(Void... args) {
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                Map<String, String> mapConditions = new HashMap<>();
                mapConditions.put("title", questionTitle);
                mapConditions.put("body", questionBody);
                mapConditions.put("course", courseId);
                mapConditions.put("topic", topicId);
                mapConditions.put("difficulty", difficultyId);
                mapConditions.put("type", typeId);
                JSONObject jsonQuestion = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.add_questions)), ServiceHandler.POST,mapConditions));
                if(jsonQuestion.getBoolean("success")) {
                    Map<String, String> mQuestionTitle = new HashMap<>();
                    mQuestionTitle.put("title", questionTitle);
                    JSONObject jsonQuestionId = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.list_questions)), ServiceHandler.GET,mQuestionTitle));
                    if(jsonQuestionId.getBoolean("success")) {
                        String idQuestion = ((JSONObject)jsonQuestionId
                                .getJSONArray("names")
                                .get(0)).getString("questionid");
                        Map<String, String> mAnswer1 = new HashMap<>();
                        mAnswer1.put("qid", idQuestion);
                        mAnswer1.put("sid", "1");
                        mAnswer1.put("text", answer1);
                        JSONObject jsonAddAnswer1 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)), ServiceHandler.POST, mAnswer1));
                        Map<String, String> mAnswer2 = new HashMap<>();
                        mAnswer2.put("qid", idQuestion);
                        mAnswer2.put("sid", "2");
                        mAnswer2.put("text", answer2);
                        JSONObject jsonAddAnswer2 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)), ServiceHandler.POST, mAnswer2));
                        Map<String, String> mAnswer3 = new HashMap<>();
                        mAnswer3.put("qid", idQuestion);
                        mAnswer3.put("sid", "3");
                        mAnswer3.put("text", answer3);
                        JSONObject jsonAddAnswer3 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)), ServiceHandler.POST, mAnswer3));
                        Map<String, String> mAnswer4 = new HashMap<>();
                        mAnswer4.put("qid", idQuestion);
                        mAnswer4.put("sid", "4");
                        mAnswer4.put("text", answer4);
                        JSONObject jsonAddAnswer4 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)), ServiceHandler.POST, mAnswer4));
                        List<Boolean> successInsert = new ArrayList<>();
                        for(int i = 0; i < lAnswers.size(); i++) {
                            Map<String, String> mAnswer = new HashMap<>();
                            mAnswer.put("qid", idQuestion);
                            mAnswer.put("aid", Integer.toString(i+1));
                            mAnswer.put("text", lAnswers.get(i));
                            JSONObject jsonAddAnswer = new JSONObject(jsonParser.makeServiceCall(
                                    getString((R.string.add_answers)), ServiceHandler.POST, mAnswer));
                            successInsert.add(jsonAddAnswer.getBoolean("success"));
                        }

                        boolean checkInsertAnswersSuccess = true;
                        for (int i = 0; i < successInsert.size();i++) {
                            if(successInsert.get(i) == false) {
                                checkInsertAnswersSuccess = false;
                            }
                        }

                        if(jsonAddAnswer1.getBoolean("success")
                                && jsonAddAnswer2.getBoolean("success")
                                && jsonAddAnswer3.getBoolean("success")
                                && jsonAddAnswer4.getBoolean("success")
                                && checkInsertAnswersSuccess) {
                            return true;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (listener != null)
                listener.myMethod(result);
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
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                //Read question titles using
                JSONObject jsonQuestion = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.list_questions)), ServiceHandler.GET));
                // Read courses using GET METHOD
                JSONObject jsonCourse = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.get_courses)), ServiceHandler.GET));
                // Read topics using GET METHOD
                JSONObject jsonTopic = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.get_topics)), ServiceHandler.GET));
                if (jsonQuestion.getBoolean("success") && jsonCourse.getBoolean("success")
                        && jsonTopic.getBoolean("success")) {
                    //Get list courses
                    JSONArray questions = jsonQuestion.getJSONArray("names");
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject questionObj = (JSONObject) questions.get(i);
                        //Get information a course and add to a list course
                        lQuestionTitle.add(questionObj.getString("questiontitle"));
                    }

                    //Get list courses
                    JSONArray courses = jsonCourse.getJSONArray("names");
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject courseObj = (JSONObject) courses.get(i);
                        //Get information a course and add to a list course
                        Course course = new Course(courseObj.getInt("courseid"),
                                courseObj.getString("coursename"));
                        lCourses.add(course);
                    }

                    //Get list topics
                    JSONArray topics = jsonTopic.getJSONArray("names");
                    for (int i = 0; i < topics.length(); i++) {
                        JSONObject topicObj = (JSONObject) topics.get(i);
                        //Get information a topic and add to a list topic
                        Topic topic = new Topic(topicObj.getInt("topicid"),
                                topicObj.getString("topicdescription"));
                        lTopics.add(topic);
                    }
                }
            } catch (JSONException e) {
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
        for (int i = 0; i < lCourses.size(); i++) {
            courseNames.add(lCourses.get(i).getCourseName());
        }

        //Get list topic description and attach to topic spinner
        List<String> topicDescriptions = new ArrayList<String>();
        topicDescriptions.add("--- Choose Topic ---");
        for (int i = 0; i < lTopics.size(); i++) {
            topicDescriptions.add(lTopics.get(i).getTopicDescription());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, courseNames);
        spinnerCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCourses.setAdapter(spinnerCourseAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerTopicAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, topicDescriptions);
        spinnerTopicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerTopics.setAdapter(spinnerTopicAdapter);
    }
}