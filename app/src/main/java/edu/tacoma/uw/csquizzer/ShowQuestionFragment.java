package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Answer;
import edu.tacoma.uw.csquizzer.model.Question;
import edu.tacoma.uw.csquizzer.model.SubQuestion;

/**
 * The ShowQuestionFragment is placed in MainActivity. It contains a recycler. A recycler contains
 * question id, question title, question body, course name, topic description,
 * difficulty description, type of question (true false or single choice or multiple choice)
 * and "View Answer" Button and "Report" Button
 *
 * When hitting the search button in HomeFragment, it will send conditions such as easy question
 * and Collections topic which a user wants to find to ShowQuestionFragment. Based on these conditions,
 * take all questions that matches to conditions and shows on recycler.
 *
 * On the recycler, a user can report a question or check the correct answer or finish the quiz.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class ShowQuestionFragment extends Fragment {
    View rootView;
    ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private List<Question> lQuestions = new ArrayList<>();

    /**
     * Execute GetQuestions with CourseName or TopicDescription or DifficultyDescription
     * or NumberQuestion conditions which extends AsyncTask to
     *      get question information based on these conditions
     *
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        String[] args = new String[]{getArguments().getString("Course"), getArguments().getString("Topic"),
                getArguments().getString("Difficulty"),getArguments().getString("NumQuestions")};
        new ShowQuestionFragment.GetQuestions().execute(args);
    }

    /**
     * Render components to GUI
     * @param inflater a class used to instantiate layout XML file into its corresponding view objects
     * @param container a special view that can contain other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     * @return view
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show_question, container, false);
        recyclerView = rootView.findViewById(R.id.rv_questions);
        return rootView;
    }

    /**
     * The GetQuestions class to get json data (question id, question title, question body,
     * course name, topic description, difficulty description, type of question) and attach to reclyer.
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    private class GetQuestions extends AsyncTask<String, Void, Void> {
        /**
         * Shows Progress Dialog when getting json data.
         *
         * @author  Phuc Pham N
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
         * Read json data from get_questions to get all questions matching the conditions.
         * For every question, we read
         *      + json data from get_answers based on question id and add them to a list of answers.
         *      + json data from get_subquestions based on question id and add them to a list of subquestions.
         *      + create new question object contains question information and list of answers
         *          and list of subquestions relating to this question.
         *
         * @param arg0 there are 4 arguments
         * @author  Phuc Pham N
         * @since   2020-08-05
         */
        @Override
        protected Void doInBackground(String... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            if(!arg0[0].equals("--- Choose Course ---")) {
                mapConditions.put("course",arg0[0]);
            }
            if(!arg0[1].equals("--- Choose Topic ---")) {
                mapConditions.put("topic",arg0[1]);
            }
            if(!arg0[2].equals("--- Choose Difficulty ---")) {
                mapConditions.put("difficulty",arg0[2]);
            }
            if(!arg0[3].equals("--- Choose Number Questions ---")) {
                mapConditions.put("limit",arg0[3]);
            } else {
                mapConditions.put("limit","20");
            }
            String jsonQuestion = jsonParser.makeServiceCall(
                                    getString((R.string.get_questions)), ServiceHandler.GET,mapConditions);
            if (jsonQuestion != null) {
                try {
                    JSONObject jsonQuestionObj = new JSONObject(jsonQuestion);
                    JSONArray questions = jsonQuestionObj.getJSONArray("questions");
                    for (int i = 0; i < questions.length(); i++) {
                        List<Answer> answersList = new ArrayList<>();
                        List<SubQuestion> subQuestionsList = new ArrayList<>();
                        JSONObject questionObj = (JSONObject) questions.get(i);
                        Map<String, String> qid = new HashMap<>();
                        int questionId = Integer.parseInt(questionObj.getString("questionid"));
                        qid.put("qid", Integer.toString( questionId));
                        String jsonAnswer = jsonParser.makeServiceCall(
                                getString((R.string.get_answers)),
                                ServiceHandler.GET, qid);
                        if (jsonAnswer != null) {
                            try {
                                JSONObject jsonAnswerObj = new JSONObject(jsonAnswer);
                                JSONArray ans = jsonAnswerObj.getJSONArray("answers");
                                for (int j = 0; j < ans.length(); j++) {
                                    JSONObject answerObj = (JSONObject) ans.get(j);
                                    Answer ansobj = new Answer(answerObj.getInt("answerid"),
                                            questionId, answerObj.getString("answertext"));
                                    answersList.add(ansobj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }

                        String jsonSubQuestion = jsonParser.makeServiceCall(
                                getString((R.string.get_subquestions)),
                                ServiceHandler.GET, qid);

                        if (jsonSubQuestion != null) {
                            try {
                                JSONObject jsonSubQuestionObj = new JSONObject(jsonSubQuestion);
                                JSONArray subs = jsonSubQuestionObj
                                        .getJSONArray("subquestions");
                                for (int k = 0; k < subs.length(); k++) {
                                    JSONObject subQObj = (JSONObject) subs.get(k);
                                    SubQuestion subqobj = new SubQuestion(subQObj.getInt("subquestionid"),
                                            questionId, subQObj.getString("subquestiontext"));
                                    subQuestionsList.add(subqobj);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            break;
                        }
                        Question question = new Question(questionId,
                                questionObj.getString("questiontitle"),
                                questionObj.getString("questionbody"),
                                questionObj.getString("coursename"),
                                questionObj.getString("topicdescription"),
                                questionObj.getString("difficultydescription"),
                                questionObj.getString("typedescription"),
                                answersList, subQuestionsList);
                        lQuestions.add(question);
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
         * Finish reading json data and attach them to recyler
         * @param result the result
         * @author  Phuc Pham N
         * @since   2020-08-05
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            loadQuestions();
        }
    }

    /**
     * Attach data to cycler
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    private void loadQuestions() {
        QuestionAdapter adapter = new QuestionAdapter(getActivity(), lQuestions);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemViewCacheSize(10);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

}