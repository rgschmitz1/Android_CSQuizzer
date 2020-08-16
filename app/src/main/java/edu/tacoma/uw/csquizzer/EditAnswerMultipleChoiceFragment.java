package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Answer;
import edu.tacoma.uw.csquizzer.model.Course;
import edu.tacoma.uw.csquizzer.model.Difficulty;
import edu.tacoma.uw.csquizzer.model.SubQuestion;
import edu.tacoma.uw.csquizzer.model.Topic;
import edu.tacoma.uw.csquizzer.model.Type;

public class EditAnswerMultipleChoiceFragment extends Fragment {

    private Context context;
    private String questionId;
    private String questionTitle;
    private String questionBody;
    private String course;
    private String topic;
    private String difficulty;
    private String type;
    private String lastType;
    private List<Course> listCourses = new ArrayList<>();
    private List<Topic> listTopics = new ArrayList<>();
    private List<Difficulty> listDifficulties = new ArrayList<>();
    private List<Type> listTypes = new ArrayList<>();
    private List<Answer> listAnswers = new ArrayList<>();
    private List<SubQuestion> listSubQuestions = new ArrayList<>();
    private ImageButton tvBackToList;
    private TextView tvQuestionId;
    private TextView tvQuestionTitle;
    private TextView tvQuestionBody;
    private TextView tvCourseName;
    private TextView tvTopicDescription;
    private TextView tvDifficultyDescription;
    private TextView tvTypeDescription;
    private EditText etAnswer1;
    private EditText etAnswer2;
    private EditText etAnswer3;
    private EditText etAnswer4;
    private CheckBox cbAnswer1;
    private CheckBox cbAnswer2;
    private CheckBox cbAnswer3;
    private CheckBox cbAnswer4;
    private Button btnUpdateAnswers;
    private Button btnCancel;
    ProgressDialog pDialog;

    public EditAnswerMultipleChoiceFragment(Context mContext, String mQuestionId, String mQuestionTitle, String mQuestionBody,
                                            String mCourse, String mTopic, String mDifficulty, String mType, String mLastType,
                                            List<Course> lCourses, List<Topic> lTopics, List<Difficulty> lDifficulties,
                                            List<Type> lTypes) {
        this.context = mContext;
        this.questionId = mQuestionId;
        this.questionTitle = mQuestionTitle;
        this.questionBody = mQuestionBody;
        this.course = mCourse;
        this.topic = mTopic;
        this.difficulty = mDifficulty;
        this.type = mType;
        this.lastType = mLastType;
        this.listCourses = lCourses;
        this.listTopics = lTopics;
        this.listDifficulties = lDifficulties;
        this.listTypes = lTypes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(lastType.equals(type)) {
            new GetData().execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_edit_answer_multiple_choice, container, false);
        tvQuestionId = rootView.findViewById(R.id.tv_QuestionId);
        tvQuestionId.setText(questionId);
        tvQuestionTitle = rootView.findViewById(R.id.tv_QuestionTitle);
        tvQuestionTitle.setText(questionTitle);
        tvQuestionBody = rootView.findViewById(R.id.tv_QuestionBody);
        tvQuestionBody.setText(questionBody);
        tvCourseName = rootView.findViewById(R.id.tv_CourseName);
        tvCourseName.setText(course);
        tvTopicDescription = rootView.findViewById(R.id.tv_TopicDescription);
        tvTopicDescription.setText(topic);
        tvDifficultyDescription = rootView.findViewById(R.id.tv_DifficultyDescription);
        tvDifficultyDescription.setText(difficulty);
        tvTypeDescription = rootView.findViewById(R.id.tv_TypeDescription);
        tvTypeDescription.setText(type);
        etAnswer1 = rootView.findViewById(R.id.et_answer_1);
        etAnswer2 = rootView.findViewById(R.id.et_answer_2);
        etAnswer3 = rootView.findViewById(R.id.et_answer_3);
        etAnswer4 = rootView.findViewById(R.id.et_answer_4);
        cbAnswer1 = rootView.findViewById(R.id.cb_subquestion_1);
        cbAnswer2 = rootView.findViewById(R.id.cb_subquestion_2);
        cbAnswer3 = rootView.findViewById(R.id.cb_subquestion_3);
        cbAnswer4 = rootView.findViewById(R.id.cb_subquestion_4);
        btnUpdateAnswers = rootView.findViewById(R.id.btn_UpdateAnswers);
        btnUpdateAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelQuestion);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvBackToList = rootView.findViewById(R.id.imb_back_to_list);
        tvBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return rootView;
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
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("qid", questionId);
            // Read courses using GET METHOD
            String jsonSubquestions = jsonParser.makeServiceCall(
                    getString((R.string.get_subquestions)), ServiceHandler.GET, mapConditions);
            // Read topics using GET METHOD
            String jsonAnswer = jsonParser.makeServiceCall(
                    getString((R.string.get_answers)), ServiceHandler.GET, mapConditions);
            if (jsonSubquestions != null && jsonAnswer != null) {
                try {
                    //Convert courses data string to JSON
                    JSONObject jsonSubquestionObj = new JSONObject(jsonSubquestions);
                    if (jsonSubquestionObj != null) {
                        //Get list courses
                        JSONArray subquestions = jsonSubquestionObj.getJSONArray("subquestions");
                        for (int i = 0; i < subquestions.length(); i++) {
                            JSONObject subquestionObj = (JSONObject) subquestions.get(i);
                            //Get information a course and add to a list course
                            SubQuestion subQuestion = new SubQuestion(
                                    subquestionObj.getInt("subquestionid"),
                                    Integer.parseInt(questionId),
                                    subquestionObj.getString("subquestiontext"));
                            listSubQuestions.add(subQuestion);
                        }
                    }

                    //Convert topics data string to JSON
                    JSONObject jsonAnswerObj = new JSONObject(jsonAnswer);
                    if (jsonAnswerObj != null) {
                        //Get list topics
                        JSONArray answers = jsonAnswerObj.getJSONArray("answers");
                        for (int i = 0; i < answers.length(); i++) {
                            JSONObject answerObj = (JSONObject) answers.get(i);
                            //Get information a topic and add to a list topic
                            Answer answer = new Answer(
                                    answerObj.getInt("answerid"),
                                    Integer.parseInt(questionId),
                                    answerObj.getString("answertext"));
                            listAnswers.add(answer);
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
        etAnswer1.setText(listSubQuestions.get(0).getSubQuestionText());
        etAnswer2.setText(listSubQuestions.get(1).getSubQuestionText());
        etAnswer3.setText(listSubQuestions.get(2).getSubQuestionText());
        etAnswer4.setText(listSubQuestions.get(3).getSubQuestionText());
        if(listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(0).getSubQuestionText())) {
            cbAnswer1.setChecked(true);
        }

        if(listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(1).getSubQuestionText())) {
            cbAnswer2.setChecked(true);
        }

        if(listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(2).getSubQuestionText())) {
            cbAnswer3.setChecked(true);
        }

        if (listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(3).getSubQuestionText())) {
            cbAnswer4.setChecked(true);
        }
    }
}