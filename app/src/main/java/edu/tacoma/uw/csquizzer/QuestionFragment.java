package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * The purpose of QuestionFragment module is to list out question and add action listener to recycler
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-17
 */
public class QuestionFragment extends Fragment implements SearchView.OnQueryTextListener{
    View rootView;
    private RecyclerView recyclerView;
    private List<Question> lQuestions = new ArrayList<>();
    private SearchView searchView;
    private QuestionListAdapter adapter;
    private FloatingActionButton btnFag;
    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new QuestionFragment.GetQuestions().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        searchView = (SearchView) rootView.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_questions);
        btnFag = rootView.findViewById(R.id.btn_AddQuestion);
        btnFag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddQuestionFragment addQuestionFragment =  new AddQuestionFragment(getContext());
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, addQuestionFragment);
                ft.commit();
            }
        });
        return rootView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    private class GetQuestions extends AsyncTask<Void, Void, Void> {
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
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonQuestions = jsonParser.makeServiceCall(
                    getString((R.string.get_questions)), ServiceHandler.GET);
            if (jsonQuestions != null) {
                try {
                    JSONObject jsonQuestionObj = new JSONObject(jsonQuestions);
                    if (jsonQuestionObj != null) {
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
                                    if (jsonAnswerObj != null) {
                                        JSONArray ans = jsonAnswerObj.getJSONArray("answers");
                                        for (int j = 0; j < ans.length(); j++) {
                                            JSONObject answerObj = (JSONObject) ans.get(j);
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

                            String jsonSubQuestion = jsonParser.makeServiceCall(
                                    getString((R.string.get_subquestions)),
                                    ServiceHandler.GET, qid);

                            if (jsonSubQuestion != null) {
                                try {
                                    JSONObject jsonSubQuestionObj = new JSONObject(jsonSubQuestion);
                                    if (jsonSubQuestionObj != null) {
                                        JSONArray subs = jsonSubQuestionObj
                                                .getJSONArray("subquestions");
                                        for (int k = 0; k < subs.length(); k++) {
                                            JSONObject subQObj = (JSONObject) subs.get(k);
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
                            lQuestions.add(question);
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

    /**
     * Add top margin only for the first item to avoid double space between items
     */
    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;
        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }
        @Override
        public void getItemOffsets(Rect outRect,
                                   View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }

    private void loadQuestions() {
        adapter = new QuestionListAdapter(getActivity(), lQuestions);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(adapter);
    }
}