package com.example.myfirestore;

import com.google.firebase.firestore.Exclude;

public class MyMultipleInfo
{
    private String firstName, surName, documentId;
    private int priority;

    public MyMultipleInfo(){}

    public MyMultipleInfo(String firstName, String surName, int priority) {
        this.firstName = firstName;
        this.surName = surName;
        this.priority = priority;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
