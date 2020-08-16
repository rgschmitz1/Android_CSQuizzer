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
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import edu.tacoma.uw.csquizzer.helper.ServiceHandler;

public class EditTopicFragment extends Fragment {
    TextView tvTopicId;
    ImageButton tvBackToList;
    EditText tvTopicDescription;
    Button btnSubmit;
    Button btnCancel;
    String topicId;
    String topicDescription;
    Context context;
    public EditTopicFragment(Context mContext, String mTopicId, String mTopicDescription) {
        this.context = mContext;
        this.topicId = String.valueOf(mTopicId.charAt(0));
        this.topicDescription = mTopicDescription;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_topic, container, false);
        tvTopicId = rootView.findViewById(R.id.tv_TopicId);
        tvTopicId.setText(topicId);
        tvTopicDescription = rootView.findViewById(R.id.et_TopicDescription);
        tvTopicDescription.setText(topicDescription);
        btnSubmit = rootView.findViewById(R.id.btn_SubmitTopic);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String topicId = tvTopicId.getText().toString();
                String topicDescription = tvTopicDescription.getText().toString();
                if(topicDescription.length() == 0) {
                    Toast.makeText(getContext(), "Please input the topic description", Toast.LENGTH_SHORT)
                            .show();
                } else{
                    String[] args = new String[]{topicId, topicDescription};
                    EditTopic task = new EditTopic(context, topicId, topicDescription, new MyInterface() {
                        @Override
                        public void myMethod(boolean result) {
                            if (result == true) {
                                Toast.makeText(context, "Update topic successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Update topic unsuccessfully", Toast.LENGTH_LONG).show();
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

    private class EditTopic extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        Context context;
        String topicId;
        String topicDescription;


        public EditTopic(Context mContext, String mTopicId, String mTopicDescription, MyInterface listener) {
            this.context = mContext;
            this.topicId = mTopicId;
            this.topicDescription = mTopicDescription;
            this.mListener  = listener;
        }
        @Override
        protected Boolean doInBackground(Void... args) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("id", topicId);
            mapConditions.put("description", topicDescription);
            String jsonTopic = jsonParser.makeServiceCall(
                    getString((R.string.update_topics)), ServiceHandler.POST,mapConditions);
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