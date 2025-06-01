package com.isik.testsuit.DokumentReference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isik.testsuit.config.ConfigLoader;
import com.isik.testsuit.config.FetchAccessToken;
import com.isik.testsuit.util.Util;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;


@Service
public class DocumentReferenceRecipient {

    private final RestTemplate restTemplate;

    private static final String CONTENT_TYPE = "application/json";

    public DocumentReferenceRecipient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * holt ein documentReference vonm ISiK Server ab und speichert es im Ordner src/main/resources/docs/
     * <p>
     * !!!Nur zum Test!!!
     *
     * @param documentReference
     */
    public void SaveDocFileSystem(DocumentReference documentReference) {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/pdf");

        org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);

        String fileUrl = null;
        Attachment attachment = documentReference.getContent().get(0).getAttachment();
        if (attachment.hasUrl()) {
            fileUrl = attachment.getUrl();
        }

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new IllegalArgumentException("Die Datei-URL ist ungültig oder fehlt.");
        }

        ResponseEntity<byte[]> res = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, byte[].class);

        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null && res.getBody().length > 0) {
            saveFile(res.getBody(), "downloaded_file.pdf");
            System.out.println("Datei erfolgreich heruntergeladen.");
        } else {
            System.err.println("Fehler: Die Datei konnte nicht heruntergeladen werden oder ist leer.");
        }
    }

    /**
     * holt ein documentReference vom ISiK Server ab und gibt es als Base64 zurück
     *
     * @param documentReference
     * @return
     */
    public String getDocAsBase64(DocumentReference documentReference) {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", CONTENT_TYPE);
        headers.set("Accept", "application/pdf");

        org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);

        String fileUrl = null;
        Attachment attachment = documentReference.getContent().get(0).getAttachment();
        if (attachment.hasUrl()) {
            fileUrl = attachment.getUrl();
        }

        ResponseEntity<byte[]> res = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, byte[].class);

        byte[] fileBytes = res.getBody();

        return Base64.getEncoder().encodeToString(fileBytes);
    }

    /**
     * holt ein URL Dokument vom ISiK Server ab und gibt es als Base64 zurück
     *
     * @return
     */
    public String getDocAsBase64ByUrl(String url) {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", CONTENT_TYPE);
        headers.set("Accept", "application/pdf");

        org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);

        String fileUrl = url;

        ResponseEntity<byte[]> res = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, byte[].class);

        byte[] fileBytes = res.getBody();

        return Base64.getEncoder().encodeToString(fileBytes);
    }

    /**
     * holt ein documentReference vom ISiK Server ab und gibt den MD5 Hash des Files zurück
     * <p>
     * für spätere Test Fälle
     *
     * @param documentReference
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String getHashfromDoc(DocumentReference documentReference) throws NoSuchAlgorithmException {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.set("Content-Type", CONTENT_TYPE);
        headers.set("Accept", "application/pdf");

        org.springframework.http.HttpEntity<String> entity = new HttpEntity<>(headers);

        String fileUrl = null;
        Attachment attachment = documentReference.getContent().get(0).getAttachment();
        if (attachment.hasUrl()) {
            fileUrl = attachment.getUrl();
        }

        ResponseEntity<byte[]> res = restTemplate.exchange(fileUrl, HttpMethod.GET, entity, byte[].class);

        byte[] fileBytes = res.getBody();

        return Util.calculateHash(fileBytes, ConfigLoader.getInstance().getHash());
    }


    /**
     * speichert mit einem byte[] und einem fileName im Ordner src/main/resources/docs/ ein File ab
     * <p>
     * !!!NUR ZUM TEST!!!
     *
     * @param fileBytes
     * @param fileName
     */
    private static void saveFile(byte[] fileBytes, String fileName) {
        try {
            String filePath = Paths.get("src/main/resources/docs/", fileName).toString();
            File file = new File(filePath);

            file.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileBytes);
            }

            System.out.println("Datei erfolgreich gespeichert: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
