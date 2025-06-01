package com.isik.testsuit.service;

import com.isik.testsuit.DokumentReference.DocumentReferenceCompare;
import com.isik.testsuit.DokumentReference.DocumentReferenceFactory;
import com.isik.testsuit.DokumentReference.DocumentReferenceRecipient;
import com.isik.testsuit.entity.Doc;
import com.isik.testsuit.entity.TestCase;
import com.isik.testsuit.entity.TestCaseDoc;
import com.isik.testsuit.exception.DocHashException;
import com.isik.testsuit.exception.MetaDataException;
import com.isik.testsuit.exception.UnexpectedSuccessException;
import com.isik.testsuit.repository.DocRepository;
import com.isik.testsuit.repository.TestCaseRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r4.model.DocumentReference;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class TestExecuteService {

    private final TestCaseRepository testCaseRepository;

    private final DocRepository docRepository;

    private final DocHandleService docHandleService;

    private final DocumentReferenceRecipient documentReferenceRecipient;

    private static final Logger logger = LogManager.getLogger(TestExecuteService.class);

    public TestExecuteService(TestCaseRepository testCaseRepository, DocRepository docRepository, DocHandleService docHandleService, DocumentReferenceRecipient documentReferenceRecipient) {
        this.testCaseRepository = testCaseRepository;
        this.docRepository = docRepository;
        this.docHandleService = docHandleService;
        this.documentReferenceRecipient = documentReferenceRecipient;
    }

    /**
     * Async execute
     *
     * @param id
     */
    @Async
    public void executeAsync(Long id) {
        try {
            execute(id);
        } catch (Exception e) {
            logger.error("Fehler beim asynchronen Ausführen von execute({}): {}", id, e.getMessage(), e);
            TestCase testCase = testCaseRepository.findById(id).get();
            testCase.setStatus(3);
            testCase.setException("Interner Fehler: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            testCaseRepository.save(testCase);
        }
    }

    /**
     * Führt den Test mit der ID durch
     * <p>
     * Wirft bei fehlern Exceptions wenn ein Dokument scheitert wird abgebrochen (Exception wird auf der DB gespeichert)
     * <p>
     * //bei true Test soll erfolgreich sein
     * //bei false Test soll fehlschlagen
     *
     * @param id
     * @return
     */
    public String execute(Long id) {

        TestCase testCase = testCaseRepository.findById(id).get();
        testCase.setStatus(2);
        testCaseRepository.save(testCase);

        List<TestCaseDoc> list = testCase.getTestCaseDocs();

        for (TestCaseDoc doc : list) {
            Doc sendDoc = docRepository.findById((long) doc.getDocID()).get();

            //bei true Test soll erfolgreich sein
            //bei false Test soll fehlschlagen
            boolean testType = doc.isTestType();

            DocumentReference sendDocumentReference = new DocumentReferenceFactory(sendDoc.getDocRef_json()).getDocRef();


            try {
                if (testType) {
                    positivTest(sendDocumentReference, sendDoc);
                } else {
                    negativTest(sendDocumentReference, sendDoc);
                }
            } catch (Exception e) {
                if (testType) {
                    logger.error("Fehler bei TestCase ID {}: {}", testCase.getId(), e.getMessage(), e);
                    testCase.setStatus(3);
                    testCase.setException(e.getClass().getSimpleName() + ": " + e.getMessage());
                    testCaseRepository.save(testCase);
                    throw new RuntimeException(e);
                } else {
                    break;
                }

            }
            logger.info("TestCaseID: " + testCase.getId() + " TestCaseName: " + testCase.getTestName() + " | Doc ID: " + sendDoc.getId() + " tested successfully");

        }

        logger.info("TestCaseID: " + testCase.getId() + " TestCaseName: " + testCase.getTestName() + " | was completed successfully");

        testCase.setException("");
        testCase.setStatus(1);
        testCaseRepository.save(testCase);

        return "TestCaseID: " + testCase.getId() + " TestCaseName: " + testCase.getTestName() + " | was completed successfully";
    }

    /**
     * Positiver Test (alle Dokumente müssen richtig mit metadaten übertragen werden und der Hashwert muss stimmen)
     *
     * @param sendDocumentReference
     * @param sendDoc
     * @return
     * @throws MetaDataException
     * @throws NoSuchAlgorithmException
     * @throws DocHashException
     */
    private boolean positivTest(DocumentReference sendDocumentReference, Doc sendDoc) throws MetaDataException, NoSuchAlgorithmException, DocHashException {
        boolean output;
        ResponseEntity<String> result = docHandleService.archiveByDoc(sendDoc);
        DocumentReference recipientDocumentReference = new DocumentReferenceFactory(result.getBody()).getDocRef();

        DocumentReferenceCompare documentReferenceCompare = new DocumentReferenceCompare(sendDocumentReference, recipientDocumentReference);

        // test Metadata
        output = documentReferenceCompare.isEqual();

        if (!output) {
            throw new MetaDataException(documentReferenceCompare.compareDeep());
        }

        // test Document Hash
        String hashRecipient = documentReferenceRecipient.getHashfromDoc(recipientDocumentReference);
        String hashSend = sendDoc.getHash();

        if (!hashRecipient.equals(hashSend)) {
            throw new DocHashException("Hash is not eqal" + " Doc ID " + sendDoc.getId());
        }

        return output;
    }

    /**
     * Negativ Test (einer der Schritte MUSS scheitern es wird nicht genau bestimmt welcher)
     *
     * @param sendDocumentReference
     * @param sendDoc
     * @return
     * @throws MetaDataException
     * @throws NoSuchAlgorithmException
     * @throws DocHashException
     * @throws UnexpectedSuccessException
     */
    private boolean negativTest(DocumentReference sendDocumentReference, Doc sendDoc) throws MetaDataException, NoSuchAlgorithmException, DocHashException, UnexpectedSuccessException {
        boolean output = false;

        ResponseEntity<String> result = docHandleService.archiveByDoc(sendDoc);

        if (result.getBody().contains("error")) {
            return true;
        }

        DocumentReference recipientDocumentReference = new DocumentReferenceFactory(result.getBody()).getDocRef();

        DocumentReferenceCompare documentReferenceCompare = new DocumentReferenceCompare(sendDocumentReference, recipientDocumentReference);

        // test Metadata
        output = documentReferenceCompare.isEqual();

        if (!output) {
            return true;
        }

        // test Document Hash
        String hashRecipient = documentReferenceRecipient.getHashfromDoc(recipientDocumentReference);
        String hashSend = sendDoc.getHash();

        if (!hashRecipient.equals(hashSend)) {
            return true;
        }

        throw new UnexpectedSuccessException("The test for ID " + sendDoc.getId() + " should have failed");
    }

}
