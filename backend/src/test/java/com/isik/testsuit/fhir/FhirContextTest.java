package com.isik.testsuit.fhir;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireContextTest {

    @Test
    void testSingletonInstance() {
        FireContext instance1 = FireContext.getInstance();
        FireContext instance2 = FireContext.getInstance();

        assertSame(instance1, instance2, "FireContext should be a singleton");
    }

    @Test
    void testFhirContextNotNull() {
        FireContext fireContext = FireContext.getInstance();

        assertNotNull(fireContext.getCtx(), "FhirContext should not be null");
    }
}
