package edu.tacoma.uw.csquizzer.model;

/**
 * The Difficulty class
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class Difficulty {
    private int DifficultiesId;
    private String DifficultiesDescription;

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
