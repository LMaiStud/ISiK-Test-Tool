package com.isik.testsuit.controller;

import com.isik.testsuit.config.FetchAccessToken;
import com.isik.testsuit.entity.DocumentTransferHistory;
import com.isik.testsuit.service.DocumentTransferHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/documentTransferHistory")
@RestController
public class DocumentTransferHistoryController {

    private final DocumentTransferHistoryService documentTransferHistoryService;

    @Autowired
    public DocumentTransferHistoryController(DocumentTransferHistoryService documentTransferHistoryService) {
        this.documentTransferHistoryService = documentTransferHistoryService;
    }

    /**
     * Gibt alle DocumentTransferHistories zurück
     *
     * @return
     */
    @GetMapping("/getAllDocumentTransferHistory")
    public ResponseEntity<List<DocumentTransferHistory>> getAllTestCase() {
        List<DocumentTransferHistory> documentTransferHistories = documentTransferHistoryService.getAll();
        if (documentTransferHistories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(documentTransferHistories);
    }


    /**
     * Gibt den Token zurück
     *
     * @return
     */
    @GetMapping("/getToken")
    public String getToken() {
        return FetchAccessToken.getInstance().getAccessToken();
    }


    /**
     * löscht das DocumentTransferHistorie mit der id
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {
        documentTransferHistoryService.deleteById(id);
        boolean output = true;
        return ResponseEntity.ok(output);
    }
}
