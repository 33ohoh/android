package com.example.competition1;

public class ReportHistory {
    private String title;
    private String date;
    private String status;

    public ReportHistory(String title, String date, String status){
        this.title = title;
        this.date = date;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getStatus() {
        return status;
    }
}
