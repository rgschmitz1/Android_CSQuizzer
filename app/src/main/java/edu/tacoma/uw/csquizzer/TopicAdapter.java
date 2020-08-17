package edu.tacoma.uw.csquizzer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Topic;

/**
 * The TopicAdapter renders topics information to recycler.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.MyViewHolder> {
    private List<Topic> lTopics;
    private List<Topic> queryTopics = new ArrayList<>();
    private Context mContext;
    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;

    public TopicAdapter(Context mContext, List<Topic> lTopics) {
        this.mContext = mContext;
        this.lTopics = lTopics;
        this.queryTopics.addAll(lTopics);
    }

    /**
     * Get topic in a list at position
     *
     * @param position the current position of the recycler.
     * @return
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return TYPE2;
        else
            return TYPE1;
    }

    /**
     * render view on GUI
     *
     * @param parent MainActivity
     * @param viewType type of question
     * @return viewHolder
     *
     * @author  Phuc Pham N
     * @since   2020-08-17
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType)
        {
            case TYPE1:
                view = inflater.inflate(R.layout.item_topics_white, parent, false);
                break;
            case TYPE2:
                view = inflater.inflate(R.layout.item_topics_purple, parent, false);
                break;
        }

        return new TopicAdapter.MyViewHolder(view);
    }

    /**
     * We have two templates and the different between them is color
     * Render view on recycler.
     *
     * @param holder recycler
     * @param position type of question
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Topic topic = lTopics.get(position);
        holder.tvTopicId.setText(topic.getTopicId() + ".");
        holder.tvTopicTitle.setText(" " + topic.getTopicDescription());
    }

    @Override
    public int getItemCount() {
        return lTopics.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lTopics.clear();
        if (charText.length() == 0) {
            lTopics.addAll(queryTopics);
        } else {
            for (Topic topic : queryTopics) {
                if (topic.getTopicDescription().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lTopics.add(topic);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTopicId;
        private TextView tvTopicTitle;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTopicId = itemView.findViewById(R.id.tv_TopicId);
            this.tvTopicTitle = itemView.findViewById(R.id.tv_TopicDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                EditTopicFragment editTopicFragment = new EditTopicFragment(mContext,
                        tvTopicId.getText().toString(), tvTopicTitle.getText().toString());
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editTopicFragment)
                        .commit();
                }
            });
            final Button btnDeleteTopic = itemView.findViewById(R.id.btn_DeleteTopic);
            btnDeleteTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setView(inflater.inflate(R.layout.confirm, null));
                final AlertDialog dialog = builder.create();
                dialog.show();
                final Button btnOK = dialog.findViewById(R.id.btn_ok);
                final Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                    AsyncDeleteTask task = new AsyncDeleteTask(tvTopicId.getText().toString(),
                        dialog, new MyInterface() {
                    @Override
                    public void myMethod(boolean result) {
                        if (result == true) {
                            Toast.makeText(mContext, "Delete topic successfully", Toast.LENGTH_LONG).show();
                            AppCompatActivity activity = (AppCompatActivity) view.getContext();
                            TopicFragment topicFragment = new TopicFragment();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, topicFragment)
                                    .commit();
                        } else {
                            Toast.makeText(mContext, "Delete topic unsuccessfully", Toast.LENGTH_LONG).show();
                        }
                        }
                    });
                    task.execute();
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                }
            });
        }
    }
    public interface MyInterface {
        public void myMethod(boolean result);
    }

    /**
     * Delete a topic in database
     */
    private class AsyncDeleteTask extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        private String topicId;
        private AlertDialog dialog;
        public AsyncDeleteTask(String mTopicId, AlertDialog mdialog, MyInterface listener) {
            this.topicId = mTopicId.substring(0, mTopicId.length() - 1);
            this.dialog = mdialog;
            this.mListener  = listener;
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                Map<String, String> mapConditions = new HashMap<>();
                mapConditions.put("topicid", topicId);
                // Read courses using GET METHOD
                JSONObject jsonQuestionObj = new JSONObject(jsonParser.makeServiceCall(
                        mContext.getString((R.string.get_questions_by_topic_id)),
                        ServiceHandler.GET, mapConditions));
                if (jsonQuestionObj != null) {
                    //Get list questions
                    JSONArray arrQuestions = jsonQuestionObj.getJSONArray("names");
                    List<String> arrQuestionId = new ArrayList<>();
                    for (int i = 0; i < arrQuestions.length(); i++) {
                        arrQuestionId.add(((JSONObject) arrQuestions.get(i)).getString("questionid"));
                    }

                    boolean deleteAllSubAns = true;
                    for(int i = 0; i < arrQuestionId.size();i++) {
                        Map<String, String> mQuestion = new HashMap<>();
                        mQuestion.put("qid", arrQuestionId.get(i));
                        JSONObject jsonDeleteSubquestion = new JSONObject(jsonParser.makeServiceCall(
                                mContext.getString((R.string.delete_subquestions)),
                                ServiceHandler.POST, mQuestion));
                        JSONObject jsonDeleteAnswer = new JSONObject(jsonParser.makeServiceCall(
                                mContext.getString((R.string.delete_answers)),
                                ServiceHandler.POST, mQuestion));
                        if (jsonDeleteSubquestion.getBoolean("success")
                                && jsonDeleteAnswer.getBoolean("success")) {
                            Map<String, String> mQuestionId = new HashMap<>();
                            mQuestionId.put("id", arrQuestionId.get(i));
                            JSONObject jsonDeleteQuestion = new JSONObject(jsonParser.makeServiceCall(
                                    mContext.getString((R.string.delete_questions)),
                                    ServiceHandler.POST, mQuestionId));
                            if (jsonDeleteQuestion.getBoolean("success")) {
                                deleteAllSubAns = true;
                            } else {
                                deleteAllSubAns = false;
                                break;
                            }
                        } else {
                            deleteAllSubAns = false;
                            break;
                        }
                    }

                    if(deleteAllSubAns) {
                        Map<String, String> mTopicId = new HashMap<>();
                        mTopicId.put("id", topicId);
                        JSONObject jsonDeleteObj = new JSONObject(jsonParser.makeServiceCall(
                                mContext.getString((R.string.delete_topics)),
                                ServiceHandler.POST, mTopicId));
                        if(jsonDeleteObj != null) {
                            dialog.cancel();
                            return true;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.cancel();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (mListener != null)
                mListener.myMethod(result);
        }
    }
}
