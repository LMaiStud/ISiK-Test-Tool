package com.isik.testsuit.entitiy;

import com.isik.testsuit.entity.Doc;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

class DocTest {

    @Test
    void testDocSettersAndGetters() {
        Doc doc = new Doc();

        doc.setId(1L);
        doc.setMimeType("application/pdf");
        doc.setKdl("KDL-123");
        doc.setPadId("PAD-456");
        doc.setFallNummer("FALL-789");
        doc.setDocRef_json("{\"resourceType\": \"DocumentReference\"}");
        doc.setKdlName("Radiology Report");
        doc.setDocumentReferenceCode("DOC-REF-001");
        doc.setContent(new byte[]{1, 2, 3, 4});
        doc.setHash("abc123");

        assertThat(doc.getId()).isEqualTo(1L);
        assertThat(doc.getMimeType()).isEqualTo("application/pdf");
        assertThat(doc.getKdl()).isEqualTo("KDL-123");
        assertThat(doc.getPadId()).isEqualTo("PAD-456");
        assertThat(doc.getFallNummer()).isEqualTo("FALL-789");
        assertThat(doc.getDocRef_json()).isEqualTo("{\"resourceType\": \"DocumentReference\"}");
        assertThat(doc.getKdlName()).isEqualTo("Radiology Report");
        assertThat(doc.getDocumentReferenceCode()).isEqualTo("DOC-REF-001");
        assertThat(doc.getHash()).isEqualTo("abc123");
        assertThat(doc.getContent()).isNotNull().hasSize(4).containsExactly(1, 2, 3, 4);
    }

    @Test
    void testEmptyDoc() {
        Doc doc = new Doc();
        assertThat(doc.getId()).isNull();
        assertThat(doc.getMimeType()).isNull();
        assertThat(doc.getKdl()).isNull();
        assertThat(doc.getPadId()).isNull();
        assertThat(doc.getFallNummer()).isNull();
        assertThat(doc.getDocRef_json()).isNull();
        assertThat(doc.getKdlName()).isNull();
        assertThat(doc.getDocumentReferenceCode()).isNull();
        assertThat(doc.getHash()).isNull();
        assertThat(doc.getContent()).isNull();
    }

    @Test
    void testContentSetterGetter() {
        Doc doc = new Doc();
        byte[] data = {10, 20, 30};

        doc.setContent(data);

        assertThat(doc.getContent()).isNotNull().containsExactly(10, 20, 30);
    }
}
