package com.isik.testsuit.entity;

public class TestCaseDoc {


    int docID;

    boolean testType;

    public TestCaseDoc() {
    }

    public TestCaseDoc(int docID, boolean testType) {
        this.docID = docID;
        this.testType = testType;
    }

    public int getDocID() {
        return docID;
    }

    public boolean isTestType() {
        return testType;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public void setTestType(boolean testType) {
        this.testType = testType;
    }
}
