package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
        }
    }
}
