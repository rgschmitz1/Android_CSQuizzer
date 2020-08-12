package edu.tacoma.uw.csquizzer;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import edu.tacoma.uw.csquizzer.authentication.RegistrationFragment;
import edu.tacoma.uw.csquizzer.model.SubQuestion;

/**
 * The RepositoryFragment is placed in MainActivity.
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class RepositoryFragment extends Fragment {
    /**
     * Render components to GUI
     * @param inflater a class used to instantiate layout XML file into its corresponding view objects
     * @param container a special view that can contain other views
     * @param savedInstanceState a reference to a Bundle object that is passed into the onCreate method.
     * @return view
     *
     * @author  Phuc Pham N
     * @version 1.0
     * @since   2020-08-05
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_repository, container, false);

        ImageButton imbUser = (ImageButton) view.findViewById(R.id.imb_users);
        imbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserFragment userFragment =  new UserFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, userFragment);
                ft.commit();
            }
        });

        ImageButton imbTopic = (ImageButton) view.findViewById(R.id.imb_topics);
        imbTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TopicFragment topicFragment =  new TopicFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, topicFragment);
                ft.commit();
            }
        });

        ImageButton imbCourse = (ImageButton) view.findViewById(R.id.imb_courses);
        imbCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseFragment courseFragment =  new CourseFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, courseFragment);
                ft.commit();
            }
        });

        ImageButton imbQuestion = (ImageButton) view.findViewById(R.id.imb_questions);
        imbQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionFragment questionFragment =  new QuestionFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, questionFragment);
                ft.commit();
            }
        });

        ImageButton imbAnswer = (ImageButton) view.findViewById(R.id.imb_answers);
        imbAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnswerFragment answerFragment =  new AnswerFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, answerFragment);
                ft.commit();
            }
        });

        ImageButton imbSubquestion = (ImageButton) view.findViewById(R.id.imb_subquestions);
        imbSubquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubQuestionFragment subQuestionFragment =  new SubQuestionFragment();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                //Replace current fragment with a show question fragment
                ft.replace(R.id.fragment_container, subQuestionFragment);
                ft.commit();
            }
        });

        return view;
    }
}