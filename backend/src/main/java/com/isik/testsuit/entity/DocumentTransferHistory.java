package com.isik.testsuit.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_transfer_history")
public class DocumentTransferHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kdl", length = 255)
    private String kdl;

    @Column(name = "kdl_name", length = 255)
    private String kdlName;

    @Column(name = "pad_id", length = 255)
    private String padId;

    @Column(name = "fallnummer", length = 255)
    private String fallnummer;

    @Lob
    @Column(name = "docRef_json", columnDefinition = "NVARCHAR(MAX)")
    private String documentReference;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "url", updatable = false)
    private String url;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public DocumentTransferHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKdl() {
        return kdl;
    }

    public void setKdl(String kdl) {
        this.kdl = kdl;
    }

    public String getKdlName() {
        return kdlName;
    }

    public void setKdlName(String kdlName) {
        this.kdlName = kdlName;
    }

    public String getPadId() {
        return padId;
    }

    public void setPadId(String padId) {
        this.padId = padId;
    }

    public String getFallnummer() {
        return fallnummer;
    }

    public void setFallnummer(String fallnummer) {
        this.fallnummer = fallnummer;
    }

    public String getDocumentReference() {
        return documentReference;
    }

    public void setDocumentReference(String documentReference) {
        this.documentReference = documentReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
