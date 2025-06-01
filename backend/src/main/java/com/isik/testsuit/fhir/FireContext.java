package com.isik.testsuit.fhir;

import ca.uhn.fhir.context.FhirContext;

public class FireContext {
    private static FireContext instance;
    private FhirContext ctx;

    /**
     * Singleton, der die FireContext-Klasse bereitstellt
     */
    private FireContext() {
        ctx = new FhirContext();
    }

    public static synchronized FireContext getInstance() {
        if (instance == null) {
            instance = new FireContext();
        }
        return instance;
    }

    public FhirContext getCtx() {
        return ctx;
    }
}
