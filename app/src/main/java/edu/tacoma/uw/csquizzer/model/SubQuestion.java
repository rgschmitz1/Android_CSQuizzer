package edu.tacoma.uw.csquizzer.model;

public class SubQuestion {
    private int SubQuestionId;
    private int QuestionId;
    private String SubQuestionText;

    public SubQuestion(int subQuestionId, int questionId, String subQuestionText) {
        SubQuestionId = subQuestionId;
        QuestionId = questionId;
        SubQuestionText = subQuestionText;
    }

    public int getSubQuestionId() {
        return SubQuestionId;
    }

    public void setSubQuestionId(int subQuestionId) {
        SubQuestionId = subQuestionId;
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int questionId) {
        QuestionId = questionId;
    }

    public String getSubQuestionText() {
        return SubQuestionText;
    }

    public void setSubQuestionText(String subQuestionText) {
        SubQuestionText = subQuestionText;
    }
}
