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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import edu.tacoma.uw.csquizzer.model.SubQuestion;

/**
 * The purpose of EditAnswerSingleChoiceFragment module is to edit answers of a question
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-17
 */
public class EditAnswerSingleChoiceFragment extends Fragment {
    private Context context;
    private String questionId;
    private String questionTitle;
    private String questionBody;
    private String type;
    private String lastType;
    private List<Answer> listAnswers = new ArrayList<>();
    private List<SubQuestion> listSubQuestions = new ArrayList<>();
    private ImageButton tvBackToList;
    private TextView tvQuestionId;
    private TextView tvQuestionTitle;
    private TextView tvQuestionBody;
    private EditText etAnswer1;
    private EditText etAnswer2;
    private EditText etAnswer3;
    private EditText etAnswer4;
    private RadioButton radioAnswer1;
    private RadioButton radioAnswer2;
    private RadioButton radioAnswer3;
    private RadioButton radioAnswer4;
    private Button btnUpdateAnswers;
    private Button btnCancel;
    ProgressDialog pDialog;

    public EditAnswerSingleChoiceFragment(Context mContext, String mQuestionId, String mQuestionTitle,
                                          String mQuestionBody, String mType, String mLastType) {
        this.context = mContext;
        this.questionId = mQuestionId;
        this.questionTitle = mQuestionTitle;
        this.questionBody = mQuestionBody;
        this.type = mType;
        this.lastType = mLastType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(lastType.equals(type)) {
            new GetData().execute();
        }
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
        View rootView = inflater.inflate(R.layout.fragment_edit_answer_single_choice, container, false);
        tvQuestionId = rootView.findViewById(R.id.tv_QuestionId);
        tvQuestionId.setText(questionId);
        tvQuestionTitle = rootView.findViewById(R.id.tv_QuestionTitle);
        tvQuestionTitle.setText(questionTitle);
        tvQuestionBody = rootView.findViewById(R.id.tv_QuestionBody);
        tvQuestionBody.setText(questionBody);
        etAnswer1 = rootView.findViewById(R.id.et_answer_1);
        etAnswer2 = rootView.findViewById(R.id.et_answer_2);
        etAnswer3 = rootView.findViewById(R.id.et_answer_3);
        etAnswer4 = rootView.findViewById(R.id.et_answer_4);
        radioAnswer1 = rootView.findViewById(R.id.radio_answer_1);
        radioAnswer2 = rootView.findViewById(R.id.radio_answer_2);
        radioAnswer3 = rootView.findViewById(R.id.radio_answer_3);
        radioAnswer4 = rootView.findViewById(R.id.radio_answer_4);
        btnUpdateAnswers = rootView.findViewById(R.id.btn_UpdateAnswers);
        btnUpdateAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String idQuestion = tvQuestionId.getText().toString();
            String idSubQuestion1 = "1";
            String idSubQuestion2 = "2";
            String idSubQuestion3 = "3";
            String idSubQuestion4 = "4";
            String textSubQuestion1 = etAnswer1.getText().toString();
            String textSubQuestion2 = etAnswer2.getText().toString();
            String textSubQuestion3 = etAnswer3.getText().toString();
            String textSubQuestion4 = etAnswer4.getText().toString();;
            String idAnswer = "1";
            String textAnswer = radioAnswer1.getText().toString();
            if(radioAnswer2.isChecked()) {
                textAnswer = radioAnswer2.getText().toString();
            } else if(radioAnswer3.isChecked()) {
                textAnswer = radioAnswer3.getText().toString();
            } else if (radioAnswer4.isChecked()) {
                textAnswer = radioAnswer4.getText().toString();
            }
            if((idQuestion.length() != 0) && (textSubQuestion1.length() != 0)
                    && (textSubQuestion2.length() != 0) && (textSubQuestion3.length() != 0)
                    && (textSubQuestion4.length() != 0) && (textAnswer.length() != 0)) {
                List<String> subQuestions = new ArrayList<>();
                subQuestions.add(textSubQuestion1);
                subQuestions.add(textSubQuestion2);
                subQuestions.add(textSubQuestion3);
                subQuestions.add(textSubQuestion4);
                if(checkUniqueInput(subQuestions)) {
                    EditAnswer task = new EditAnswer(context, idQuestion, type, lastType,
                            idSubQuestion1, idSubQuestion2, idSubQuestion3, idSubQuestion4,
                            textSubQuestion1, textSubQuestion2, textSubQuestion3, textSubQuestion4,
                            idAnswer, textAnswer, new MyInterface() {
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
                }else {
                    Toast.makeText(context, "Please input unique answers", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Please input data for an answer", Toast.LENGTH_SHORT).show();
            }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelQuestion);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAnswer1.setText("");
                etAnswer2.setText("");
                etAnswer3.setText("");
                etAnswer4.setText("");
                radioAnswer1.setChecked(true);
                radioAnswer2.setChecked(false);
                radioAnswer3.setChecked(false);
                radioAnswer3.setChecked(false);
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
     * The EditAnswer AsyncTask to edit answers in database
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-17
     */
    private class EditAnswer extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String questionId;
        String typeId;
        String lastTypeId;
        String subQuestion1Id;
        String subQuestion2Id;
        String subQuestion3Id;
        String subQuestion4Id;
        String subQuestion1Text;
        String subQuestion2Text;
        String subQuestion3Text;
        String subQuestion4Text;
        String answerId;
        String answerText;

        public EditAnswer(Context mContext, String mQuestionId, String mTypeId, String mLastTypeId,
                          String mSubQuestion1Id, String mSubQuestion2Id, String mSubQuestion3Id,
                          String mSubQuestion4Id, String mSubQuestion1Text, String mSubQuestion2Text,
                          String mSubQuestion3Text, String mSubQuestion4Text, String mAnswerId,
                          String mAnswerText, MyInterface listener) {
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
            this.subQuestion1Id = mSubQuestion1Id;
            this.subQuestion2Id = mSubQuestion2Id;
            this.subQuestion3Id = mSubQuestion3Id;
            this.subQuestion4Id = mSubQuestion4Id;
            this.subQuestion1Text = mSubQuestion1Text;
            this.subQuestion2Text = mSubQuestion2Text;
            this.subQuestion3Text = mSubQuestion3Text;
            this.subQuestion4Text = mSubQuestion4Text;
            this.answerId = mAnswerId;
            this.answerText = mAnswerText;
            this.mListener = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            // Delete subquestions and answer in database before storing new subquestions and answer
            try {
                Map<String, String> mDeleteSubquestion = new HashMap<>();
                mDeleteSubquestion.put("qid", questionId);
                JSONObject jsonDeleteSubquestion = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.delete_subquestions)),
                        ServiceHandler.POST, mDeleteSubquestion));
                Map<String, String> mDeleteAnswer = new HashMap<>();
                mDeleteAnswer.put("qid", questionId);
                JSONObject jsonDeleteAnswer = new JSONObject(jsonParser.makeServiceCall(
                        getString((R.string.delete_answers)),
                        ServiceHandler.POST, mDeleteAnswer));
                if (jsonDeleteSubquestion.getBoolean("success")
                                                && jsonDeleteAnswer.getBoolean("success")) {
                    try {
                        Map<String, String> mUpdateQuestionType = new HashMap<>();
                        mUpdateQuestionType.put("question", questionId);
                        mUpdateQuestionType.put("type", typeId);
                        JSONObject jsonUpdateQuestionType = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.update_question_type)),
                                ServiceHandler.POST, mUpdateQuestionType));

                        Map<String, String> mapConditionSubQuestion1 = new HashMap<>();
                        mapConditionSubQuestion1.put("qid", questionId);
                        mapConditionSubQuestion1.put("sid", subQuestion1Id);
                        mapConditionSubQuestion1.put("text", subQuestion1Text);
                        JSONObject jsonSubQuestion1 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)),
                                ServiceHandler.POST, mapConditionSubQuestion1));

                        Map<String, String> mapConditionSubQuestion2 = new HashMap<>();
                        mapConditionSubQuestion2.put("qid", questionId);
                        mapConditionSubQuestion2.put("sid", subQuestion2Id);
                        mapConditionSubQuestion2.put("text", subQuestion2Text);
                        JSONObject jsonSubQuestion2 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)),
                                ServiceHandler.POST, mapConditionSubQuestion2));

                        Map<String, String> mapConditionSubQuestion3 = new HashMap<>();
                        mapConditionSubQuestion3.put("qid", questionId);
                        mapConditionSubQuestion3.put("sid", subQuestion3Id);
                        mapConditionSubQuestion3.put("text", subQuestion3Text);
                        JSONObject jsonSubQuestion3 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)),
                                ServiceHandler.POST, mapConditionSubQuestion3));

                        Map<String, String> mapConditionSubQuestion4 = new HashMap<>();
                        mapConditionSubQuestion4.put("qid", questionId);
                        mapConditionSubQuestion4.put("sid", subQuestion4Id);
                        mapConditionSubQuestion4.put("text", subQuestion4Text);
                        JSONObject jsonSubQuestion4 = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_subquestions)),
                                ServiceHandler.POST, mapConditionSubQuestion4));

                        Map<String, String> mapConditionAnswer = new HashMap<>();
                        mapConditionAnswer.put("qid", questionId);
                        mapConditionAnswer.put("aid", answerId);
                        if(answerText.equals("Answer 1")) {
                            mapConditionAnswer.put("text", subQuestion1Text);
                        } else if (answerText.equals("Answer 2")) {
                            mapConditionAnswer.put("text", subQuestion2Text);
                        } else if (answerText.equals("Answer 3")) {
                            mapConditionAnswer.put("text", subQuestion3Text);
                        } else if (answerText.equals("Answer 4")) {
                            mapConditionAnswer.put("text", subQuestion4Text);
                        }

                        JSONObject jsonAnswer = new JSONObject(jsonParser.makeServiceCall(
                                getString((R.string.add_answers)),
                                ServiceHandler.POST, mapConditionAnswer));
                        if(jsonUpdateQuestionType.getBoolean("success")
                                && jsonSubQuestion1.getBoolean("success")
                                && jsonSubQuestion2.getBoolean("success")
                                && jsonSubQuestion3.getBoolean("success")
                                && jsonSubQuestion4.getBoolean("success")
                                && jsonAnswer.getBoolean("success")) {
                            return true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    // Insert subquestions and answer back to database when storing error
                    Map<String, String> mapUpdateQuestionLastType = new HashMap<>();
                    mapUpdateQuestionLastType.put("question", questionId);
                    mapUpdateQuestionLastType.put("type", lastTypeId);
                    JSONObject jsonUpdateQuestionLastType = new JSONObject(jsonParser.makeServiceCall(
                            getString(R.string.update_question_type),
                            ServiceHandler.POST,mapUpdateQuestionLastType));
                    Map<String, String> mInsertSubquestion1 = new HashMap<>();
                    mInsertSubquestion1.put("qid", questionId);
                    mInsertSubquestion1.put("sid", Integer.toString(listSubQuestions.get(0).getSubQuestionId()));
                    mInsertSubquestion1.put("text", listSubQuestions.get(0).getSubQuestionText());
                    JSONObject jsonInsertSubquestion1 = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestion1));
                    Map<String, String> mInsertSubquestion2 = new HashMap<>();
                    mInsertSubquestion2.put("qid", questionId);
                    mInsertSubquestion2.put("sid", Integer.toString(listSubQuestions.get(1).getSubQuestionId()));
                    mInsertSubquestion2.put("text", listSubQuestions.get(1).getSubQuestionText());
                    JSONObject jsonInsertSubquestion2 = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestion2));
                    Map<String, String> mInsertSubquestion3 = new HashMap<>();
                    mInsertSubquestion3.put("qid", questionId);
                    mInsertSubquestion3.put("sid", Integer.toString(listSubQuestions.get(2).getSubQuestionId()));
                    mInsertSubquestion3.put("text", listSubQuestions.get(2).getSubQuestionText());
                    JSONObject jsonInsertSubquestion3 = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestion3));
                    Map<String, String> mInsertSubquestion4 = new HashMap<>();
                    mInsertSubquestion4.put("qid", questionId);
                    mInsertSubquestion4.put("sid", Integer.toString(listSubQuestions.get(3).getSubQuestionId()));
                    mInsertSubquestion4.put("text", listSubQuestions.get(3).getSubQuestionText());
                    JSONObject jsonInsertSubquestion4 = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_subquestions)),
                            ServiceHandler.POST,mInsertSubquestion4));
                    Map<String, String> mInsertAnswer = new HashMap<>();
                    mInsertAnswer.put("qid", questionId);
                    mInsertAnswer.put("aid", Integer.toString(listAnswers.get(0).getAnswerId()));
                    mInsertAnswer.put("aid", listAnswers.get(0).getAnswerText());
                    JSONObject jsonInsertAnswer = new JSONObject(jsonParser.makeServiceCall(
                            getString((R.string.add_answers)),
                            ServiceHandler.POST,mInsertAnswer));
                    if (jsonUpdateQuestionLastType.getBoolean("success")
                            || jsonInsertSubquestion1.getBoolean("success")
                            || jsonInsertSubquestion2.getBoolean("success")
                            || jsonInsertSubquestion3.getBoolean("success")
                            || jsonInsertSubquestion4.getBoolean("success")
                            || jsonInsertAnswer.getBoolean("success")) {
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
     * Check unique answers
     * @param inputStrings list of answers
     * @return
     */
    private boolean checkUniqueInput(List<String> inputStrings) {
        boolean checkUniqueness = true;
        for(int i = 0; i < inputStrings.size() - 1; i++) {
            for (int j = i + 1; j < inputStrings.size(); j++) {
                if(inputStrings.get(i).equals(inputStrings.get(j))) {
                    checkUniqueness = false;
                    break;
                }
            }
        }
        return checkUniqueness;
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
                    getString((R.string.get_subquestions)),
                    ServiceHandler.GET, mapConditions);
            // Read topics using GET METHOD
            String jsonAnswer = jsonParser.makeServiceCall(
                    getString((R.string.get_answers)),
                    ServiceHandler.GET, mapConditions);
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
            radioAnswer1.setChecked(true);
        } else if(listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(1).getSubQuestionText())) {
            radioAnswer2.setChecked(true);
        } else if(listAnswers.get(0).getAnswerText().equals(listSubQuestions.get(2).getSubQuestionText())) {
            radioAnswer3.setChecked(true);
        } else {
            radioAnswer4.setChecked(true);
        }
    }
}