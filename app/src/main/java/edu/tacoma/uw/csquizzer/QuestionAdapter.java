package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import edu.tacoma.uw.csquizzer.model.Question;

/**
 * The QuestionAdapter renders question information to recycler.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder>{
    private List<Question> mQuestions;
    private Context mContext;

    public static final int TYPE1 = 0;  // True/False
    public static final int TYPE2 = 1;  // Single Choice
    public static final int TYPE3 = 2;  // Multiple Choice

    public QuestionAdapter(Context mContext, List<Question> mQuestions) {
        this.mQuestions = mQuestions;
        this.mContext = mContext;
    }

    /**
     * Get question in a list at position, and return a type of question.
     *
     * @param position the current position of the recyler.
     * @return type of question 0: true/ false, 1: single choice, 2: multiple choice
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Override
    public int getItemViewType(int position) {
        if (mQuestions.get(position).getTypeDescription().equals("True/False"))
            return TYPE1;
        else if (mQuestions.get(position).getTypeDescription().equals("Single Choice"))
            return TYPE2;
        else
            return TYPE3;
    }

    /**
     * Based on type of question, render view on GUI
     *
     * @param parent MainActivity
     * @param viewType type of question
     * @return viewHolder
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = null;
        switch (viewType)
        {
            case TYPE1: // If question is a true false question, using true false template
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_true_false, parent, false);
                break;
            case TYPE2: // If question is a true false question, using single choice template
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_single_choice, parent, false);
                break;
            case TYPE3: // If question is a true false question, using multiple choice template
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_multiple_choice, parent, false);
            break;
        }
        MyViewHolder viewHolder = new MyViewHolder(itemView, viewType);
        return viewHolder;
    }

    /**
     * We have three templates and the different between
     * them is type of questions (true/false, single choice, multiple choice)
     * Render view on recyler.
     *
     * @param holder recycler
     * @param position type of question
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /**
         * Attach data in first load, we have three templates
         * and the different between them is type of questions (true/false, single choice, multiple choice)
         */

        // Get question at position in the question list
        Question question = mQuestions.get(position);
        holder.tvQuestionId.setText(question.getQuestionId() + "");
        holder.tvCountNumber.setText((position + 1) + ". ");
        holder.tvQuestionTitle.setText(question.getQuestionTitle());
        holder.tvCourseName.setText(question.getCourseName());
        holder.tvTopicDescription.setText(question.getTopicDescription());
        holder.tvDifficultyDescription.setText(question.getDifficultyDescription());
        holder.tvQuestionBody.setText(question.getQuestionBody());
        if (mQuestions.get(position).getTypeDescription().equals("True/False")) {
            holder.rbTrue.setText("True");
            holder.rbFalse.setText("False");
        } else if (mQuestions.get(position).getTypeDescription().equals("Single Choice")) {
            holder.rbSubQuestion1.setText(mQuestions.get(position).getListSubQuestions().get(0).getSubQuestionText());
            holder.rbSubQuestion2.setText(mQuestions.get(position).getListSubQuestions().get(1).getSubQuestionText());
            holder.rbSubQuestion3.setText(mQuestions.get(position).getListSubQuestions().get(2).getSubQuestionText());
            holder.rbSubQuestion4.setText(mQuestions.get(position).getListSubQuestions().get(3).getSubQuestionText());
        } else if (mQuestions.get(position).getTypeDescription().equals("Multiple Choice")) {
            holder.cbSubQuestion1.setText(mQuestions.get(position).getListSubQuestions().get(0).getSubQuestionText());
            holder.cbSubQuestion2.setText(mQuestions.get(position).getListSubQuestions().get(1).getSubQuestionText());
            holder.cbSubQuestion3.setText(mQuestions.get(position).getListSubQuestions().get(2).getSubQuestionText());
            holder.cbSubQuestion4.setText(mQuestions.get(position).getListSubQuestions().get(3).getSubQuestionText());
        }
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
        private Button btnReport;
        private Button btnShowAnswer;
        private RadioButton rbTrue;
        private RadioButton rbFalse;
        private RadioButton rbSubQuestion1;
        private RadioButton rbSubQuestion2;
        private RadioButton rbSubQuestion3;
        private RadioButton rbSubQuestion4;
        private CheckBox cbSubQuestion1;
        private CheckBox cbSubQuestion2;
        private CheckBox cbSubQuestion3;
        private CheckBox cbSubQuestion4;
        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            //Listen recyler item
            this.tvQuestionId = itemView.findViewById(R.id.tv_QuestionId);
            this.tvQuestionTitle = itemView.findViewById(R.id.tv_QuestionTitle);
            this.tvCourseName = itemView.findViewById(R.id.tv_CourseName);
            this.tvCountNumber = itemView.findViewById(R.id.tv_CountNumber);
            this.tvTopicDescription = itemView.findViewById(R.id.tv_TopicDescription);
            this.tvDifficultyDescription = itemView.findViewById(R.id.tv_DifficultyDescription);
            this.tvQuestionBody = itemView.findViewById(R.id.tv_QuestionBody);
            this.btnReport = itemView.findViewById(R.id.btn_Report);
            this.btnShowAnswer = itemView.findViewById(R.id.btn_ShowAnswer);
            if(viewType == TYPE1) {
                this.rbTrue = itemView.findViewById(R.id.radio_true);
                this.rbFalse = itemView.findViewById(R.id.radio_false);
            } else if (viewType == TYPE2) {
                this.rbSubQuestion1 = itemView.findViewById(R.id.radio_subquestion_1);
                this.rbSubQuestion2 = itemView.findViewById(R.id.radio_subquestion_2);
                this.rbSubQuestion3 = itemView.findViewById(R.id.radio_subquestion_3);
                this.rbSubQuestion4 = itemView.findViewById(R.id.radio_subquestion_4);
            } else if (viewType == TYPE3) {
                this.cbSubQuestion1 = itemView.findViewById(R.id.cb_subquestion_1);
                this.cbSubQuestion2 = itemView.findViewById(R.id.cb_subquestion_2);
                this.cbSubQuestion3 = itemView.findViewById(R.id.cb_subquestion_3);
                this.cbSubQuestion4 = itemView.findViewById(R.id.cb_subquestion_4);
            }
            this.btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            this.btnShowAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
