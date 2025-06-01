package com.isik.testsuit.service;

import com.isik.testsuit.DokumentReference.DocumentReferenceFactory;
import com.isik.testsuit.DokumentReference.DocumentReferenceRecipient;
import com.isik.testsuit.entity.DocumentTransferHistory;
import com.isik.testsuit.repository.DocumentTransferHistoryRepository;
import org.hl7.fhir.r4.model.Attachment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class DocumentTransferHistoryService {

    private final DocumentTransferHistoryRepository documentTransferHistoryRepository;
    private final DocumentReferenceRecipient documentReferenceRecipient;

    private final String kdlUrl = "http://dvmd.de/fhir/CodeSystem/kdl";

    public DocumentTransferHistoryService(DocumentTransferHistoryRepository documentTransferHistoryRepository, DocumentReferenceRecipient documentReferenceRecipient) {
        this.documentTransferHistoryRepository = documentTransferHistoryRepository;
        this.documentReferenceRecipient = documentReferenceRecipient;
    }

    /**
     * speichert documentReference mit der URL in der DocumentTransferHistory DB ab
     *
     * @param kdl
     * @param padId
     * @param kdlName
     * @param documentReference
     * @param fallnummer
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public DocumentTransferHistory save(String kdl, String padId, String kdlName, String documentReference, String fallnummer) throws IOException, NoSuchAlgorithmException {
        DocumentReferenceFactory docRef = new DocumentReferenceFactory(documentReference);

        DocumentTransferHistory documentTransferHistory = new DocumentTransferHistory();
        documentTransferHistory.setKdl(kdl);
        documentTransferHistory.setPadId(padId);
        documentTransferHistory.setKdlName(kdlName);
        documentTransferHistory.setDocumentReference(documentReference);
        documentTransferHistory.setFallnummer(fallnummer);

        String url = null;
        Attachment attachment = docRef.getDocRef().getContent().get(0).getAttachment();
        if (attachment.hasUrl()) {
            url = attachment.getUrl();
        }
        documentTransferHistory.setUrl(url);

        return documentTransferHistoryRepository.save(documentTransferHistory);
    }

    public List<DocumentTransferHistory> getAll() {
        return documentTransferHistoryRepository.findAll();
    }

    public DocumentTransferHistory getById(Long id) {
        return documentTransferHistoryRepository.findById(id).get();
    }

    public String getDocAsBase64ById(Long id) {
        return documentReferenceRecipient.getDocAsBase64ByUrl(documentTransferHistoryRepository.findById(id).get().getUrl());
    }

    public void deleteById(Long id) {
        documentTransferHistoryRepository.deleteById(id);
    }
}
