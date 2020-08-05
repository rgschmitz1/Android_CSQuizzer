package edu.tacoma.uw.csquizzer.model;

public class Type {
    private int TypeId;
    private String TypeDescription;

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
