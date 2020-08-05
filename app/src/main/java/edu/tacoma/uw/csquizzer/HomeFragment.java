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

// Lifecycle OnCreate ->  onPreExecute -> doInBackground -> onPostExecute -> onCreateView
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
        // GetData will extend AsyncTask.
        /**
         * get course name in course table and attach data to spinner
         * get topic description in topic table and attach data to spinner
         * get difficulty description in difficulty and attach data to spinner
         */
        new GetData().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // get spinner information on GUI
        spinnerCourses = (Spinner) rootView.findViewById(R.id.sn_courses);
        spinnerTopics = (Spinner) rootView.findViewById(R.id.sn_topics);
        spinnerDifficulties = (Spinner) rootView.findViewById(R.id.sn_difficulty);
        spinnerNumQuestions = (Spinner) rootView.findViewById(R.id.sn_num);

        // Store list courses, topics, difficulty
        coursesList = new ArrayList<>();
        topicsList = new ArrayList<>();
        difficultiesList = new ArrayList<>();

        // Listen button action
        Button button = (Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String courseName = ((Spinner) rootView.findViewById(R.id.sn_courses))
                                            .getSelectedItem().toString();
                String topicDescription = ((Spinner) rootView.findViewById(R.id.sn_topics))
                                            .getSelectedItem().toString();
                String difficultyDescription = ((Spinner) rootView.findViewById(R.id.sn_difficulty))
                                            .getSelectedItem().toString();
                String numQuestions = ((Spinner) rootView.findViewById(R.id.sn_num))
                                            .getSelectedItem().toString();

                //Store data(Course Name, Topic Description, Difficulty Description, Number Question) to bundle
                Bundle args = new Bundle();
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
                    //Replace current fragment with a show question fragment
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
            // Read courses using GET METHOD
            String jsonCourse = jsonParser.makeServiceCall(
                    getString((R.string.get_courses)), ServiceHandler.GET);
            // Read topics using GET METHOD
            String jsonTopic = jsonParser.makeServiceCall(
                    getString((R.string.get_topics)), ServiceHandler.GET);
            // Read difficulties using GET METHOD
            String jsonDifficulty = jsonParser.makeServiceCall(
                    getString((R.string.get_difficulties)), ServiceHandler.GET);

            if (jsonCourse != null && jsonTopic != null && jsonDifficulty != null) {
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
                            coursesList.add(course);
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
                            topicsList.add(topic);
                        }
                    }

                    //Convert difficulties data string to JSON
                    JSONObject jsonDifficultyObj = new JSONObject(jsonDifficulty);
                    if (jsonDifficultyObj != null) {
                        //Get list difficulties
                        JSONArray difficulties = jsonDifficultyObj.getJSONArray("names");
                        for (int i = 0; i < difficulties.length(); i++) {
                            JSONObject difficultyObjObj = (JSONObject) difficulties.get(i);
                            //Get information a difficulty and add to a list difficulty
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

    /** Adding spinner data */
    private void populateSpinner() {
        //Get list course name and attach to course spinner
        List<String> courseNames = new ArrayList<String>();
        courseNames.add("--- Choose Course ---");
        for (int i = 0; i < coursesList.size(); i++) {
            courseNames.add(coursesList.get(i).getCourseName());
        }

        //Get list topic description and attach to topic spinner
        List<String> topicDescriptions = new ArrayList<String>();
        topicDescriptions.add("--- Choose Topic ---");
        for (int i = 0; i < topicsList.size(); i++) {
            topicDescriptions.add(topicsList.get(i).getTopicDescription());
        }

        //Get list difficulty description and attach to difficulty spinner
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