package edu.tacoma.uw.csquizzer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddQuestionFragment extends Fragment {
    private Context context;
    private ImageButton tvBackToList;
    private Spinner spinnerChooseQuestionTypes;
    private Button btnSubmitQuestionTypes;

    public AddQuestionFragment(Context mContext) {
        this.context = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_question, container, false);
        spinnerChooseQuestionTypes = rootView.findViewById(R.id.sn_types);
        btnSubmitQuestionTypes = rootView.findViewById(R.id.btn_SubmitQuestionTypes);
        btnSubmitQuestionTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!spinnerChooseQuestionTypes.getSelectedItem().equals("--- Choose Type Questions ---")) {
                    if(spinnerChooseQuestionTypes.getSelectedItem().equals("True/False")) {
                        AddQuestionTrueFalseFragment addQuestionTrueFalseFragment =
                                new AddQuestionTrueFalseFragment(context, "1");
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        //Replace current fragment with a show question fragment
                        ft.replace(R.id.fragment_container, addQuestionTrueFalseFragment);
                        ft.commit();
                    } else if (spinnerChooseQuestionTypes.getSelectedItem().equals("Single Choice")) {
                        AddQuestionSingleChoiceFragment addQuestionSingleChoiceFragment =
                                new AddQuestionSingleChoiceFragment(context, "2");
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        //Replace current fragment with a show question fragment
                        ft.replace(R.id.fragment_container, addQuestionSingleChoiceFragment);
                        ft.commit();
                    } else if (spinnerChooseQuestionTypes.getSelectedItem().equals("Multiple Choice")) {
                        AddQuestionMultipleChoiceFragment addQuestionMultipleChoiceFragment =
                                new AddQuestionMultipleChoiceFragment(context, "3");
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        //Replace current fragment with a show question fragment
                        ft.replace(R.id.fragment_container, addQuestionMultipleChoiceFragment);
                        ft.commit();
                    }
                } else {
                    Toast.makeText(context, "Please choose a question type", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvBackToList = rootView.findViewById(R.id.imb_back_to_list);
        tvBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionFragment questionFragment =  new QuestionFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, questionFragment);
                ft.commit();
            }
        });
        return rootView;
    }
}