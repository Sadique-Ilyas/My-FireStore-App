package com.example.myfirestore;

import java.util.List;
import java.util.Map;

public class NestedInfo
{
    private String firstName, surName, documentId;
    private int priority;
    Map<String, Boolean> tags;

    public NestedInfo(){}

    public NestedInfo(String firstName, String surName, int priority, Map<String, Boolean> tags) {
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

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }
}
