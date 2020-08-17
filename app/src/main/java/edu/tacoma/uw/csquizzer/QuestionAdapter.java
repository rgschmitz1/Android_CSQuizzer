package edu.tacoma.uw.csquizzer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.Arrays;
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
     * @param position the current position of the recycler.
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
     * Render view on recycler.
     *
     * @param holder recycler
     * @param position type of question
     *
     * @author  Phuc Pham N
     * @since   2020-08-05
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /*
         * Attach data in first load, we have three templates
         * and the different between them is type of questions (true/false, single choice, multiple choice)
         */
        // Get question at position in the question list
        Question question = lQuestions.get(position);
        holder.tvQuestionId.setText(question.getQuestionId() + "");
        holder.tvCountNumber.setText((position + 1) + ". ");
        holder.tvQuestionTitle.setText(" " + question.getQuestionTitle());
        holder.tvCourseName.setText(question.getCourseName());
        holder.tvTopicDescription.setText(question.getTopicDescription());
        holder.tvDifficultyDescription.setText(question.getDifficultyDescription());
        holder.tvQuestionBody.setText(question.getQuestionBody());
        if (question.getTypeDescription().equals("True/False")) {
            holder.rbTrue.setText(R.string.tv_true);
            holder.rbFalse.setText(R.string.tv_false);
        } else if (question.getTypeDescription().equals("Single Choice")) {
            if(question.getListSubQuestions().get(0) == null) {
                holder.rbSubQuestion1.setText("Please report question error");
            } else {
                holder.rbSubQuestion1.setText(question.getListSubQuestions().get(0).getSubQuestionText());
            }

            if(question.getListSubQuestions().get(1) == null) {
                holder.rbSubQuestion2.setText("Please report question error");
            } else {
                holder.rbSubQuestion2.setText(question.getListSubQuestions().get(1).getSubQuestionText());
            }
            if(question.getListSubQuestions().get(2) == null) {
                holder.rbSubQuestion3.setText("Please report question error");
            } else {
                holder.rbSubQuestion3.setText(question.getListSubQuestions().get(2).getSubQuestionText());
            }

            if (question.getListSubQuestions().get(3) == null) {
                holder.rbSubQuestion4.setText("Please report question error");
            } else {
                holder.rbSubQuestion4.setText(question.getListSubQuestions().get(3).getSubQuestionText());
            }
        } else if (lQuestions.get(position).getTypeDescription().equals("Multiple Choice")) {
            if(question.getListSubQuestions().get(0) == null) {
                holder.cbSubQuestion1.setText("Please report question error");
            } else {
                holder.cbSubQuestion1.setText(question.getListSubQuestions().get(0).getSubQuestionText());
            }

            if(question.getListSubQuestions().get(1) == null) {
                holder.cbSubQuestion2.setText("Please report question error");
            } else {
                holder.cbSubQuestion2.setText(question.getListSubQuestions().get(1).getSubQuestionText());
            }
            if(question.getListSubQuestions().get(2) == null) {
                holder.cbSubQuestion3.setText("Please report question error");
            } else {
                holder.cbSubQuestion3.setText(question.getListSubQuestions().get(2).getSubQuestionText());
            }

            if (question.getListSubQuestions().get(3) == null) {
                holder.cbSubQuestion4.setText("Please report question error");
            } else {
                holder.cbSubQuestion4.setText(question.getListSubQuestions().get(3).getSubQuestionText());
            }
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
            final Button btnReport = itemView.findViewById(R.id.btn_Report);
            final Button btnShowAnswer = itemView.findViewById(R.id.btn_ShowAnswer);
            if(viewType == TYPE1) {
                this.rbTrue = itemView.findViewById(R.id.radio_true);
                this.rbFalse = itemView.findViewById(R.id.radio_false);
                this.relativeLayout = itemView.findViewById(R.id.template_true_false);
            } else if (viewType == TYPE2) {
                this.rbSubQuestion1 = itemView.findViewById(R.id.radio_subquestion_1);
                this.rbSubQuestion2 = itemView.findViewById(R.id.radio_subquestion_2);
                this.rbSubQuestion3 = itemView.findViewById(R.id.radio_subquestion_3);
                this.rbSubQuestion4 = itemView.findViewById(R.id.radio_subquestion_4);
                this.relativeLayout = itemView.findViewById(R.id.template_single_choice);
            } else if (viewType == TYPE3) {
                this.cbSubQuestion1 = itemView.findViewById(R.id.cb_subquestion_1);
                this.cbSubQuestion2 = itemView.findViewById(R.id.cb_subquestion_2);
                this.cbSubQuestion3 = itemView.findViewById(R.id.cb_subquestion_3);
                this.cbSubQuestion4 = itemView.findViewById(R.id.cb_subquestion_4);
                this.relativeLayout = itemView.findViewById(R.id.template_multiple_choice);
            }
            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                builder.setView(inflater.inflate(R.layout.dialog_report, null));
                final AlertDialog dialog = builder.create();
                dialog.show();
                final EditText etReport = dialog.findViewById(R.id.et_report);
                final Button btnOK = dialog.findViewById(R.id.btn_ok);
                final Button btnCancel = dialog.findViewById(R.id.btn_cancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    if(etReport.getText().toString().length() > 0) {
                        AsyncConnectTask task = new AsyncConnectTask(
                                tvQuestionId.getText().toString(),
                                tvQuestionTitle.getText().toString(),
                                etReport.getText().toString(), dialog);
                        task.execute();
                        Toast.makeText(mContext, "Send report successful", Toast.LENGTH_SHORT)
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
                        dialog.cancel();
                    }
                });
                }
            });

            btnShowAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // Disable the check answer button once the user selects it
                Map<String, Question> mQuestions = convertListToMapOfQuestions(lQuestions);
                List<Answer> answer = mQuestions.get(tvQuestionId.getText().toString()).getListAnswers();
                String answerText = answer.get(0).getAnswerText();
                List<RadioButton> radioButtons = new ArrayList<>();
                final int green = mContext.getResources().getColor(R.color.Green);
                final int red = mContext.getResources().getColor(R.color.Red);

                // Display correct answer in green, display incorrect selection in red
                switch (mQuestions.get(tvQuestionId.getText().toString()).getTypeDescription()) {
                    case "True/False":
                        if (!rbTrue.isChecked() && !rbFalse.isChecked()) {
                            Toast.makeText(mContext, "Make a selection and try again.",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        btnShowAnswer.setEnabled(false);
                        radioButtons.addAll(Arrays.asList(rbTrue, rbFalse));
                        for (RadioButton radioButton : radioButtons) {
                            radioButton.setEnabled(false);
                            if (answerText.equals(radioButton.getText().toString())) {
                                radioButton.setTypeface(null, Typeface.BOLD_ITALIC);
                                radioButton.setTextColor(green);
                            } else if (radioButton.isChecked()) {
                                radioButton.setTextColor(red);
                            }
                        }
                        break;
                    case "Single Choice":
                        if (!rbSubQuestion1.isChecked() &&
                                !rbSubQuestion2.isChecked() &&
                                !rbSubQuestion3.isChecked() &&
                                !rbSubQuestion4.isChecked()) {
                            Toast.makeText(mContext, "Make a selection and try again.",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        btnShowAnswer.setEnabled(false);
                        radioButtons.addAll(Arrays.asList(rbSubQuestion1, rbSubQuestion2,
                                rbSubQuestion3, rbSubQuestion4));
                        for (RadioButton radioButton : radioButtons) {
                            radioButton.setEnabled(false);
                            if (answerText.equals(radioButton.getText().toString())) {
                                radioButton.setTypeface(null, Typeface.BOLD_ITALIC);
                                radioButton.setTextColor(green);
                            } else if (radioButton.isChecked()) {
                                radioButton.setTextColor(red);
                            }
                        }
                        break;
                    case "Multiple Choice":
                        if (!cbSubQuestion1.isChecked() &&
                                !cbSubQuestion2.isChecked() &&
                                !cbSubQuestion3.isChecked() &&
                                !cbSubQuestion4.isChecked()) {
                            Toast.makeText(mContext, "Make a selection and try again.",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        }
                        btnShowAnswer.setEnabled(false);
                        List<CheckBox> checkBoxes = new ArrayList<>(
                                Arrays.asList(cbSubQuestion1, cbSubQuestion2,
                                cbSubQuestion3, cbSubQuestion4));
                        for (int i = 0; i < checkBoxes.size(); i++) {
                            CheckBox checkBox = checkBoxes.get(i);
                            checkBox.setEnabled(false);
                            if (checkBox.isChecked())
                                checkBox.setTextColor(red);
                            for (int j = 0; j < answer.size(); j++) {
                                if (checkBox.getText().toString().equals(
                                        answer.get(j).getAnswerText())) {
                                    checkBox.setTypeface(null, Typeface.BOLD_ITALIC);
                                    checkBox.setTextColor(green);
                                    break;
                                }
                            }
                        }
                        break;
                }
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

    /**
     * Report a question
     */
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