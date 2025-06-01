package com.isik.testsuit.util;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DocumentReference;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TestJsonCompaire {

    public static void main(String[] args) throws IOException {
        File tempFile = new File("pfad/zur/deiner/tempDatei.pdf");
        File cleanedFile = new File("pfad/zur/deiner/neuenDatei_cleaned.pdf");

        try {
            byte[] bytes = Files.readAllBytes(tempFile.toPath());
            String content = new String(bytes, StandardCharsets.UTF_8);

            boolean wasModified = false;

            // 1. OpenAction (Referenz auf Objekt) entfernen
            Pattern refPattern = Pattern.compile("/OpenAction\\s+\\d+\\s+\\d+\\s+R");
            Matcher refMatcher = refPattern.matcher(content);
            while (refMatcher.find()) {
                System.out.println("[INFO] Entferne OpenAction Referenz: " + refMatcher.group());
                wasModified = true;
            }
            content = refMatcher.replaceAll("");

            // 2. OpenAction (direktes Objekt) entfernen
            Pattern directPattern = Pattern.compile("/OpenAction\\s*<<.*?/S\\s*/Named.*?/N\\s*/Print.*?>>", Pattern.DOTALL);
            Matcher directMatcher = directPattern.matcher(content);
            while (directMatcher.find()) {
                System.out.println("[INFO] Entferne OpenAction Objekt: " + directMatcher.group());
                wasModified = true;
            }
            content = directMatcher.replaceAll("");

            // 3. Einzelne Einträge entfernen
            String[] singlePatterns = {"/S\\s*/Named", "/Type\\s*/Action", "/N\\s*/Print"};
            for (String singleRegex : singlePatterns) {
                Pattern p = Pattern.compile(singleRegex);
                Matcher m = p.matcher(content);
                while (m.find()) {
                    System.out.println("[INFO] Entferne Eintrag: " + m.group());
                    wasModified = true;
                }
                content = m.replaceAll("");
            }

            if (wasModified) {
                Files.write(cleanedFile.toPath(), content.getBytes(StandardCharsets.UTF_8));
                System.out.println("[INFO] Änderungen gespeichert in: " + cleanedFile.getAbsolutePath());
            } else {
                System.out.println("[INFO] Keine OpenAction-Einträge gefunden. Keine Änderungen vorgenommen.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[ERROR] Fehler beim Bearbeiten der Datei: " + tempFile.getAbsolutePath());
        }
    }
}
