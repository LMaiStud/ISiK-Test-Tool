package com.isik.testsuit.DokumentReference;

import ca.uhn.fhir.context.FhirContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonDiff;
import com.isik.testsuit.fhir.FireContext;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DocumentReference;

public class DocumentReferenceCompare {

    private FhirContext ctx = null;
    private DocumentReference documentReference1;
    private DocumentReference documentReference2;


    /**
     * documentReference1 ist immer das gesendete Objekt (mit data)
     * documentReference2 ist immer das empfangene Objekt (mit URL)
     *
     * @param documentReference1
     * @param documentReference2
     */
    public DocumentReferenceCompare(DocumentReference documentReference1, DocumentReference documentReference2) {
        this.documentReference1 = documentReference1;
        this.documentReference2 = documentReference2;
        this.ctx = FireContext.getInstance().getCtx();

        //anpassen der documentReference1
        this.documentReference1.setCategory(this.documentReference2.getCategory());
        this.documentReference1.setContent(this.documentReference2.getContent());
        this.documentReference1.setId(this.documentReference2.getId());

        CodeableConcept type;
        type = this.documentReference1.getType();
        type.addCoding(this.documentReference2.getType().getCoding().get(1));

        this.documentReference1.setType(type);
    }

    /**
     * Vergleich documentReference1 und documentReference2 und gibt einen Boolean zurück
     *
     * @return boolean
     */
    public boolean isEqual(){
        if (this.documentReference1.equalsDeep(this.documentReference2)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Gibt genau zurück an welcher Stelle sich DocumentReference1 und DocumentReference2 unterscheiden
     *
     * @return String
     */
    public String compareDeep(){

        String normalizedJson1 = ctx.newJsonParser().setPrettyPrint(false).encodeResourceToString(this.documentReference1);
        String normalizedJson2 = ctx.newJsonParser().setPrettyPrint(false).encodeResourceToString(this.documentReference2);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode1;
        JsonNode jsonNode2;

        {
            try {
                jsonNode1 = objectMapper.readTree(normalizedJson1);
                jsonNode2 = objectMapper.readTree(normalizedJson2);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


        JsonNode diff = JsonDiff.asJson(jsonNode1, jsonNode2);

        return diff.toPrettyString();
    }

}
