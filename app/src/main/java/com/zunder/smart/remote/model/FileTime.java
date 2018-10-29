package com.zunder.smart.remote.model;

import java.sql.Time;

/**
 * Created by Administrator on 2018/5/7.
 */

public class FileTime {
    private int Id;
    private String TimeName;
    private String StartTime;
    private String EndTime;
    private int Cycle;
    private String AssignDate;
    private String CreateTime;
    private String MasterID;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public int getCycle() {
        return Cycle;
    }

    public void setCycle(int cycle) {
        Cycle = cycle;
    }

    public String getAssignDate() {
        return AssignDate;
    }

    public void setAssignDate(String assignDate) {
        AssignDate = assignDate;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMasterID() {
        return MasterID;
    }

    public void setMasterID(String masterID) {
        MasterID = masterID;
    }

    public String getTimeName() {
        if(TimeName==null|| TimeName.equals("null")||TimeName=="null"){
            TimeName="未定义";
        }
        return TimeName;
    }

    public void setTimeName(String timeName) {
        TimeName = timeName;
    }
}