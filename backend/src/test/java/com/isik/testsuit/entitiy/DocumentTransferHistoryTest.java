package com.isik.testsuit.entitiy;

import com.isik.testsuit.entity.DocumentTransferHistory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

class DocumentTransferHistoryTest {

    @Test
    void testSettersAndGetters() {
        DocumentTransferHistory history = new DocumentTransferHistory();

        history.setId(1L);
        history.setKdl("KDL-123");
        history.setKdlName("Radiology Report");
        history.setPadId("PAD-456");
        history.setFallnummer("FALL-789");
        history.setDocumentReference("{\"resourceType\": \"DocumentReference\"}");
        history.setUrl("http://example.com/document");
        LocalDateTime now = LocalDateTime.now();
        history.setCreatedAt(now);

        assertThat(history.getId()).isEqualTo(1L);
        assertThat(history.getKdl()).isEqualTo("KDL-123");
        assertThat(history.getKdlName()).isEqualTo("Radiology Report");
        assertThat(history.getPadId()).isEqualTo("PAD-456");
        assertThat(history.getFallnummer()).isEqualTo("FALL-789");
        assertThat(history.getDocumentReference()).isEqualTo("{\"resourceType\": \"DocumentReference\"}");
        assertThat(history.getUrl()).isEqualTo("http://example.com/document");
        assertThat(history.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void testPrePersistSetsCreatedAt() {
        DocumentTransferHistory history = new DocumentTransferHistory();

        history.onCreate(); // Simuliert das PrePersist-Event

        assertThat(history.getCreatedAt()).isNotNull();
        assertThat(history.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void testEmptyDocumentTransferHistory() {
        DocumentTransferHistory history = new DocumentTransferHistory();

        assertThat(history.getId()).isNull();
        assertThat(history.getKdl()).isNull();
        assertThat(history.getKdlName()).isNull();
        assertThat(history.getPadId()).isNull();
        assertThat(history.getFallnummer()).isNull();
        assertThat(history.getDocumentReference()).isNull();
        assertThat(history.getCreatedAt()).isNull();
        assertThat(history.getUrl()).isNull();
    }
}
