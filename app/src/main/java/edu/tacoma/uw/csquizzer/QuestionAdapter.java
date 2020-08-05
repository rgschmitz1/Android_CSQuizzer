package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.tacoma.uw.csquizzer.model.Question;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder>{
    private List<Question> mQuestions;
    private Context mContext;
    // True/False
    public static final int TYPE1 = 1;

    // Single Choice
    public static final int TYPE2 = 2;

    // Multiple Choice
    public static final int TYPE3 = 3;

    public QuestionAdapter(Context mContext, List<Question> mQuestions) {
        this.mQuestions = mQuestions;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if (mQuestions.get(position).getTypeDescription().equals("True/False"))
            return TYPE1;
        else if (mQuestions.get(position).getTypeDescription().equals("Single Choice"))
            return TYPE2;
        else
            return TYPE3;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = null;
        switch (viewType)
        {
            case TYPE1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_true_false, parent, false);
                break;
            case TYPE2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_single_choice, parent, false);
                break;
            case TYPE3:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_multiple_choice, parent, false);
            break;
        }
        MyViewHolder viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Question question = mQuestions.get(position);
        holder.tvQuestionId.setText(question.getQuestionId() + "");
        holder.tvCountNumber.setText((position + 1) + ". ");
        holder.tvQuestionTitle.setText(question.getQuestionTitle());
        holder.tvCourseName.setText(question.getCourseName());
        holder.tvTopicDescription.setText(question.getTopicDescription());
        holder.tvDifficultyDescription.setText(question.getDifficultyDescription());
        holder.tvQuestionBody.setText(question.getQuestionBody());


//        if (mQuestions.get(position).getTypeDescription().equals("True/False"))
//
//        else if (mQuestions.get(position).getTypeDescription().equals("True/False"))
//
//        else


    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestionId;
        private TextView tvQuestionTitle;
        private TextView tvCountNumber;
        private TextView tvCourseName;
        private TextView tvTopicDescription;
        private TextView tvDifficultyDescription;
        private TextView tvQuestionBody;
        private Button btnCheckQuestion;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.tvQuestionId = itemView.findViewById(R.id.tv_QuestionId);
            this.tvQuestionTitle = itemView.findViewById(R.id.tv_QuestionTitle);
            this.tvCourseName = itemView.findViewById(R.id.tv_CourseName);
            this.tvCountNumber = itemView.findViewById(R.id.tv_CountNumber);
            this.tvTopicDescription = itemView.findViewById(R.id.tv_TopicDescription);
            this.tvDifficultyDescription = itemView.findViewById(R.id.tv_DifficultyDescription);
            this.tvQuestionBody = itemView.findViewById(R.id.tv_QuestionBody);
            this.btnCheckQuestion = itemView.findViewById(R.id.btn_CheckQuestion);

            this.btnCheckQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("JSON Data", tvQuestionTitle.getText().toString());
                }
            });
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle args = new Bundle();
//                    String questionId = ((TextView)v.findViewById(R.id.tv_QuestionId)).getText().toString();
//                    int id = Integer.parseInt(questionId.substring(questionId.length() - 1));
//                    args.putString("QuestionId", Integer.toString(id));
//                    String questionTitle = "", questionBody = "", course = "", topic = "", difficulty = "", type ="";
//                    for(Question question: mQuestions) {
//                        if(question.getQuestionId() == id) {
//                            questionTitle = question.getQuestionTitle();
//                            questionBody = question.getQuestionBody();
//                            course = question.getCourseName();
//                            topic = question.getTopicDescription();
//                            difficulty = question.getDifficultyDescription();
//                            type = question.getTypeDescription();
//                        }
//                    }
//                    args.putString("QuestionTitle", questionTitle);
//                    args.putString("QuestionBody", questionBody);
//                    args.putString("CourseName", course);
//                    args.putString("TopicDescription", topic);
//                    args.putString("DifficultyDescription", difficulty);
//                    args.putString("TypeDescription", type);
//                    if(type.equals("True/False")) {
//                        QuestionDetailTrueFalseFragment questionDetailFragment =  new QuestionDetailTrueFalseFragment();
//                        questionDetailFragment.setArguments(args);
//                        FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        final FragmentTransaction ft = manager.beginTransaction();
//                        ft.replace(R.id.fragment_container, questionDetailFragment);
//                        ft.commit();
//                    } else if(type.equals("Single Choice")) {
//                        QuestionDetailSingleChoiceFragment questionDetailSingleChoiceFragment =  new QuestionDetailSingleChoiceFragment();
//                        questionDetailSingleChoiceFragment.setArguments(args);
//                        FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        final FragmentTransaction ft = manager.beginTransaction();
//                        ft.replace(R.id.fragment_container, questionDetailSingleChoiceFragment);
//                        ft.commit();
//                    } else {
//                        QuestionDetailMultipleChoiceFragment questionDetailMultipleChoiceFragment =  new QuestionDetailMultipleChoiceFragment();
//                        questionDetailMultipleChoiceFragment.setArguments(args);
//                        FragmentManager manager = ((AppCompatActivity)mContext).getSupportFragmentManager();
//                        final FragmentTransaction ft = manager.beginTransaction();
//                        ft.replace(R.id.fragment_container, questionDetailMultipleChoiceFragment);
//                        ft.commit();
//                    }
                }
//            });
        }
}
