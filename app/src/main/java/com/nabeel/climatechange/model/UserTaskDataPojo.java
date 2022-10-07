package com.nabeel.climatechange.model;

public class UserTaskDataPojo {
    private String task_completion_msg;
    private String date;
    private String status;

    public UserTaskDataPojo() {}

    public UserTaskDataPojo(String task_completion_msg, String date, String status) {
        this.task_completion_msg = task_completion_msg;
        this.date = date;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTask_completion_msg() {
        return task_completion_msg;
    }

    public void setTask_completion_msg(String task_completion_msg) {
        this.task_completion_msg = task_completion_msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
