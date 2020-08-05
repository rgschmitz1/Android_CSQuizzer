package edu.tacoma.uw.csquizzer.model;

public class Answer {
    private int AnswerId;
    private int QuestionId;
    private String AnswerText;

    public Answer(int answerId, int questionId, String answerText) {
        AnswerId = answerId;
        QuestionId = questionId;
        AnswerText = answerText;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getAnswerText() {
        return AnswerText;
    }

    public void setAnswerText(String answerText) {
        AnswerText = answerText;
    }
}
