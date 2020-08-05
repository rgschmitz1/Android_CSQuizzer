package edu.tacoma.uw.csquizzer.model;

public class Difficulty {
    private int DifficultiesId;
    private String DifficultiesDescription;

    /**
     * Difficulty Class
     * @param difficultiesId Difficulty Id
     * @param difficultiesDescription Difficulty Description
     */
    public Difficulty(int difficultiesId, String difficultiesDescription) {
        DifficultiesId = difficultiesId;
        DifficultiesDescription = difficultiesDescription;
    }

    public int getDifficultiesId() {
        return DifficultiesId;
    }

    public void setDifficultiesId(int difficultiesId) {
        DifficultiesId = difficultiesId;
    }

    public String getDifficultiesDescription() {
        return DifficultiesDescription;
    }

    public void setDifficultiesDescription(String difficultiesDescription) {
        DifficultiesDescription = difficultiesDescription;
    }
}
