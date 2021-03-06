package edu.tacoma.uw.csquizzer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Question class
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class Question {
    private int QuestionId;
    private String QuestionTitle;
    private String QuestionBody;
    private String CourseName;
    private String TopicDescription;
    private String DifficultyDescription;
    private String TypeDescription;
    private List<Answer> listAnswers = new ArrayList<>();
    private List<SubQuestion> listSubQuestions = new ArrayList<>();
    public Question() {}
    /**
     * Question class relative to answer class and subquestion class
     * @param questionId Question Id
     * @param questionTitle Question Title
     * @param questionBody Question Body
     * @param courseName Course Name
     * @param topicDescription Topic Description
     * @param difficultyDescription Difficulty Description
     * @param typeDescription Type Description
     * @param answers List Answers having the same Question ID
     * @param subQuestions List Questions having the same Question ID
     */
    public Question(int questionId, String questionTitle, String questionBody, String courseName,
                    String topicDescription, String difficultyDescription, String typeDescription,
                    List<Answer> answers, List<SubQuestion> subQuestions) {
        QuestionId = questionId;
        QuestionTitle = questionTitle;
        QuestionBody = questionBody;
        CourseName = courseName;
        TopicDescription = topicDescription;
        DifficultyDescription = difficultyDescription;
        TypeDescription = typeDescription;
        listAnswers = answers;
        listSubQuestions = subQuestions;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        QuestionTitle = questionTitle;
    }

    public String getQuestionBody() {
        return QuestionBody;
    }

    public void setQuestionBody(String questionBody) {
        QuestionBody = questionBody;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public String getTopicDescription() {
        return TopicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        TopicDescription = topicDescription;
    }

    public String getDifficultyDescription() {
        return DifficultyDescription;
    }

    public void setDifficultyDescription(String difficultyDescription) {
        DifficultyDescription = difficultyDescription;
    }

    public String getTypeDescription() {
        return TypeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        TypeDescription = typeDescription;
    }

    public List<Answer> getListAnswers() {
        return listAnswers;
    }

    public void setListAnswers(List<Answer> listAnswers) {
        this.listAnswers = listAnswers;
    }

    public List<SubQuestion> getListSubQuestions() {
        return listSubQuestions;
    }

    public void setListSubQuestions(List<SubQuestion> listSubQuestions) {
        this.listSubQuestions = listSubQuestions;
    }
}
