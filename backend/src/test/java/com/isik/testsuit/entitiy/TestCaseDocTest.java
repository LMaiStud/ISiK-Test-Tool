package com.isik.testsuit.entitiy;

import com.isik.testsuit.entity.TestCaseDoc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TestCaseDocTest {

    private TestCaseDoc testCaseDoc;

    @BeforeEach
    void setUp() {
        testCaseDoc = new TestCaseDoc();
    }

    @Test
    void testDefaultConstructor() {
        assertThat(testCaseDoc.getDocID()).isEqualTo(0);
        assertThat(testCaseDoc.isTestType()).isFalse();
    }

    @Test
    void testParameterizedConstructor() {
        TestCaseDoc doc = new TestCaseDoc(42, true);
        assertThat(doc.getDocID()).isEqualTo(42);
        assertThat(doc.isTestType()).isTrue();
    }

    @Test
    void testSettersAndGetters() {
        testCaseDoc.setDocID(99);
        testCaseDoc.setTestType(true);

        assertThat(testCaseDoc.getDocID()).isEqualTo(99);
        assertThat(testCaseDoc.isTestType()).isTrue();
    }
}
