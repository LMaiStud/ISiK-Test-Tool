package com.isik.testsuit.service;

import com.isik.testsuit.DokumentReference.DocumentReferenceFactory;
import com.isik.testsuit.config.ConfigLoader;
import com.isik.testsuit.entity.Doc;
import com.isik.testsuit.repository.DocRepository;
import com.isik.testsuit.repository.TestCaseRepository;
import com.isik.testsuit.util.Util;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class DocService {

    private final DocRepository docRepository;
    private final TestCaseRepository testCaseRepository;

    private final String kdlUrl = "http://dvmd.de/fhir/CodeSystem/kdl";

    public DocService(DocRepository docRepository, TestCaseRepository testCaseRepository) {
        this.docRepository = docRepository;
        this.testCaseRepository = testCaseRepository;
    }

    /**
     * Speichert ein File in die DOC DB
     * <p>
     * Für die späteren Test Fälle
     *
     * @param file
     * @param kdl
     * @param padId
     * @param kdlName
     * @param documentReference
     * @param fallnummer
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public Doc saveDoc(MultipartFile file, String kdl, String padId, String kdlName, String documentReference, String fallnummer, String dokumentBeschreibung) throws IOException, NoSuchAlgorithmException {

        Doc doc = new Doc();
        doc.setMimeType(file.getContentType());
        doc.setKdl(kdl);
        doc.setFallNummer(fallnummer);
        doc.setKdlName(kdlName);
        doc.setDocumentReferenceCode(documentReference);
        doc.setPadId(padId);
        doc.setDocumentBeschreibung(dokumentBeschreibung);

        byte[] content = file.getBytes();
        doc.setContent(content);

        String hash = Util.calculateHash(content, ConfigLoader.getInstance().getHash());
        doc.setHash(hash);

        DocumentReferenceFactory docRef = new DocumentReferenceFactory(file, kdlUrl, kdl, kdlName, padId, file.getContentType(), documentReference, fallnummer);
        doc.setDocRef_json(docRef.jsonExport());

        return docRepository.save(doc);
    }

    public Doc saveSpecializedDoc(
            String ID,
            Enumerations.DocumentReferenceStatus status,
            DocumentReference.ReferredDocumentStatus docStatus,
            String metaProfile,
            String metaSystem,
            String metaCode,
            String masterIdentifierSystem,
            String masterIdentifierValue,
            String identifierSystem,
            String identifierSystemValue,
            String kdlSystem,
            String kdlCode,
            String kdlDisplay,
            String padId,
            String authorName,
            String securityLabelSystem,
            String securityLabelCode,
            String securityLabelDisplay,
            DocumentReference.DocumentRelationshipType type,
            String documentReference,
            String contentType,
            MultipartFile file,
            String language,
            String creationElement,
            String formatSystem,
            String formatCode,
            String formatDisplay,
            String facilityTypeSystem,
            String facilityTypeCode,
            String facilityTypeDisplay,
            String practiceSettingSystem,
            String practiceSettingCode,
            String practiceSettingDisplay,
            String fallnummer,
            String dokumentBeschreibung,
            Boolean destroyHash) throws IOException, NoSuchAlgorithmException {
        Doc doc = new Doc();
        doc.setMimeType(file.getContentType());
        doc.setKdl(kdlCode);
        doc.setFallNummer(fallnummer);
        doc.setKdlName(kdlDisplay);
        doc.setDocumentReferenceCode(documentReference);
        doc.setPadId(padId);
        doc.setDocumentBeschreibung(dokumentBeschreibung);

        byte[] content = file.getBytes();
        doc.setContent(content);

        if (destroyHash) {
            doc.setHash(Util.generateRandomString());
        } else {
            String hash = Util.calculateHash(content, ConfigLoader.getInstance().getHash());
            doc.setHash(hash);
        }


        DocumentReferenceFactory docRefFactory = new DocumentReferenceFactory(
                ID, status, docStatus, metaProfile, metaSystem, metaCode,
                masterIdentifierSystem, masterIdentifierValue, identifierSystem, identifierSystemValue,
                kdlSystem, kdlCode, kdlDisplay, padId, authorName, securityLabelSystem,
                securityLabelCode, securityLabelDisplay, type, documentReference, contentType,
                file, language, creationElement, formatSystem, formatCode, formatDisplay,
                facilityTypeSystem, facilityTypeCode, facilityTypeDisplay, practiceSettingSystem,
                practiceSettingCode, practiceSettingDisplay, fallnummer
        );

        doc.setDocRef_json(docRefFactory.jsonExport());
        return docRepository.save(doc);
    }

    public Optional<Doc> getDocById(Long id) {
        return docRepository.findById(id);
    }

    public Optional<Doc> getDocByFallNummer(String padId) {
        return docRepository.findBypadId(padId);
    }

    public List<Doc> getAll() {
        return docRepository.findAll();
    }

    public byte[] getDocAsBase64ById(Long id) {
        return docRepository.findById(id).get().getContent();
    }

    @Transactional
    public boolean deleteById(Long id) {
        docRepository.deleteById(id);
        testCaseRepository.deleteDocFromTestCaseJson(Math.toIntExact(id));
        return true;
    }
}

