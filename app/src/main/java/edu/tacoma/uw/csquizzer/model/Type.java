package edu.tacoma.uw.csquizzer.model;

/**
 * The Type class
 *
 * @author  Phuc Pham N
 * @version 1.0
 * @since   2020-08-05
 */
public class Type {
    private int TypeId;
    private String TypeDescription;

    /**
     * Type Class
     * @param typeId Type Id
     * @param typeDescription Type Description
     */
    public Type(int typeId, String typeDescription) {
        TypeId = typeId;
        TypeDescription = typeDescription;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getTypeDescription() {
        return TypeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        TypeDescription = typeDescription;
    }
}
