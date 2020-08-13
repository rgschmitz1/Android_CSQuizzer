package edu.tacoma.uw.csquizzer;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.tacoma.uw.csquizzer.helper.ServiceHandler;
import edu.tacoma.uw.csquizzer.model.Answer;
import edu.tacoma.uw.csquizzer.model.Question;

/**
 * The QuestionAdapter renders question information to recycler.
 * Lifecycle: QuestionAdapter -> GetItemViewType -> onCreateViewHolder -> MyViewHolder -> onBindViewHolder
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.MyViewHolder>{
    private List<Question> lQuestions;
    private Context mContext;

    public static final int TYPE1 = 0;  // True/False
    public static final int TYPE2 = 1;  // Single Choice
    public static final int TYPE3 = 2;  // Multiple Choice

    public QuestionAdapter(Context mContext, List<Question> lQuestions) {
        this.mContext = mContext;
        this.lQuestions = lQuestions;
    }

    /**
     * Get question in a list at position, and return a type of question.
     *
     * @param position the current position of the recyler.
     * @return type of question 0: true/ false, 1: single choice, 2: multiple choice
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public int getItemViewType(int position) {
        if (lQuestions.get(position).getTypeDescription().equals("True/False"))
            return TYPE1;
        else if (lQuestions.get(position).getTypeDescription().equals("Single Choice"))
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
     * @since   2020-08-05
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        return new MyViewHolder(itemView, viewType);
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
     * @since   2020-08-05
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*
         * Attach data in first load, we have three templates
         * and the different between them is type of questions (true/false, single choice, multiple choice)
         */
        // Get question at position in the question list
        Question question = lQuestions.get(position);
        holder.tvQuestionId.setText(question.getQuestionId() + "");
        holder.tvCountNumber.setText((position + 1) + ". ");
        holder.tvQuestionTitle.setText(question.getQuestionTitle());
        holder.tvCourseName.setText(question.getCourseName());
        holder.tvTopicDescription.setText(question.getTopicDescription());
        holder.tvDifficultyDescription.setText(question.getDifficultyDescription());
        holder.tvQuestionBody.setText(question.getQuestionBody());
        if (question.getTypeDescription().equals("True/False")) {
            holder.rbTrue.setText(question.getListSubQuestions().get(0).getSubQuestionText());
            holder.rbFalse.setText(question.getListSubQuestions().get(1).getSubQuestionText());
        } else if (question.getTypeDescription().equals("Single Choice")) {
            holder.rbSubQuestion1.setText(question.getListSubQuestions().get(0).getSubQuestionText());
            holder.rbSubQuestion2.setText(question.getListSubQuestions().get(1).getSubQuestionText());
            holder.rbSubQuestion3.setText(question.getListSubQuestions().get(2).getSubQuestionText());
            holder.rbSubQuestion4.setText(question.getListSubQuestions().get(3).getSubQuestionText());
        } else if (lQuestions.get(position).getTypeDescription().equals("Multiple Choice")) {
            holder.cbSubQuestion1.setText(question.getListSubQuestions().get(0).getSubQuestionText());
            holder.cbSubQuestion2.setText(question.getListSubQuestions().get(1).getSubQuestionText());
            holder.cbSubQuestion3.setText(question.getListSubQuestions().get(2).getSubQuestionText());
            holder.cbSubQuestion4.setText(question.getListSubQuestions().get(3).getSubQuestionText());
        }
    }

    @Override
    public int getItemCount() {
        return lQuestions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestionId;
        private TextView tvQuestionTitle;
        private TextView tvCountNumber;
        private TextView tvCourseName;
        private TextView tvTopicDescription;
        private TextView tvDifficultyDescription;
        private TextView tvQuestionBody;
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
        public RelativeLayout relativeLayout;
        public MyViewHolder(final View itemView, final int viewType) {
            super(itemView);
            //Listen recycler item
            this.tvQuestionId = itemView.findViewById(R.id.tv_QuestionId);
            this.tvQuestionTitle = itemView.findViewById(R.id.tv_QuestionTitle);
            this.tvCourseName = itemView.findViewById(R.id.tv_CourseName);
            this.tvCountNumber = itemView.findViewById(R.id.tv_CountNumber);
            this.tvTopicDescription = itemView.findViewById(R.id.tv_TopicDescription);
            this.tvDifficultyDescription = itemView.findViewById(R.id.tv_DifficultyDescription);
            this.tvQuestionBody = itemView.findViewById(R.id.tv_QuestionBody);
            Button btnReport = itemView.findViewById(R.id.btn_Report);
            Button btnShowAnswer = itemView.findViewById(R.id.btn_ShowAnswer);
            if(viewType == TYPE1) {
                this.rbTrue = itemView.findViewById(R.id.radio_true);
                this.rbFalse = itemView.findViewById(R.id.radio_false);
                this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.template_true_false);
            } else if (viewType == TYPE2) {
                this.rbSubQuestion1 = itemView.findViewById(R.id.radio_subquestion_1);
                this.rbSubQuestion2 = itemView.findViewById(R.id.radio_subquestion_2);
                this.rbSubQuestion3 = itemView.findViewById(R.id.radio_subquestion_3);
                this.rbSubQuestion4 = itemView.findViewById(R.id.radio_subquestion_4);
                this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.template_single_choice);
            } else if (viewType == TYPE3) {
                this.cbSubQuestion1 = itemView.findViewById(R.id.cb_subquestion_1);
                this.cbSubQuestion2 = itemView.findViewById(R.id.cb_subquestion_2);
                this.cbSubQuestion3 = itemView.findViewById(R.id.cb_subquestion_3);
                this.cbSubQuestion4 = itemView.findViewById(R.id.cb_subquestion_4);
                this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.template_multiple_choice);
            }
            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setView(inflater.inflate(R.layout.dialog_report, null));
                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText etReport = (EditText) dialog.findViewById(R.id.et_report);
                final Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
                final Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if(etReport.getText().toString().length() > 0) {
                        AsyncConnectTask task = new AsyncConnectTask(
                                tvQuestionId.getText().toString(),
                                tvQuestionTitle.getText().toString(),
                                etReport.getText().toString(), dialog);
                        task.execute();
                        Toast.makeText(mContext, "Send error successful", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(mContext, "Please input message", Toast.LENGTH_SHORT)
                                .show();
                    }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Canceled sending error question", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });
                }
            });

            btnShowAnswer.setOnClickListener(new View.OnClickListener() {
                private AlertDialog dialog;
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    builder.setView(inflater.inflate(R.layout.check_answer, null));
                    Map<String, Question> mQuestions = convertListToMapOfQuestions(lQuestions);
                    dialog = builder.create();
                    dialog.show();
                    final TextView tvAnswer1 = (TextView)dialog.findViewById(R.id.tv_answer1);
                    final TextView tvAnswer2 = (TextView)dialog.findViewById(R.id.tv_answer2);
                    final TextView tvAnswer3 = (TextView)dialog.findViewById(R.id.tv_answer3);
                    final TextView tvAnswer4 = (TextView)dialog.findViewById(R.id.tv_answer4);
                    final Button btnOK = (Button) dialog.findViewById(R.id.btn_ok);
                    switch (mQuestions.get(tvQuestionId.getText().toString()).getTypeDescription()) {
                        case "True/False":
                        case "Single Choice": {
                            List<Answer> answer = mQuestions.get(tvQuestionId.getText().toString()).getListAnswers();
                            tvAnswer1.setText("Answer . " + answer.get(0).getAnswerText());
                            tvAnswer1.setVisibility(View.VISIBLE);
//                        Toast.makeText(mContext, "The answer is " + answer.get(0).getAnswerText(), Toast.LENGTH_SHORT).show();
//                        if(answer.get(0).getAnswerText().equals("True")) {
//                            rbTrue.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbTrue.setTextColor(mContext.getResources().getColor(R.color.Green));
//                        } else if (answer.get(0).getAnswerText().equals("False")) {
//                            rbFalse.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbFalse.setTextColor(mContext.getResources().getColor(R.color.Green));
//                        }
                            break;
                        }//                        if(answer.get(0).getAnswerText().equals(rbSubQuestion1.getText().toString())) {
//                            rbSubQuestion1.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbSubQuestion1.setTextColor(mContext.getResources().getColor(R.color.Green));
//                        } else if (answer.get(0).getAnswerText().equals(rbSubQuestion2.getText().toString())) {
//                            rbSubQuestion2.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbSubQuestion2.setTextColor(mContext.getResources().getColor(R.color.Green));
//                        } else if (answer.get(0).getAnswerText().equals(rbSubQuestion3.getText().toString())) {
//                            rbSubQuestion3.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbSubQuestion3.setTextColor(mContext.getResources().getColor(R.color.Green));
//                        } else if (answer.get(0).getAnswerText().equals(rbSubQuestion4.getText().toString())) {
//                            rbSubQuestion4.setTypeface(null, Typeface.BOLD_ITALIC);
//                            rbSubQuestion4.setTextColor(mContext.getResources().getColor(R.color.Green));
                        case "Multiple Choice": {
                            List<Answer> answer = mQuestions.get(
                                    tvQuestionId.getText().toString()).getListAnswers();
                            for (int i=1; i<=answer.size(); i++) {
                                switch (i) {
                                    case 1:
                                        tvAnswer1.setText("Answer 1. " + answer.get(0).getAnswerText());
                                        tvAnswer1.setVisibility(View.VISIBLE);
                                        break;
                                    case 2:
                                        tvAnswer2.setText("Answer 2. " + answer.get(1).getAnswerText());
                                        tvAnswer2.setVisibility(View.VISIBLE);
                                        break;
                                    case 3:
                                        tvAnswer3.setText("Answer 3. " + answer.get(2).getAnswerText());
                                        tvAnswer3.setVisibility(View.VISIBLE);
                                        break;
                                    case 4:
                                        tvAnswer4.setText("Answer 4. " + answer.get(3).getAnswerText());
                                        tvAnswer4.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                            break;
                        }
                    }
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                }
            });
        }
    }

    private Map<String, Question> convertListToMapOfQuestions(List<Question> mListQuestions) {
        Map<String, Question> mQuestions = new HashMap<>();
        for(Question question : mListQuestions) {
            mQuestions.put(Integer.toString(question.getQuestionId()), question);
        }
        return mQuestions;
    }

    private class AsyncConnectTask extends AsyncTask<Void, Void, Void> {
        private String qid;
        private String title;
        private String message;
        private AlertDialog dialog;
        public AsyncConnectTask(String mQid, String mTitle, String mMessage, AlertDialog mdialog) {
            this.qid = mQid;
            this.title = mTitle;
            this.message = mMessage;
            this.dialog = mdialog;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ServiceHandler jsonParser = new ServiceHandler();
            Map<String, String> mapConditions = new HashMap<>();
            mapConditions.put("qid", this.qid);
            mapConditions.put("title", this.title);
            mapConditions.put("message", this.message);
            jsonParser.makeServiceCall(mContext.getString((R.string.report_question)),
                    ServiceHandler.POST, mapConditions);
            dialog.cancel();
            return null;
        }
    }
}
