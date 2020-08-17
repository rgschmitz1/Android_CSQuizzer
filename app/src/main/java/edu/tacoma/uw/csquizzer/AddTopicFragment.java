package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;

/**
 * The purpose of AddTopicFragment module is to add a new topic to database
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-17
 */
public class AddTopicFragment extends Fragment {
    ImageButton tvBackToList;
    EditText tvTopicDescription;
    Button btnSubmit;
    Button btnCancel;
    Context context;

    public AddTopicFragment(Context mContext) {
        this.context = mContext;
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
        View rootView =  inflater.inflate(R.layout.fragment_add_topic, container, false);
        tvTopicDescription = rootView.findViewById(R.id.et_TopicDescription);
        btnSubmit = rootView.findViewById(R.id.btn_SubmitTopic);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topicDescription = tvTopicDescription.getText().toString();
                if(topicDescription.length() == 0) {
                    Toast.makeText(getContext(), "Please input the topic description", Toast.LENGTH_SHORT)
                            .show();
                } else{
                    String[] args = new String[]{topicDescription};
                    AddTopic task = new AddTopic(context, topicDescription, new MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                            if (result == true) {
                                Toast.makeText(context, "Add topic successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Add topic unsuccessfully", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    task.execute();
                }
            }
        });
        btnCancel = rootView.findViewById(R.id.btn_CancelTopic);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTopicDescription.setText("");
            }
        });
        tvBackToList = rootView.findViewById(R.id.imb_back_to_list);
        tvBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopicFragment topicFragment =  new TopicFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, topicFragment);
                ft.commit();
            }
        });
        return rootView;
    }


    public interface MyInterface {
        public void myMethod(boolean result);
    }
    /**
     * The AddTopic AsyncTask to store json data (topic title, topic description) to database
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-17
     */
    private class AddTopic extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String topicDescription;

        public AddTopic(Context mContext, String mTopicDescription, MyInterface listener) {
            this.context = mContext;
            this.topicDescription = mTopicDescription;
            this.mListener  = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("description", topicDescription);
            String jsonTopic = jsonParser.makeServiceCall(
                    getString((R.string.add_topics)), ServiceHandler.POST,mapConditions);
            if (jsonTopic != null) {
                try {
                    JSONObject jsonTopicObj = new JSONObject(jsonTopic);
                    if(jsonTopicObj.getBoolean("success")) {
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (mListener != null)
                mListener.myMethod(result);
        }
    }
}