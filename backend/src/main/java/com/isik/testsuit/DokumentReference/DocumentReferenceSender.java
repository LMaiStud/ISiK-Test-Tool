package com.isik.testsuit.DokumentReference;

import com.isik.testsuit.config.ConfigLoader;
import com.isik.testsuit.config.FetchAccessToken;
import com.isik.testsuit.fhir.FireContext;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.DocumentReference;

@Service
public class DocumentReferenceSender {
    private static final String CONTENT_TYPE = "application/json";

    private FhirContext ctx;
    private final RestTemplate restTemplate;

    public DocumentReferenceSender(RestTemplate restTemplate) {
        this.ctx = FireContext.getInstance().getCtx();
        this.restTemplate = restTemplate;
    }

    /**
     * Legt das Dokument in der json über die ISiK Schnittstelle ab
     *
     * @param json
     * @return
     */
    public ResponseEntity<String> send(String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", CONTENT_TYPE);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        return restTemplate.exchange(ConfigLoader.getInstance().getBaseUrl() + ConfigLoader.getInstance().getPort() + ConfigLoader.getInstance().getEndPointISiK(), HttpMethod.POST, entity, String.class);
    }


    /**
     * Legt das Dokument in der documentReference über die ISiK Schnittstelle ab
     *
     * @param documentReference
     * @return
     */
    public ResponseEntity<String> send(DocumentReference documentReference) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", CONTENT_TYPE);

        HttpEntity<String> entity = new HttpEntity<>(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(documentReference), headers);
        
        return restTemplate.exchange(ConfigLoader.getInstance().getBaseUrl() + ConfigLoader.getInstance().getPort() + ConfigLoader.getInstance().getEndPointISiK(), HttpMethod.POST, entity, String.class);
    }


}

