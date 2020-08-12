package edu.tacoma.uw.csquizzer;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Topic;

public class TopicFragment extends Fragment implements SearchView.OnQueryTextListener{
    View rootView;
    private RecyclerView recyclerView;
    private List<Topic> lTopics = new ArrayList<>();
    private SearchView searchView;
    private TopicAdapter adapter;
    private FloatingActionButton btnFag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TopicFragment.GetTopics().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_topic, container, false);
        searchView = (SearchView) rootView.findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(this);
        // Inflate the layout for this fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_topics);
        btnFag = rootView.findViewById(R.id.btn_AddTopic);
        btnFag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AddTopicFragment addTopicFragment =  new AddTopicFragment(getContext());
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            //Replace current fragment with a show question fragment
            ft.replace(R.id.fragment_container, addTopicFragment);
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

    private class GetTopics extends AsyncTask<Void, Void, Void> {
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
         * @version 1.0
         * @since   2020-08-05
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            ServiceHandler jsonParser = new ServiceHandler();
            String jsonTopics = jsonParser.makeServiceCall(
                    getString((R.string.get_topics)), ServiceHandler.GET);
            if (jsonTopics != null) {
                try {
                    JSONObject jsonTopicObj = new JSONObject(jsonTopics);
                    if (jsonTopicObj != null) {
                        JSONArray topics = jsonTopicObj.getJSONArray("names");
                        for (int i = 0; i < topics.length(); i++) {
                            JSONObject topicObj = (JSONObject) topics.get(i);
                            //Get information a difficulty and add to a list difficulty
                            Topic topic = new Topic(topicObj.getInt("topicid"),
                                    topicObj.getString("topicdescription"));
                            lTopics.add(topic);
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
         * Finish reading json data and attach them to recyler
         * @param result
         * @author  Phuc Pham N
         * @version 1.0
         * @since   2020-08-05
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadTopics();
        }
    }

    /**
     * ItemDecoration - thiết lập khoảng cách giữa các phần tử với nhau
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


    /**
     * Attach data to cycler
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    private void loadTopics() {
        adapter = new TopicAdapter(getActivity(), lTopics);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        recyclerView.setAdapter(adapter);
    }
}