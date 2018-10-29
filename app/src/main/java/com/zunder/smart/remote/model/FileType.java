package com.zunder.smart.remote.model;

/**
 * Created by Administrator on 2018/4/4.
 */

public class FileType {
    private int Id ;
    private String TypeName ;
    private String TypeImage ;
    private String TypeText ;
    private int TypeId ;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getTypeImage() {
        return TypeImage;
    }

    public void setTypeImage(String typeImage) {
        TypeImage = typeImage;
    }

    public String getTypeText() {
        return TypeText;
    }

    public void setTypeText(String typeText) {
        TypeText = typeText;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }
}
