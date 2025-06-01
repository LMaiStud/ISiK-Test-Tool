package com.isik.testsuit.service;

import com.isik.testsuit.DokumentReference.DocumentReferenceSender;
import com.isik.testsuit.DokumentReference.DocumentReferenceFactory;
import com.isik.testsuit.config.AppConfig;
import com.isik.testsuit.entity.Doc;
import com.isik.testsuit.entity.DocumentTransferHistory;
import com.isik.testsuit.repository.DocRepository;
import com.isik.testsuit.repository.DocumentTransferHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class DocHandleService {

    private final String kdlUrl = "http://dvmd.de/fhir/CodeSystem/kdl";
    private final DocRepository docRepository;

    private final DocumentTransferHistoryRepository documentTransferHistoryRepository;

    private final DocumentTransferHistoryService documentTransferHistoryService;


    public DocHandleService(DocRepository docRepository, DocumentTransferHistoryRepository documentTransferHistoryRepository, DocumentTransferHistoryService documentTransferHistoryService) {
        this.docRepository = docRepository;
        this.documentTransferHistoryRepository = documentTransferHistoryRepository;
        this.documentTransferHistoryService = documentTransferHistoryService;
    }

    /**
     * legt ein Doc ab anhand der ID aus der DOC Tabelle
     * ablegen = an ISiK Server senden
     *
     * @param id
     * @return
     */
    public ResponseEntity<String> ablegenById(Long id) {
        Optional<Doc> doc = docRepository.findById(id);
        AppConfig appConfig = new AppConfig();
        DocumentReferenceSender documentReferenceSender = new DocumentReferenceSender(appConfig.restTemplate());
        ResponseEntity<String> res = documentReferenceSender.send(doc.get().getDocRef_json());
        return res;
    }

    /**
     * legt ein Doc ab das neu erstellt wurde und sofort abgelegt wird
     * ablegen = an ISiK Server senden
     *
     * @param doc
     * @return
     */
    public ResponseEntity<String> archiveByDoc(Doc doc) {
        AppConfig appConfig = new AppConfig();
        DocumentReferenceSender documentReferenceSender = new DocumentReferenceSender(appConfig.restTemplate());
        ResponseEntity<String> res = documentReferenceSender.send(doc.getDocRef_json());
        return res;
    }

    /**
     * legt Doc´s ab anhand der im Array liegenden ID´s aus der DOC Tabelle
     * ablegen = an ISiK Server senden
     *
     * @param ids
     * @return
     */
    public ResponseEntity<String> archiveById(Long[] ids) {
        ResponseEntity<String> res = null;
        for (Long id : ids) {
            Optional<Doc> doc = docRepository.findById(id);
            AppConfig appConfig = new AppConfig();
            DocumentReferenceSender documentReferenceSender = new DocumentReferenceSender(appConfig.restTemplate());
            res = documentReferenceSender.send(doc.get().getDocRef_json());
        }
        return res;
    }

    /**
     * legt ein Doc ab ohne es in der DOC Tabelle anzuspeichern (FÜr späteren UseCase Dokument versenden)
     * ablegen = an ISiK Server senden
     *
     * @param file
     * @param kdl
     * @param padId
     * @param kdlName
     * @param documentReference
     * @param fallnummer
     * @return
     */
    public ResponseEntity<String> archive(MultipartFile file, String kdl, String padId, String kdlName, String documentReference, String fallnummer) throws IOException, NoSuchAlgorithmException {
        DocumentReferenceFactory docRef = new DocumentReferenceFactory(file, kdlUrl, kdl, kdlName, padId, file.getContentType(), documentReference, fallnummer);
        AppConfig appConfig = new AppConfig();
        DocumentReferenceSender documentReferenceSender = new DocumentReferenceSender(appConfig.restTemplate());
        ResponseEntity<String> res = documentReferenceSender.send(docRef.getDocRef());
        DocumentTransferHistory savedDoc = documentTransferHistoryService.save(kdl, padId, kdlName, res.getBody(), fallnummer);
        return res;
    }

}
