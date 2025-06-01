package com.isik.testsuit.DokumentReference;

import ca.uhn.fhir.context.FhirContext;
import com.isik.testsuit.fhir.FireContext;
import org.hl7.fhir.r4.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentReferenceFactory {

    private DocumentReference docRef;
    private FhirContext ctx = FhirContext.forR4();

    /**
     * Constructor für alle Variablen der DocumentReference
     * Hiermit kann die DocumentReference in allen Variablen erstellt werden
     *
     * @param ID
     * @param status
     * @param docStatus
     * @param metaProfile
     * @param metaSystem
     * @param metaCode
     * @param masterIdentifierSystem
     * @param masterIdentifierValue
     * @param identifierSystem
     * @param identifierSystemValue
     * @param kdlSystem
     * @param kdlCode
     * @param kdlDisplay
     * @param padId
     * @param authorName
     * @param securityLabelSystem
     * @param securityLabelCode
     * @param securityLabelDisplay
     * @param type
     * @param documentReference
     * @param contentType
     * @param file
     * @param language
     * @param creationElement
     * @param formatSystem
     * @param formatCode
     * @param formatDisplay
     * @param facilityTypeSystem
     * @param facilityTypeCode
     * @param facilityTypeDisplay
     * @param practiceSettingSystem
     * @param practiceSettingCode
     * @param practiceSettingDisplay
     * @throws IOException
     */
    public DocumentReferenceFactory(String ID,
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
                                    String fallnummer) throws IOException {
        this.docRef = new DocumentReference();

        // DocumentReference erstellen
        docRef.setId(ID);
        docRef.setStatus(status);
        docRef.setDocStatus(docStatus);

        // Meta-Daten setzen
        Meta meta = new Meta();
        meta.setProfile(Collections.singletonList(new CanonicalType(metaProfile)));
        meta.addSecurity().setSystem(metaSystem).setCode(metaCode);
        docRef.setMeta(meta);

        // Master Identifier setzen
        Identifier masterIdentifier = new Identifier();
        masterIdentifier.setSystem(masterIdentifierSystem);
        masterIdentifier.setValue(masterIdentifierValue);
        docRef.setMasterIdentifier(masterIdentifier);

        // Identifier setzen
        Identifier identifier = new Identifier();
        identifier.setSystem(identifierSystem);
        identifier.setValue(identifierSystemValue);
        docRef.setIdentifier(Collections.singletonList(identifier));

        // Typ setzen
        CodeableConcept ty = new CodeableConcept();
        ty.addCoding(new Coding(kdlSystem, kdlCode, kdlDisplay));
        docRef.setType(ty);

        // Subject (Patient) setzen
        docRef.setSubject(new Reference("Patient/" + padId));

        // Context erstellen
        DocumentReference.DocumentReferenceContextComponent context = new DocumentReference.DocumentReferenceContextComponent();

        // Encounter setzen
        List<Reference> encounters = new ArrayList<>();
        encounters.add(new Reference("Encounter/" + fallnummer));
        context.setEncounter(encounters);

        // Facility Type setzen
        CodeableConcept facilityType = new CodeableConcept();
        facilityType.addCoding(new Coding(facilityTypeSystem, facilityTypeCode, facilityTypeDisplay));
        context.setFacilityType(facilityType);

        // Practice Setting setzen
        CodeableConcept practiceSetting = new CodeableConcept();
        practiceSetting.addCoding(new Coding(practiceSettingSystem, practiceSettingCode, practiceSettingDisplay));
        context.setPracticeSetting(practiceSetting);

        // Context zur DocumentReference hinzufügen
        docRef.setContext(context);

        // Autor setzen
        Practitioner author = new Practitioner();
        author.setId(authorName);
        docRef.addAuthor(new Reference().setDisplay(authorName));

        // Security Label setzen
        CodeableConcept securityLabel = new CodeableConcept();
        securityLabel.addCoding(new Coding(securityLabelSystem, securityLabelCode, securityLabelDisplay));
        docRef.setSecurityLabel(Collections.singletonList(securityLabel));

        // RelatesTo setzen
        DocumentReference.DocumentReferenceRelatesToComponent relatesTo = new DocumentReference.DocumentReferenceRelatesToComponent();
        relatesTo.setCode(DocumentReference.DocumentRelationshipType.REPLACES);
        relatesTo.setTarget(new Reference("DocumentReference/" + documentReference));
        docRef.setRelatesTo(Collections.singletonList(relatesTo));

        // Beschreibung setzen
        docRef.setDescription("Molekularpathologiebefund vom 31.12.21");

        // Content setzen
        Attachment attachment = new Attachment();
        attachment.setContentType(contentType);
        attachment.setData(file.getBytes());
        attachment.setLanguage(language);
        attachment.setCreationElement(new DateTimeType(creationElement));

        DocumentReference.DocumentReferenceContentComponent content = new DocumentReference.DocumentReferenceContentComponent();
        content.setAttachment(attachment);
        content.setFormat(new Coding(formatSystem, formatCode, formatDisplay));

        docRef.setContent(Collections.singletonList(content));

    }


    /**
     * Erstellung einer DocumentReference mit den nur notwendigen Varablen
     * Die restlichen Variablen werden immer gleich gesetzt (Test für CLA)
     *
     * @param file
     * @param kdlSystem
     * @param kdlCode
     * @param kdlDisplay
     * @param padId
     * @param contentType
     * @param documentReference
     * @param fallnummer
     * @throws IOException
     */
    public DocumentReferenceFactory(MultipartFile file,
                                    String kdlSystem,
                                    String kdlCode,
                                    String kdlDisplay,
                                    String padId,
                                    String contentType,
                                    String documentReference,
                                    String fallnummer) throws IOException {
        this.docRef = new DocumentReference();

        // DocumentReference erstellen
        docRef.setId("dok-beispiel-client-with-binary-jpeg-example-short");
        docRef.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
        docRef.setDocStatus(DocumentReference.ReferredDocumentStatus.FINAL);

        // Meta-Daten setzen
        Meta meta = new Meta();
        meta.setProfile(Collections.singletonList(new CanonicalType("https://gematik.de/fhir/isik/v3/Dokumentenaustausch/StructureDefinition/ISiKDokumentenMetadaten")));
        meta.addSecurity().setSystem("http://terminology.hl7.org/CodeSystem/v3-ActReason").setCode("HTEST");
        docRef.setMeta(meta);

        // Master Identifier setzen
        Identifier masterIdentifier = new Identifier();
        masterIdentifier.setSystem("urn:ietf:rfc:3986");
        masterIdentifier.setValue("urn:oid:1.2.840.113556.1.8000.2554.58783.21864.3474.19410.44358.58254.41281.46340");
        docRef.setMasterIdentifier(masterIdentifier);

        // Identifier setzen
        Identifier identifier = new Identifier();
        identifier.setSystem("urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
        identifier.setValue("129.6.58.42.33726");
        docRef.setIdentifier(Collections.singletonList(identifier));

        // Typ setzen
        CodeableConcept type = new CodeableConcept();
        type.addCoding(new Coding(kdlSystem, kdlCode, kdlDisplay));
        docRef.setType(type);

        // Subject (Patient) setzen
        docRef.setSubject(new Reference("Patient/" + padId));

        // Context erstellen
        DocumentReference.DocumentReferenceContextComponent context = new DocumentReference.DocumentReferenceContextComponent();

        // Encounter setzen
        List<Reference> encounters = new ArrayList<>();
        encounters.add(new Reference("Encounter/" + fallnummer));
        context.setEncounter(encounters);

        // Facility Type setzen
        CodeableConcept facilityType = new CodeableConcept();
        facilityType.addCoding(new Coding("http://ihe-d.de/CodeSystems/PatientBezogenenGesundheitsversorgung", "KHS", "Krankenhaus"));
        context.setFacilityType(facilityType);

        // Practice Setting setzen
        CodeableConcept practiceSetting = new CodeableConcept();
        practiceSetting.addCoding(new Coding("http://ihe-d.de/CodeSystems/AerztlicheFachrichtungen", "ALLG", null));
        context.setPracticeSetting(practiceSetting);

        // Context zur DocumentReference hinzufügen
        docRef.setContext(context);

        // Autor setzen
        Practitioner author = new Practitioner();
        author.setId("Maxine Mustermann");
        docRef.addAuthor(new Reference().setDisplay("Maxine Mustermann"));

        // Security Label setzen
        CodeableConcept securityLabel = new CodeableConcept();
        securityLabel.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "N", "normal"));
        docRef.setSecurityLabel(Collections.singletonList(securityLabel));

        // RelatesTo setzen
        DocumentReference.DocumentReferenceRelatesToComponent relatesTo = new DocumentReference.DocumentReferenceRelatesToComponent();
        relatesTo.setCode(DocumentReference.DocumentRelationshipType.REPLACES);
        relatesTo.setTarget(new Reference("DocumentReference/" + documentReference));
        docRef.setRelatesTo(Collections.singletonList(relatesTo));

        // Beschreibung setzen
        docRef.setDescription("Molekularpathologiebefund vom 31.12.21");

        // Content setzen
        Attachment attachment = new Attachment();
        attachment.setContentType(contentType);
        attachment.setData(file.getBytes());
        attachment.setLanguage("de");
        attachment.setCreationElement(new DateTimeType("2020-12-31T23:50:50+01:00"));

        DocumentReference.DocumentReferenceContentComponent content = new DocumentReference.DocumentReferenceContentComponent();
        content.setAttachment(attachment);
        content.setFormat(new Coding("http://ihe.net/fhir/ihe.formatcode.fhir/CodeSystem/formatcode", "urn:ihe:iti:xds:2017:mimeTypeSufficient", "mimeType Sufficient"));

        docRef.setContent(Collections.singletonList(content));
    }

    /**
     * Baut aus dem Json string eine DocumentReference
     *
     * @param json
     */
    public DocumentReferenceFactory(String json) {
        this.docRef = new DocumentReference();
        FhirContext ctx = FireContext.getInstance().getCtx();
        this.docRef = ctx.newJsonParser().parseResource(DocumentReference.class, json);
    }


    /**
     * Exportiert das DocRef als json
     *
     * @return
     */
    public String jsonExport() {
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(this.docRef);
    }


    /**
     * gibt das docRef zurück das erstellt wurde
     *
     * @return
     */
    public DocumentReference getDocRef() {
        return docRef;
    }


}
