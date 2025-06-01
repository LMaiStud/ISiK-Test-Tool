package com.isik.testsuit.entitiy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.isik.testsuit.entity.TestCase;
import com.isik.testsuit.entity.TestCaseDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class TestCaseTest {

    private TestCase testCase;

    @BeforeEach
    void setUp() {
        testCase = new TestCase();
    }

    @Test
    void testSettersAndGetters() {
        testCase.setId(1L);
        testCase.setTestName("Sample Test");
        testCase.setTestDescription("This is a test case.");
        testCase.setStatus(1);
        testCase.setResult(100);
        testCase.setException("No exception");

        assertThat(testCase.getId()).isEqualTo(1L);
        assertThat(testCase.getTestName()).isEqualTo("Sample Test");
        assertThat(testCase.getTestDescription()).isEqualTo("This is a test case.");
        assertThat(testCase.getStatus()).isEqualTo(1);
        assertThat(testCase.getResult()).isEqualTo(100);
        assertThat(testCase.getException()).isEqualTo("No exception");
    }

    @Test
    void testJsonSerialization() throws JsonProcessingException {
        TestCaseDoc doc1 = new TestCaseDoc(1, true);
        TestCaseDoc doc2 = new TestCaseDoc(2, true);

        List<TestCaseDoc> docs = Arrays.asList(doc1, doc2);
        testCase.setTestCaseDocs(docs);

        String json = testCase.getTestCaseDocsJson();
        assertThat(json).contains("{\"docID\":1,\"testType\":true}").contains("{\"docID\":2,\"testType\":true}");

        List<TestCaseDoc> deserializedDocs = testCase.getTestCaseDocs();
        assertThat(deserializedDocs).hasSize(2);
        assertThat(deserializedDocs.get(0).getDocID()).isEqualTo(1);
        assertThat(deserializedDocs.get(1).getDocID()).isEqualTo(2);
        assertThat(deserializedDocs.get(0).isTestType()).isEqualTo(true);
        assertThat(deserializedDocs.get(1).isTestType()).isEqualTo(true);
    }

    @Test
    void testEmptyTestCase() {
        assertThat(testCase.getId()).isNull();
        assertThat(testCase.getTestName()).isNull();
        assertThat(testCase.getTestDescription()).isNull();
        assertThat(testCase.getStatus()).isEqualTo(0);
        assertThat(testCase.getResult()).isNull();
        assertThat(testCase.getException()).isNull();
    }
}
