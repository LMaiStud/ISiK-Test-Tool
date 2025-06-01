package com.isik.testsuit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isik.testsuit.entity.TestCase;
import com.isik.testsuit.entity.TestCaseDoc;
import com.isik.testsuit.repository.TestCaseRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestCaseService {

    private final TestCaseRepository testCaseRepository;

    private final String kdlUrl = "http://dvmd.de/fhir/CodeSystem/kdl";

    public TestCaseService(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }


    public TestCase save(String testName, String testDescription, int status, int result, int[] doc, boolean[] testType) throws IOException, NoSuchAlgorithmException {
        TestCase testCase = new TestCase();
        List<TestCaseDoc> testCases = new ArrayList<>();
        for (int i = 0; i < doc.length; i++) {
            TestCaseDoc testCaseDoc = new TestCaseDoc(doc[i], testType[i]);
            testCases.add(testCaseDoc);
        }

        testCase.setTestCaseDocs(testCases);
        testCase.setTestDescription(testDescription);
        testCase.setResult(result);
        testCase.setTestName(testName);

        testCaseRepository.save(testCase);
        return testCase;
    }

    public TestCase update(Long id, String testName, String testDescription, int status, int result, int[] doc, boolean[] testType) throws JsonProcessingException {
        Optional<TestCase> testCase = testCaseRepository.findById(id);
        TestCase update = testCase.get();

        List<TestCaseDoc> testCases = new ArrayList<>();
        for (int i = 0; i < doc.length; i++) {
            TestCaseDoc testCaseDoc = new TestCaseDoc(doc[i], testType[i]);
            testCases.add(testCaseDoc);
        }

        update.setTestCaseDocs(testCases);
        update.setTestDescription(testDescription);
        update.setResult(result);
        update.setTestName(testName);

        testCaseRepository.save(update);
        return update;
    }

    public int getStatus(Long id) {
        Optional<TestCase> testCase = testCaseRepository.findById(id);
        TestCase update = testCase.get();

        return update.getStatus();
    }

    public Optional<TestCase> findById(Long id) {
        return testCaseRepository.findById(id);
    }

    public List<TestCase> getAll() {
        return testCaseRepository.findAll();
    }

    public boolean deleteById(Long id) {
        testCaseRepository.deleteById(id);
        return true;
    }
}
