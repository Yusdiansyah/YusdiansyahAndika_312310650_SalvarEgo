package com.example.salvarego.model;

public class folder_item {
    private String foldername;

    public folder_item(String foldername){
        this.foldername = foldername;
    }

    public String getFoldername(){
        return foldername;
    }

    public void setFoldername(String foldername){
        this.foldername = foldername;
    }
}
