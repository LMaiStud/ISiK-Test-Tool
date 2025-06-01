package com.isik.testsuit.controller;

import com.isik.testsuit.DokumentReference.DocumentReferenceFactory;
import com.isik.testsuit.config.FetchAccessToken;
import com.isik.testsuit.entity.Doc;
import com.isik.testsuit.service.DocHandleService;
import com.isik.testsuit.service.DocService;
import com.isik.testsuit.DokumentReference.DocumentReferenceRecipient;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/docs")
public class DocController {

    private final DocService docService;

    private final DocHandleService docArchiveService;

    private final DocumentReferenceRecipient documentReferenceRecipient;

    /**
     * DocController
     * <p>
     * API mit lokalen Dokumenten in der ISiK-TestSuit DB
     * /api/docs/upload
     * /api/docs/{id}
     * /api/docs/encounter/{fallNummer}
     * /api/docs/upload/archive"
     * /api/docs/upload/archiveByDoc
     * /api/docs/getAllDoc
     * <p>
     * API Schnittstellen ohne ISiK-TestSuit DB (direktes ablegen am ISiK Server kein zwischenspeichern)
     * /api/docs/archive
     *
     * @param docService
     * @param docArchiveService
     * @param documentReferenceEmpfaenger
     */
    public DocController(DocService docService, DocHandleService docArchiveService, DocumentReferenceRecipient documentReferenceEmpfaenger) {
        this.docService = docService;
        this.docArchiveService = docArchiveService;
        this.documentReferenceRecipient = documentReferenceEmpfaenger;
    }


    /**
     * Schnittstelle speichert ein Dokument in die ISiK-TestSuit DB um es zu später abzulegen oder in einem TestCase zu nutzen.
     *
     * @param file
     * @param kdl
     * @param kdlName
     * @param documentReference
     * @param padId
     * @return
     */
    @PostMapping("/upload")
    public ResponseEntity<Doc> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kdl") String kdl,
            @RequestParam("kdlName") String kdlName,
            @RequestParam("documentReference") String documentReference,
            @RequestParam("padId") String padId,
            @RequestParam("fallnummer") String fallnummer,
            @RequestParam("dokumentBeschreibung") String dokumentBeschreibung) {
        try {
            Doc savedDoc = docService.saveDoc(file, kdl, padId, kdlName, documentReference, fallnummer, dokumentBeschreibung);
            return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
        } catch (IOException | NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/uploadSpecialized")
    public ResponseEntity<Doc> uploadSpecialized(
            @RequestParam("ID") String ID,
            @RequestParam("status") String status,
            @RequestParam("docStatus") String docStatus,
            @RequestParam("metaProfile") String metaProfile,
            @RequestParam("metaSystem") String metaSystem,
            @RequestParam("metaCode") String metaCode,
            @RequestParam("masterIdentifierSystem") String masterIdentifierSystem,
            @RequestParam("masterIdentifierValue") String masterIdentifierValue,
            @RequestParam("identifierSystem") String identifierSystem,
            @RequestParam("identifierSystemValue") String identifierSystemValue,
            @RequestParam("kdlSystem") String kdlSystem,
            @RequestParam("kdlCode") String kdlCode,
            @RequestParam("kdlDisplay") String kdlDisplay,
            @RequestParam("padId") String padId,
            @RequestParam("authorName") String authorName,
            @RequestParam("securityLabelSystem") String securityLabelSystem,
            @RequestParam("securityLabelCode") String securityLabelCode,
            @RequestParam("securityLabelDisplay") String securityLabelDisplay,
            @RequestParam("type") String type,
            @RequestParam("documentReference") String documentReference,
            @RequestParam("contentType") String contentType,
            @RequestParam("file") MultipartFile file,
            @RequestParam("language") String language,
            @RequestParam("creationElement") String creationElement,
            @RequestParam("formatSystem") String formatSystem,
            @RequestParam("formatCode") String formatCode,
            @RequestParam("formatDisplay") String formatDisplay,
            @RequestParam("facilityTypeSystem") String facilityTypeSystem,
            @RequestParam("facilityTypeCode") String facilityTypeCode,
            @RequestParam("facilityTypeDisplay") String facilityTypeDisplay,
            @RequestParam("practiceSettingSystem") String practiceSettingSystem,
            @RequestParam("practiceSettingCode") String practiceSettingCode,
            @RequestParam("practiceSettingDisplay") String practiceSettingDisplay,
            @RequestParam("fallnummer") String fallnummer,
            @RequestParam("dokumentBeschreibung") String dokumentBeschreibung,
            @RequestParam("destroyHash") Boolean destroyHash) {
        try {
            Doc savedDoc = docService.saveSpecializedDoc(ID, Enumerations.DocumentReferenceStatus.fromCode(status),
                    DocumentReference.ReferredDocumentStatus.fromCode(docStatus),
                    metaProfile, metaSystem, metaCode, masterIdentifierSystem, masterIdentifierValue,
                    identifierSystem, identifierSystemValue, kdlSystem, kdlCode, kdlDisplay, padId,
                    authorName, securityLabelSystem, securityLabelCode, securityLabelDisplay,
                    DocumentReference.DocumentRelationshipType.fromCode(type), documentReference,
                    contentType, file, language, creationElement, formatSystem, formatCode,
                    formatDisplay, facilityTypeSystem, facilityTypeCode, facilityTypeDisplay,
                    practiceSettingSystem, practiceSettingCode, practiceSettingDisplay, fallnummer, dokumentBeschreibung, destroyHash
            );
            return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
        } catch (IOException | NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gibt das Dokument mit der angegebenen ID zurück wenn vorhanden
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Doc> getDocById(@PathVariable Long id) {
        return docService.getDocById(id)
                .map(doc -> new ResponseEntity<>(doc, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Gibt ein Dokument anhand der Fallnummer zurück wenn vorhanden
     *
     * @param padId
     * @return
     */
    @GetMapping("/encounter/{fallNummer}")
    public ResponseEntity<Doc> getDocByFallNummer(@PathVariable String padId) {
        return docService.getDocByFallNummer(padId)
                .map(doc -> new ResponseEntity<>(doc, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Legt das Dokument mit der angegbenen ID über die ISiK Schnittstelle ab
     * Hierfür muss das Dokument zunächst mit /upload in die ISiK-TestSuit DB gespeichert worden sein
     *
     * @param id
     * @return
     */
    @GetMapping("/upload/archive")
    public ResponseEntity<String> triggerDocArchive(@RequestParam("id") Long id) {
        ResponseEntity<String> result = docArchiveService.ablegenById(id);
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    /**
     * Legt ein Dokument in die ISiK-TestSuit DB und legt es umgehend danach über die ISiK Schnittstelle ab
     *
     * @param file
     * @param kdl
     * @param kdlName
     * @param documentReference
     * @param padId
     * @return
     */
    @PostMapping("/upload/archiveByDoc")
    public ResponseEntity<Doc> uploadAndArchiveByFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kdl") String kdl,
            @RequestParam("kdlName") String kdlName,
            @RequestParam("documentReference") String documentReference,
            @RequestParam("padId") String padId,
            @RequestParam("fallnummer") String fallnummer,
            @RequestParam("dokumentBeschreibung") String dokumentBeschreibung) {
        try {
            Doc savedDoc = docService.saveDoc(file, kdl, padId, kdlName, documentReference, fallnummer, dokumentBeschreibung);
            ResponseEntity<String> result = docArchiveService.archiveByDoc(savedDoc);
            System.out.println(result);
            return new ResponseEntity<>(savedDoc, HttpStatus.CREATED);
        } catch (IOException | NoSuchAlgorithmException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Nur zum test
     *
     * @param id
     * @return
     */
    @GetMapping("/test")
    public ResponseEntity<String> test(@RequestParam("id") Long id, @RequestBody String body) throws NoSuchAlgorithmException {
        String requestBody = body;
        DocumentReferenceFactory ref = new DocumentReferenceFactory(body);
        documentReferenceRecipient.SaveDocFileSystem(ref.getDocRef());
        String test = documentReferenceRecipient.getHashfromDoc(ref.getDocRef());
        return new ResponseEntity<>("Received body: " + requestBody, HttpStatus.OK);
    }

    /**
     * Legt ein Dokument über die ISiK Schnittstelle ab
     * in der DocumentTransferHistory wird der Schatten gespeichert (URL zum abgelegten URL auf dem ISiK Server)
     * <p>
     * legt nichts in der ISiK-TestSuit DB ab
     *
     * @param file
     * @param kdl
     * @param kdlName
     * @param documentReference
     * @param padId
     * @param fallnummer
     * @return
     */
    @PostMapping("/archive")
    public ResponseEntity<String> ablegen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("kdl") String kdl,
            @RequestParam("kdlName") String kdlName,
            @RequestParam(value = "documentReference", required = false) String documentReference,
            @RequestParam("padId") String padId,
            @RequestParam("fallnummer") String fallnummer) {
        try {
            ResponseEntity<String> result = docArchiveService.archive(file, kdl, padId, kdlName, documentReference, fallnummer);
            return new ResponseEntity<>(result.getBody().toString(), HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gibt alle Docs zurück
     *
     * @return
     */
    @GetMapping("/getAllDoc")
    public ResponseEntity<List<Doc>> getAllTestCase() {
        List<Doc> doc = docService.getAll();
        if (doc.isEmpty()) {
            return ResponseEntity.noContent().build(); // Falls keine Testfälle existieren
        }
        return ResponseEntity.ok(doc);
    }


    /**
     * Holt anhand einer ID das Dokument vom ISiK Server ab
     *
     * @return
     */
    @GetMapping("/getDocumentAsBase64ById")
    public ResponseEntity<byte[]> getAllDocumentAsBase64ById(@RequestParam("id") Long id) {
        byte[] decodedBytes = docService.getDocAsBase64ById(id);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + FetchAccessToken.getInstance().getAccessToken());
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("document.pdf").build());

        return ResponseEntity.ok().headers(headers).body(decodedBytes);
    }

    /**
     * löscht das Dokument mit der id
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {
        boolean output = docService.deleteById(id);
        return ResponseEntity.ok(output);
    }

}
