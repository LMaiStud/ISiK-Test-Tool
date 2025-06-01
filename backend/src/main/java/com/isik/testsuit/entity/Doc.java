package com.isik.testsuit.entity;

import jakarta.persistence.*;

@Entity
public class Doc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mimeType;
    private String kdl;

    private String padId;

    @Column(name = "fall_nummer")
    private String fallNummer;

    @Lob
    @Column(name = "docRef_json", columnDefinition = "NVARCHAR(MAX)")
    private String docRef_json;

    @Column(name = "kdlName")
    private String kdlName;

    @Lob
    @Column(name = "documentReferenceCode")
    private String documentReferenceCode;

    @Lob
    private byte[] content;

    private String hash;

    private String documentBeschreibung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getKdl() {
        return kdl;
    }

    public void setKdl(String kdl) {
        this.kdl = kdl;
    }

    public String getFallNummer() {
        return fallNummer;
    }

    public void setFallNummer(String fallNummer) {
        this.fallNummer = fallNummer;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPadId() {
        return padId;
    }

    public String getKdlName() {
        return kdlName;
    }

    public String getDocRef_json() {
        return docRef_json;
    }

    public String getDocumentReferenceCode() {
        return documentReferenceCode;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public void setDocRef_json(String docRef_json) {
        this.docRef_json = docRef_json;
    }

    public void setKdlName(String kdlName) {
        this.kdlName = kdlName;
    }

    public String getDocumentBeschreibung() {
        return documentBeschreibung;
    }

    public void setDocumentBeschreibung(String documentBeschreibung) {
        this.documentBeschreibung = documentBeschreibung;
    }

    public void setDocumentReferenceCode(String documentReferenceCode) {
        this.documentReferenceCode = documentReferenceCode;
    }
}
