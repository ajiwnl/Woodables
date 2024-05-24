package com.intprog.woodablesapp;

import com.google.firebase.firestore.DocumentId;

public class Assessment {
    @DocumentId
    private String id;
    private String course;
    private String dateOfAssessment;
    private String desc7;
    private String educ;
    private String exp_1;
    private String exp_2;
    private String expertise;
    private String firstName;
    private String lastName;
    private String location;
    private String middleName;

    // Empty constructor needed for Firestore
    public Assessment() {}

    // Getters and setters for all fields
    public String getId() { return id; }
    public String getCourse() { return course; }
    public String getDateOfAssessment() { return dateOfAssessment; }
    public String getDesc7() { return desc7; }
    public String getEduc() { return educ; }
    public String getExp_1() { return exp_1; }
    public String getExp_2() { return exp_2; }
    public String getExpertise() { return expertise; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getLocation() { return location; }
    public String getMiddleName() { return middleName; }
}