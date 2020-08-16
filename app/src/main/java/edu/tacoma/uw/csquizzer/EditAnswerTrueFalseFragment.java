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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditAnswerTrueFalseFragment extends Fragment {
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
    private RadioButton radioTrue;
    private RadioButton radioFalse;
    private Button btnUpdateAnswers;
    private Button btnCancel;
    ProgressDialog pDialog;

    public EditAnswerTrueFalseFragment(Context mContext, String mQuestionId, String mQuestionTitle, String mQuestionBody,
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
        View rootView = inflater.inflate(R.layout.fragment_edit_answer_true_false, container, false);
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
        radioTrue = rootView.findViewById(R.id.radio_true);
        radioFalse = rootView.findViewById(R.id.radio_false);
        btnUpdateAnswers = rootView.findViewById(R.id.btn_UpdateAnswers);
        btnUpdateAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idQuestion = tvQuestionId.getText().toString();
                String idSubQuestionTrue = "1";
                String idSubQuestionFalse = "2";
                String textSubQuestionTrue = "True";
                String textSubQuestionFalse = "False";
                String idAnswer = "1";
                String textAnswer = "False";
                if(radioTrue.isChecked()) {
                    textAnswer = "True";
                }
                if((idQuestion.length() != 0)) {
                    EditAnswer task = new EditAnswer(context, idQuestion, type, lastType, idSubQuestionTrue, idSubQuestionFalse,
                            textSubQuestionTrue, textSubQuestionFalse, idAnswer, textAnswer, new EditQuestionFragment.MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                            if (result == true) {
                                Toast.makeText(context, "Update answer successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Update answer unsuccessfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    task.execute();
                } else {
                    Toast.makeText(context, "Please input data for an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelQuestion);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioTrue.setChecked(true);
                radioFalse.setChecked(false);
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

    private class EditAnswer extends AsyncTask<Void, Void, Boolean> {
        private EditQuestionFragment.MyInterface mListener;
        Context context;
        String questionId;
        String typeId;
        String lastTypeId;
        String subQuestionTrueId;
        String subQuestionFalseId;
        String subQuestionTrueText;
        String subQuestionFalseText;
        String answerId;
        String answerText;

        public EditAnswer(Context mContext, String mQuestionId, String mTypeId, String mLastTypeId,
                          String mSubQuestionTrueId, String mSubQuestionFalseId, String mSubQuestionTrueText,
                          String mSubQuestionFalseText, String mAnswerId, String mAnswerText,
                          EditQuestionFragment.MyInterface listener) {
            this.context = mContext;
            this.questionId = mQuestionId;
            if(mTypeId.equals("True/False")) {
                this.typeId = "1";
            } else if(mTypeId.equals("Single Choice")) {
                this.typeId = "2";
            } else if(mTypeId.equals("Multiple Choice")) {
                this.typeId = "3";
            }
            if(mLastTypeId.equals("True/False")) {
                this.lastTypeId = "1";
            } else if(mLastTypeId.equals("Single Choice")) {
                this.lastTypeId = "2";
            } else if(mLastTypeId.equals("Multiple Choice")) {
                this.lastTypeId = "3";
            }
            this.subQuestionTrueId = mSubQuestionTrueId;
            this.subQuestionFalseId = mSubQuestionFalseId;
            this.subQuestionTrueText = mSubQuestionTrueText;
            this.subQuestionFalseText = mSubQuestionFalseText;
            this.answerId = mAnswerId;
            this.answerText = mAnswerText;
            this.mListener  = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                // Delete subquestions and answer in database before storing new subquestions and answer
                Map<String, String> mDeleteSubquestion = new HashMap<>();
                mDeleteSubquestion.put("qid", questionId);
                JSONObject jsonDeleteSubquestion = new JSONObject(jsonParser.makeServiceCall(
                        getString(R.string.delete_subquestions),
                        ServiceHandler.POST,mDeleteSubquestion));
                Map<String, String> mDeleteAnswer = new HashMap<>();
                mDeleteAnswer.put("qid", questionId);
                JSONObject jsonDeleteAnswer = new JSONObject(jsonParser.makeServiceCall(
                        getString(R.string.delete_answers), ServiceHandler.POST,mDeleteAnswer));
                if (jsonDeleteSubquestion.getBoolean("success")
                                                && jsonDeleteAnswer.getBoolean("success")) {
                    Map<String, String> mapUpdateQuestionType = new HashMap<>();
                    mapUpdateQuestionType.put("question", questionId);
                    mapUpdateQuestionType.put("type", typeId);
                    JSONObject jsonUpdateQuestionType = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.update_question_type)),
                            ServiceHandler.POST,mapUpdateQuestionType));
                    Map<String, String> mapConditionSubQuestionTrue = new HashMap<>();
                    mapConditionSubQuestionTrue.put("qid", questionId);
                    mapConditionSubQuestionTrue.put("sid", subQuestionTrueId);
                    mapConditionSubQuestionTrue.put("text", subQuestionTrueText);
                    JSONObject jsonSubQuestionTrue = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mapConditionSubQuestionTrue));
                    Map<String, String> mapConditionSubQuestionFalse = new HashMap<>();
                    mapConditionSubQuestionFalse.put("qid", questionId);
                    mapConditionSubQuestionFalse.put("sid", subQuestionFalseId);
                    mapConditionSubQuestionFalse.put("text", subQuestionFalseText);
                    JSONObject jsonSubQuestionFalse = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mapConditionSubQuestionFalse));
                    Map<String, String> mapConditionAnswer = new HashMap<>();
                    mapConditionAnswer.put("qid", questionId);
                    mapConditionAnswer.put("aid", answerId);
                    if(answerText.equals("True")) {
                        mapConditionAnswer.put("text", "True");
                    } else if (answerText.equals("False")) {
                        mapConditionAnswer.put("text", "False");
                    }

                    JSONObject jsonAnswer = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_answers)),
                            ServiceHandler.POST,mapConditionAnswer));
                    if (jsonUpdateQuestionType.getBoolean("success") && jsonSubQuestionTrue.getBoolean("success")
                            && jsonSubQuestionFalse.getBoolean("success") && jsonAnswer.getBoolean("success")) {
                            return true;
                    }
                } else {
                    // Insert subquestions and answer back to database when storing error
                    Map<String, String> mapUpdateQuestionLastType = new HashMap<>();
                    mapUpdateQuestionLastType.put("question", questionId);
                    mapUpdateQuestionLastType.put("type", lastTypeId);
                    JSONObject jsonUpdateQuestionLastType = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.update_question_type)),
                            ServiceHandler.POST,mapUpdateQuestionLastType));
                    Map<String, String> mInsertSubquestionTrue = new HashMap<>();
                    mInsertSubquestionTrue.put("qid", questionId);
                    mInsertSubquestionTrue.put("sid", Integer.toString(listSubQuestions.get(0).getSubQuestionId()));
                    mInsertSubquestionTrue.put("text", listSubQuestions.get(0).getSubQuestionText());
                    JSONObject jsonInsertSubquestionTrue = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestionTrue));
                    Map<String, String> mInsertSubquestionFalse = new HashMap<>();
                    mInsertSubquestionFalse.put("qid", questionId);
                    mInsertSubquestionFalse.put("sid", Integer.toString(listSubQuestions.get(1).getSubQuestionId()));
                    mInsertSubquestionFalse.put("text", listSubQuestions.get(1).getSubQuestionText());
                    JSONObject jsonInsertSubquestionFalse = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestionFalse));
                    Map<String, String> mInsertAnswer = new HashMap<>();
                    mInsertAnswer.put("qid", questionId);
                    mInsertAnswer.put("aid", Integer.toString(listAnswers.get(0).getAnswerId()));
                    mInsertAnswer.put("aid", listAnswers.get(0).getAnswerText());
                    JSONObject jsonInsertAnswer = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_answers)),
                            ServiceHandler.POST,mInsertAnswer));
                    if (!jsonUpdateQuestionLastType.getBoolean("success") || !jsonInsertSubquestionTrue.getBoolean("success")
                            || !jsonInsertSubquestionFalse.getBoolean("success") || !jsonInsertAnswer.getBoolean("success")) {
                        try {
                            throw new Exception("Storing data into database error!!!");
                        } catch (Exception e) {
                            e.printStackTrace();
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
            populate();
        }
    }

    /**
     * Attach data to spinners
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    private void populate() {
        if(listAnswers.get(0).getAnswerText().equals("True")) {
            radioTrue.setChecked(true);
        } else {
            radioFalse.setChecked(true);
        }
    }
}