package edu.tacoma.uw.csquizzer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.tacoma.uw.csquizzer.model.Course;
import edu.tacoma.uw.csquizzer.model.Difficulty;
import edu.tacoma.uw.csquizzer.model.Topic;
import edu.tacoma.uw.csquizzer.model.Type;


public class AddQuestionMultipleChoiceFragment extends Fragment {
    private Context context;
    private List<Course> lCourses;
    private List<Topic> lTopics;
    private List<Difficulty> lDifficulties;
    private List<Type> lTypes;
    private String typeId;
    ProgressDialog pDialog;

    public AddQuestionMultipleChoiceFragment(Context mContext, String mTypeId) {
        this.context = mContext;
        this.typeId = mTypeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_question_multiple_choice, container, false);
    }
}