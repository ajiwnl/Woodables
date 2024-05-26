package com.intprog.woodablesapp;

public class Listing {
    private String companyName;
    private String jobTitle;
    private String payRange;
    private String details;
    private String requirements1;
    private String requirements2;
    private String requirements3;

    public Listing() {
        // Default constructor required for Firestore
    }

    public Listing(String companyName, String jobTitle, String payRange, String details, String requirements1, String requirements2, String requirements3) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.payRange = payRange;
        this.details = details;
        this.requirements1 = requirements1;
        this.requirements2 = requirements2;
        this.requirements3 = requirements3;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPayRange() {
        return payRange;
    }

    public void setPayRange(String payRange) {
        this.payRange = payRange;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRequirements1() {
        return requirements1;
    }

    public void setRequirements1(String requirements1) {
        this.requirements1 = requirements1;
    }

    public String getRequirements2() {
        return requirements2;
    }

    public void setRequirements2(String requirements2) {
        this.requirements2 = requirements2;
    }

    public String getRequirements3() {
        return requirements3;
    }

    public void setRequirements3(String requirements3) {
        this.requirements3 = requirements3;
    }
}