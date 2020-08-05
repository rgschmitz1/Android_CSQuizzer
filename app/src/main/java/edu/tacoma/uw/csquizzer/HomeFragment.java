package edu.tacoma.uw.csquizzer;

import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Course;
import edu.tacoma.uw.csquizzer.model.Difficulty;
import edu.tacoma.uw.csquizzer.model.Topic;
public class HomeFragment extends Fragment {
    View rootView;
    private Spinner spinnerCourses;
    private Spinner spinnerTopics;
    private Spinner spinnerDifficulties;
    private Spinner spinnerNumQuestions;
    private List<Course> coursesList;
    private List<Topic> topicsList;
    private List<Difficulty> difficultiesList;
    ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetData().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        spinnerCourses = (Spinner) rootView.findViewById(R.id.sn_courses);
        spinnerTopics = (Spinner) rootView.findViewById(R.id.sn_topics);
        spinnerDifficulties = (Spinner) rootView.findViewById(R.id.sn_difficulty);
        spinnerNumQuestions = (Spinner) rootView.findViewById(R.id.sn_num);

        coursesList = new ArrayList<>();
        topicsList = new ArrayList<>();
        difficultiesList = new ArrayList<>();

        Button button = (Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Bundle args = new Bundle();
                Spinner course = (Spinner) rootView.findViewById(R.id.sn_courses);
                Spinner topic = (Spinner) rootView.findViewById(R.id.sn_topics);
                Spinner difficulty = (Spinner) rootView.findViewById(R.id.sn_difficulty);
                Spinner num = (Spinner) rootView.findViewById(R.id.sn_num);


                String courseName = course.getSelectedItem().toString();
                String topicDescription = topic.getSelectedItem().toString();
                String difficultyDescription = difficulty.getSelectedItem().toString();
                String numQuestions =  num.getSelectedItem().toString();
                if (courseName.equals("--- Choose Course ---")
                        && topicDescription.equals("--- Choose Topic ---")
                        && difficultyDescription.equals("--- Choose Difficulty ---")) {
                    Toast.makeText(getActivity(), "Please choose conditions to find questions", Toast.LENGTH_LONG ).show();
                } else {
                    args.putString("Course", courseName);
                    args.putString("Topic", topicDescription);
                    args.putString("Difficulty", difficultyDescription);
                    args.putString("NumQuestions", numQuestions);
                    ShowQuestionFragment showQuestionFragment =  new ShowQuestionFragment();
                    showQuestionFragment.setArguments(args);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, showQuestionFragment);
                    ft.commit();
                }
            }
        });
        return rootView;
    }

    private class GetData extends AsyncTask<Void, Void, Void> {
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
            String jsonCourse = jsonParser.makeServiceCall(getString((R.string.get_courses)), ServiceHandler.GET);
            String jsonTopic = jsonParser.makeServiceCall(getString((R.string.get_topics)), ServiceHandler.GET);
            String jsonDifficulty = jsonParser.makeServiceCall(getString((R.string.get_difficulties)), ServiceHandler.GET);

            if (jsonCourse != null && jsonTopic != null && jsonDifficulty != null) {
                try {
                    JSONObject jsonCourseObj = new JSONObject(jsonCourse);
                    if (jsonCourseObj != null) {
                        JSONArray courses = jsonCourseObj
                                .getJSONArray("names");

                        for (int i = 0; i < courses.length(); i++) {
                            JSONObject courseObj = (JSONObject) courses.get(i);
                            Course course = new Course(courseObj.getInt("courseid"),
                                    courseObj.getString("coursename"));
                            coursesList.add(course);
                        }
                    }

                    JSONObject jsonTopicObj = new JSONObject(jsonTopic);
                    if (jsonTopicObj != null) {
                        JSONArray topics = jsonTopicObj
                                .getJSONArray("names");

                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject topicObj = (JSONObject) topics.get(i);
                            Topic topic = new Topic(topicObj.getInt("topicid"),
                                    topicObj.getString("topicdescription"));
                            topicsList.add(topic);
                        }
                    }

                    JSONObject jsonDifficultyObj = new JSONObject(jsonDifficulty);
                    if (jsonDifficultyObj != null) {
                        JSONArray difficulties = jsonDifficultyObj
                                .getJSONArray("names");

                        for (int i = 0; i < difficulties.length(); i++) {
                            JSONObject difficultyObjObj = (JSONObject) difficulties.get(i);
                            Difficulty difficulty = new Difficulty(difficultyObjObj.getInt("difficultyid"),
                                    difficultyObjObj.getString("difficultydescription"));
                            difficultiesList.add(difficulty);
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
            populateSpinner();
        }

    }
    /**
     * Adding spinner data
     * */
    private void populateSpinner() {
        List<String> courseNames = new ArrayList<String>();
        courseNames.add("--- Choose Course ---");
        for (int i = 0; i < coursesList.size(); i++) {
            courseNames.add(coursesList.get(i).getCourseName());
        }

        List<String> topicDescriptions = new ArrayList<String>();
        topicDescriptions.add("--- Choose Topic ---");
        for (int i = 0; i < topicsList.size(); i++) {
            topicDescriptions.add(topicsList.get(i).getTopicDescription());
        }

        List<String> difficultyDescriptions = new ArrayList<String>();
        difficultyDescriptions.add("--- Choose Difficulty ---");
        for (int i = 0; i < difficultiesList.size(); i++) {
            difficultyDescriptions.add(difficultiesList.get(i).getDifficultiesDescription());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerCourseAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, courseNames);
        spinnerCourseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCourses.setAdapter(spinnerCourseAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerTopicAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, topicDescriptions);
        spinnerTopicAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerTopics.setAdapter(spinnerTopicAdapter);

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerDifficultyAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, difficultyDescriptions);
        spinnerDifficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerDifficulties.setAdapter(spinnerDifficultyAdapter);
    }
}