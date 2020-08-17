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
import edu.tacoma.uw.csquizzer.model.Question;


public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.MyViewHolder> {
    private List<Question> lQuestions = new ArrayList<>();
    private List<Question> queryQuestions = new ArrayList<>();
    private Context mContext;
    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;
    public QuestionListAdapter(Context mContext, List<Question> lQuestions) {
        this.mContext = mContext;
        this.lQuestions = lQuestions;
        this.queryQuestions.addAll(lQuestions);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return TYPE2;
        else
            return TYPE1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        switch (viewType)
        {
            case TYPE1:
                view = inflater.inflate(R.layout.item_questions_white, parent, false);
                break;
            case TYPE2:
                view = inflater.inflate(R.layout.item_questions_purple, parent, false);
                break;
        }

        return new QuestionListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Question question = lQuestions.get(position);
        holder.tvQuestionId.setText(question.getQuestionId() + ".");
        holder.tvQuestionTitle.setText(question.getQuestionTitle());
        holder.tvQuestionBody.setText(question.getQuestionBody());
        holder.tvCourseName.setText(question.getCourseName());
        holder.tvTopicDescription.setText(question.getTopicDescription());
        holder.tvDifficultyDescription.setText(question.getDifficultyDescription());
        holder.tvTypeDescription.setText(question.getTypeDescription());
    }

    @Override
    public int getItemCount() {
        return lQuestions.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lQuestions.clear();
        if (charText.length() == 0) {
            lQuestions.addAll(queryQuestions);
        } else {
            for (Question question : queryQuestions) {
                if (question.getQuestionTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    lQuestions.add(question);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvQuestionId;
        private TextView tvQuestionTitle;
        private TextView tvQuestionBody;
        private TextView tvCourseName;
        private TextView tvTopicDescription;
        private TextView tvDifficultyDescription;
        private TextView tvTypeDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvQuestionId = itemView.findViewById(R.id.tv_QuestionId);
            this.tvQuestionTitle = itemView.findViewById(R.id.tv_QuestionTitle);
            this.tvQuestionBody = itemView.findViewById(R.id.tv_QuestionBody);
            this.tvCourseName = itemView.findViewById(R.id.tv_CourseName);
            this.tvTopicDescription = itemView.findViewById(R.id.tv_TopicDescription);
            this.tvDifficultyDescription = itemView.findViewById(R.id.tv_DifficultyDescription);
            this.tvTypeDescription = itemView.findViewById(R.id.tv_TypeDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                EditQuestionFragment editQuestionFragment = new EditQuestionFragment(
                                            mContext,
                                            tvQuestionId.getText().toString(),
                                            tvQuestionTitle.getText().toString(),
                                            tvQuestionBody.getText().toString(),
                                            tvCourseName.getText().toString(),
                                            tvTopicDescription.getText().toString(),
                                            tvDifficultyDescription.getText().toString(),
                                            tvTypeDescription.getText().toString());
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, editQuestionFragment)
                        .commit();
                }
            });
            final Button btnDeleteQuestion = itemView.findViewById(R.id.btn_DeleteQuestion);
            btnDeleteQuestion.setOnClickListener(new View.OnClickListener() {
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
                            AsyncDeleteTask task = new AsyncDeleteTask(tvQuestionId.getText().toString(),
                                    dialog, new MyInterface() {
                                @Override
                                public void myMethod(boolean result) {
                                    if (result == true) {
                                        Toast.makeText(mContext, "Delete question successfully", Toast.LENGTH_LONG).show();
                                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                        QuestionFragment questionFragment = new QuestionFragment();
                                        activity.getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fragment_container, questionFragment)
                                                .commit();
                                    } else {
                                        Toast.makeText(mContext, "Delete question unsuccessfully", Toast.LENGTH_LONG).show();
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
    private class AsyncDeleteTask extends AsyncTask<Void, Void, Boolean> {
        private MyInterface mListener;
        private String questionId;
        private AlertDialog dialog;
        public AsyncDeleteTask(String mQuestionId, AlertDialog mdialog, MyInterface listener) {
            this.questionId = mQuestionId.substring(0, mQuestionId.length() - 1);
            this.dialog = mdialog;
            this.mListener  = listener;
        }

        @Override
        protected Boolean doInBackground(Void... args) {
            try {
                ServiceHandler jsonParser = new ServiceHandler();
                Map<String, String> mQuestion = new HashMap<>();
                mQuestion.put("qid", questionId);
                JSONObject jsonDeleteSubquestion = new JSONObject(jsonParser.makeServiceCall(
                        mContext.getString((R.string.delete_subquestions)), ServiceHandler.POST,mQuestion));
                JSONObject jsonDeleteAnswer = new JSONObject(jsonParser.makeServiceCall(
                        mContext.getString((R.string.delete_answers)), ServiceHandler.POST,mQuestion));
                Map<String, String> mQuestionId = new HashMap<>();
                mQuestionId.put("id", questionId);
                JSONObject jsonDeleteQuestion = new JSONObject(jsonParser.makeServiceCall(
                        mContext.getString((R.string.delete_questions)), ServiceHandler.POST,mQuestionId));
                if (jsonDeleteSubquestion.getBoolean("success")
                    && jsonDeleteAnswer.getBoolean("success")
                        && jsonDeleteQuestion.getBoolean("success")) {
                    dialog.cancel();
                    return true;
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
