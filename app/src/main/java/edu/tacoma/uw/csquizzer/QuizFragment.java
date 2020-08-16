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


/**
 * The HomeFragment is placed in MainActivity. It contains four spinners and a button search question.
 * The first three spinners are getting json data.
 * The first spinner shows courses' name.
 * The second spinner shows topics' description.
 * The third spinner shows difficulties' description.
 * When hitting button search question. It is going to show all questions matching with conditions.
 * The questions will show in ShowQuestionFragment.
 *
 * The HomeFragment class basically get courses' name, topics' description, difficulties' description
 * and attach to spinners.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class QuizFragment extends Fragment {
    View rootView;
    private Spinner spinnerCourses;
    private Spinner spinnerTopics;
    private Spinner spinnerDifficulties;
    private Spinner spinnerNumQuestions;
    private List<Course> coursesList;
    private List<Topic> topicsList;
    ProgressDialog pDialog;

    /**
     * Execute GetData with no argument which extends AsyncTask to
     *      get courses' name in course table and attach data to spinner
     *      get topics' description in topic table and attach data to spinner
     *      get difficulties' description in difficulty and attach data to spinner
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Store list courses, topics, difficulty
        coursesList = new ArrayList<>();
        topicsList = new ArrayList<>();
        new GetData().execute();
    }

    /**
     * Render components to GUI
     * @param inflater a class used to instantiate layout XML file into its corresponding view objects
     * @param container a special view that can contain other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     * @return view
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
        // get spinner information on GUI
        spinnerCourses = (Spinner) rootView.findViewById(R.id.sn_courses);
        spinnerTopics = (Spinner) rootView.findViewById(R.id.sn_topics);
        spinnerDifficulties = (Spinner) rootView.findViewById(R.id.sn_difficulty);
        spinnerNumQuestions = (Spinner) rootView.findViewById(R.id.sn_num);

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
                    getString((R.string.get_courses)), ServiceHandler.GET);
            // Read topics using GET METHOD
            String jsonTopic = jsonParser.makeServiceCall(
                    getString((R.string.get_topics)), ServiceHandler.GET);

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
        for (int i = 0; i < coursesList.size(); i++) {
            courseNames.add(coursesList.get(i).getCourseName());
        }

        //Get list topic description and attach to topic spinner
        List<String> topicDescriptions = new ArrayList<String>();
        topicDescriptions.add("--- Choose Topic ---");
        for (int i = 0; i < topicsList.size(); i++) {
            topicDescriptions.add(topicsList.get(i).getTopicDescription());
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
    }

}