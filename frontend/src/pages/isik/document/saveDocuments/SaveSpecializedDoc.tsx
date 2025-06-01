import React, { useState } from "react";
import {
  Button,
  Card,
  CardContent,
  Grid,
  TextField,
  Typography,
  CircularProgress,
  Link,
  Autocomplete,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";
import axios from "axios";
import MenuAppBar from "../../../../MenuAppBar";

function SaveSpecializedDoc() {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [openDialog, setOpenDialog] = useState(false);
  const [documentId, setDocumentId] = useState(""); // Zustand für Dokument-ID
  const options = {
    ID: ["dok-beispiel-client-with-binary-application-pdf-example-short"],
    status: ["current"],
    docStatus: ["final"],
    metaProfile: ["https://gematik.de/fhir/isik/v3/Dokumentenaustausch/StructureDefinition/ISiKDokumentenMetadaten"],
    metaSystem: ["http://terminology.hl7.org/CodeSystem/v3-ActReason"],
    metaCode: ["HTEST"],
    masterIdentifierSystem: ["urn:ietf:rfc:3986"],
    masterIdentifierValue: ["urn:oid:1.2.840.113556.1.8000.2554.58783.21864.3474.19410.44358.58254.41281.46340"],
    identifierSystem: ["urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8"],
    identifierSystemValue: ["129.6.58.42.33726"],
    kdlSystem: ["http://dvmd.de/fhir/CodeSystem/kdl"],
    kdlCode: ["PT130102"],
    kdlDisplay: ["Molekularpathologiebefund"],
    padId: [""],
    authorName: ["Maxine Mustermann"],
    securityLabelSystem: ["http://terminology.hl7.org/CodeSystem/v3-Confidentiality"],
    securityLabelCode: ["N"],
    securityLabelDisplay: ["normal"],
    type: ["replaces"],
    documentReference: [""],
    contentType: ["application/pdf"],
    language: ["de"],
    creationElement: ["2020-12-31T23:50:50+01:00"],
    formatSystem: ["http://ihe.net/fhir/ihe.formatcode.fhir/CodeSystem/formatcode"],
    formatCode: ["urn:ihe:iti:xds:2017:mimeTypeSufficient"],
    formatDisplay: ["mimeType Sufficient"],
    facilityTypeSystem: ["http://ihe-d.de/CodeSystems/PatientBezogenenGesundheitsversorgung"],
    facilityTypeCode: ["KHS"],
    facilityTypeDisplay: ["Krankenhaus"],
    practiceSettingSystem: ["http://ihe-d.de/CodeSystems/AerztlicheFachrichtungen"],
    practiceSettingCode: ["ALLG"],
    practiceSettingDisplay: ["T"],
    fallnummer: [""],
    dokumentBeschreibung: ["Hier das Dokument beschreiben"],
    destroyHash: ["false", "true"],
  };

  // @ts-ignore
  const [formData, setFormData] = useState(
    Object.keys(options).reduce((acc, key) => {
      // @ts-ignore
      acc[key] = options[key][0] || "";
      return acc;
    }, {})
  );

  // @ts-ignore
  const handleInputChange = (event, newValue, key) => {
    setFormData((prev) => ({
      ...prev,
      [key]: newValue || event.target.value, // Falls kein Wert ausgewählt wird, den eingegebenen Wert nehmen
    }));
  };

  // @ts-ignore
  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  // @ts-ignore
  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    const data = new FormData();
    if (file) {
      data.append("file", file);
    }
    Object.entries(formData).forEach(([key, value]) => {
      // @ts-ignore
      data.append(key, value);
    });

    console.log("Gesendete Daten:", Object.fromEntries(data.entries()));

    try {
      const response = await axios.post("http://localhost:8080/docs/uploadSpecialized", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Upload erfolgreich", response.data);
      setDocumentId(response.data.id);
      setOpenDialog(true);
    } catch (error) {
      console.error("Fehler beim Upload", error);
      alert("Fehler beim Upload!");
    }
    setLoading(false);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  return (
    <Grid style={{ maxWidth: 1500, padding: "5px 5px", margin: "0 auto", marginTop: 5 }}>
      <MenuAppBar />
      <Card style={{ maxWidth: 1500, padding: "5px 5px", margin: "0 auto", marginTop: 5 }}>
        <Grid item xs={12} sm={2}>
          <Link
            component="button"
            variant="body2"
            onClick={() => window.history.back()}
            style={{
              display: 'flex',
              alignItems: 'center',
              cursor: 'pointer',
              textDecoration: 'none',
            }}
          >
            <span style={{ marginRight: '8px' }}>←</span> Zurück
          </Link>
        </Grid>
        <Grid container justifyContent="center" style={{ marginTop: 20 }}>
          <Card style={{ padding: 20, width: "100%", minHeight: "80vh" }}>
            <CardContent>
              <Typography variant="h5" gutterBottom>
                Spezialisiertes Dokument speichern
              </Typography>
              <form onSubmit={handleSubmit}>
                <input type="file" onChange={handleFileChange} required />
                {Object.keys(formData).map((key) => (
                  <Autocomplete
                    key={key}
                    freeSolo
                    // @ts-ignore
                    options={options[key]}
                    // @ts-ignore
                    value={formData[key]}
                    onChange={(event, newValue) => handleInputChange(event, newValue, key)}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        fullWidth
                        margin="normal"
                        label={key}
                        name={key}
                        variant="outlined"
                        onChange={(event) => handleInputChange(event, null, key)}
                      />
                    )}
                  />
                ))}
                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }} disabled={loading}>
                  {loading ? <CircularProgress size={24} /> : "Speichern"}
                </Button>
              </form>
            </CardContent>
          </Card>
        </Grid>
      </Card>

      {/* Dialog für Dokument-ID */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Dokument gespeichert</DialogTitle>
        <DialogContent>
          <Typography variant="body1">
            Das Dokument wurde erfolgreich gespeichert. Dokument-ID: {documentId}
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            Schließen
          </Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}

export default SaveSpecializedDoc;
