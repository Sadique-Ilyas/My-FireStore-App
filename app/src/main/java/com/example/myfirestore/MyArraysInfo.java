package com.example.myfirestore;

import java.util.List;

public class MyArraysInfo
{
    private String firstName, surName, documentId;
    private int priority;
    List<String> tags;

    public MyArraysInfo(){}

    public MyArraysInfo(String firstName, String surName, int priority, List<String> tags) {
        this.firstName = firstName;
        this.surName = surName;
        this.priority = priority;
        this.tags = tags;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
