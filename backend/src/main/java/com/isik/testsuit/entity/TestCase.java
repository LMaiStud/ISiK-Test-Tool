package com.isik.testsuit.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.List;

@Entity
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String testCaseDocsJson;

    @Transient
    private List<TestCaseDoc> testCaseDocs;
    private String testName;

    private String testDescription;

    /**
     * 1 erfolgreich durchgeführt; 2 läuft gerade; 3 fehlgeschlagen; 0 noch nicht gelaufen
     */
    private int status;

    public String getTestCaseDocsJson() {
        return testCaseDocsJson;
    }

    public void setTestCaseDocsJson(String testCaseDocsJson) {
        this.testCaseDocsJson = testCaseDocsJson;
        this.testCaseDocs = deserializeJson(testCaseDocsJson);
    }

    public List<TestCaseDoc> getTestCaseDocs() {
        testCaseDocs = deserializeJson(getTestCaseDocsJson());
        return testCaseDocs;
    }

    public void setTestCaseDocs(List<TestCaseDoc> testCaseDocs) throws JsonProcessingException {
        this.testCaseDocs = testCaseDocs;
        this.testCaseDocsJson = serializeToJson(testCaseDocs);
    }

    private String serializeToJson(List<TestCaseDoc> testCaseDocs) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(testCaseDocs);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Serialisieren", e);
        }
    }

    private List<TestCaseDoc> deserializeJson(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<List<TestCaseDoc>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Deserialisieren", e);
        }
    }

    private Integer result;

    @Lob
    private String exception;

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}

