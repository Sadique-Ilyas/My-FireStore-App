package com.example.myfirestore;

import com.google.firebase.firestore.Exclude;

public class MyInfo
{
    private String firstName, surName, documentId;

    public MyInfo(){}

    public MyInfo(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
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
}
