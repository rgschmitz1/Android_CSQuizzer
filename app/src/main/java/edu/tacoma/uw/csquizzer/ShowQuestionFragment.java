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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Answer;
import edu.tacoma.uw.csquizzer.model.Question;
import edu.tacoma.uw.csquizzer.model.SubQuestion;

public class ShowQuestionFragment extends Fragment {
    View rootView;
    ProgressDialog pDialog;
    private RecyclerView recyclerView;
    private List<Question> questionsList = new ArrayList<>();
    QuestionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] args = new String[]{getArguments().getString("Course"), getArguments().getString("Topic"),
                getArguments().getString("Difficulty"),getArguments().getString("NumQuestions")};
        new ShowQuestionFragment.GetQuestions().execute(args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_show_question, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_quetions);
        return rootView;
    }

    private class GetQuestions extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Fetching data from database..");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if(!arg0[0].equals("--- Choose Course ---")) {
                list.add(new BasicNameValuePair("course",arg0[0]));
            }
            if(!arg0[1].equals("--- Choose Topic ---")) {
                list.add(new BasicNameValuePair("topic",arg0[1]));
            }
            if(!arg0[2].equals("--- Choose Difficulty ---")) {
                list.add(new BasicNameValuePair("difficulty",arg0[2]));
            }
            if(!arg0[3].equals("--- Choose Number Questions ---")) {
                list.add(new BasicNameValuePair("limit",arg0[3]));
            } else {
                list.add(new BasicNameValuePair("limit","20"));
            }
            String jsonQuestion = jsonParser.makeServiceCall(getString((R.string.get_questions)), ServiceHandler.GET, list);
            if (jsonQuestion != null) {
                try {
                    JSONObject jsonQuestionObj = new JSONObject(jsonQuestion);
                    if (jsonQuestionObj != null) {
                        JSONArray questions = jsonQuestionObj
                                .getJSONArray("questions");
                        List<Answer> answersList = new ArrayList<>();
                        List<SubQuestion> subQuestionsList = new ArrayList<>();
                        for (int i = 0; i < questions.length(); i++) {
                            JSONObject questionObj = (JSONObject) questions.get(i);
                            List<NameValuePair> qid = new ArrayList<NameValuePair>();
                            int questionId = Integer.parseInt(questionObj.getString("questionid"));
                            qid.add(new BasicNameValuePair("qid", Integer.toString( questionId)));
                            String jsonAnswer = jsonParser.makeServiceCall(getString((R.string.get_answers)), ServiceHandler.GET, qid);
                            if (jsonAnswer != null) {
                                try {
                                    JSONObject jsonAnswerObj = new JSONObject(jsonAnswer);
                                    if (jsonAnswerObj != null) {
                                        JSONArray ans = jsonAnswerObj
                                                .getJSONArray("answers");
                                        for (int j = 0; j < ans.length(); j++) {
                                            JSONObject answerObj = (JSONObject) ans.get(i);
                                            Answer ansobj = new Answer(answerObj.getInt("answerid"),
                                                    questionId, answerObj.getString("answertext"));
                                            answersList.add(ansobj);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                break;
                            }

                            String jsonSubQuestion = jsonParser.makeServiceCall(getString((R.string.get_subquestions)), ServiceHandler.GET, qid);

                            if (jsonSubQuestion != null) {
                                try {
                                    JSONObject jsonSubQuestionObj = new JSONObject(jsonSubQuestion);
                                    if (jsonSubQuestionObj != null) {
                                        JSONArray subs = jsonSubQuestionObj
                                                .getJSONArray("subquestions");
                                        for (int k = 0; k < subs.length(); k++) {
                                            JSONObject subQObj = (JSONObject) subs.get(i);
                                            SubQuestion subqobj = new SubQuestion(subQObj.getInt("subquestionid"),
                                                    questionId, subQObj.getString("subquestiontext"));
                                            subQuestionsList.add(subqobj);
                                        }
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
                            questionsList.add(question);
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

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            loadQuestions();
        }
    }

    private void loadQuestions() {
        adapter = new QuestionAdapter(getActivity(), questionsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

}