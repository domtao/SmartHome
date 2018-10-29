package com.zunder.cusbutton;

public class RMCBean {
    private int Id;
    private String BtnName;
    private String BtnImage;
    private int RoomId;
    private String CreationTime;
    private int BtnSeqencing;
    private int CodeType=-1;
    private String BtnAction;
    private int BtnX;
    private int BtnY;
    private int BtnWidth;
    private int BtnHeight;
    private int BtnType;
    private int BtnSize;
    private String BtnColor;

    public int getBtnSize() {
        return BtnSize;
    }

    public void setBtnSize(int btnSize) {
        BtnSize = btnSize;
    }

    public String getBtnColor() {
        return BtnColor;
    }

    public void setBtnColor(String btnColor) {
        BtnColor = btnColor;
    }

    public int getBtnType() {
        return BtnType;
    }

    public void setBtnType(int btnType) {
        BtnType = btnType;
    }

    private int CompanyId;
    private int LanguageId;
    private String Data1;
    private String Data2;

    public RMCBean(){
    }

    public RMCBean(int Id, String BtnName,String BtnImage,  int BtnWidth, int BtnHeight,int BtnX, int BtnY){
        this.Id=Id;
        this.BtnName=BtnName;
        this.BtnImage=BtnImage;
        this.BtnX=BtnX;
        this.BtnY=BtnY;
        this.BtnWidth=BtnWidth;
        this.BtnHeight=BtnHeight;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBtnName() {
        return BtnName;
    }

    public void setBtnName(String btnName) {
        BtnName = btnName;
    }

    public String getBtnImage() {
        return BtnImage;
    }

    public void setBtnImage(String btnImage) {
        BtnImage = btnImage;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }

    public String getCreationTime() {
        return CreationTime;
    }

    public void setCreationTime(String creationTime) {
        CreationTime = creationTime;
    }

    public int getBtnSeqencing() {
        return BtnSeqencing;
    }

    public void setBtnSeqencing(int btnSeqencing) {
        BtnSeqencing = btnSeqencing;
    }

    public int getCodeType() {
        return CodeType;
    }

    public void setCodeType(int codeType) {
        CodeType = codeType;
    }

    public String getBtnAction() {
        return BtnAction;
    }

    public void setBtnAction(String btnAction) {
        BtnAction = btnAction;
    }

    public int getBtnX() {
        return BtnX;
    }

    public void setBtnX(int btnX) {
        BtnX = btnX;
    }

    public int getBtnY() {
        return BtnY;
    }

    public void setBtnY(int btnY) {
        BtnY = btnY;
    }

    public int getBtnWidth() {
        return BtnWidth;
    }

    public void setBtnWidth(int btnWidth) {
        BtnWidth = btnWidth;
    }

    public int getBtnHeight() {
        return BtnHeight;
    }

    public void setBtnHeight(int btnHeight) {
        BtnHeight = btnHeight;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public int getLanguageId() {
        return LanguageId;
    }

    public void setLanguageId(int languageId) {
        LanguageId = languageId;
    }

    public String getData1() {
        return Data1;
    }

    public void setData1(String data1) {
        Data1 = data1;
    }

    public String getData2() {
        return Data2;
    }

    public void setData2(String data2) {
        Data2 = data2;
    }



}
