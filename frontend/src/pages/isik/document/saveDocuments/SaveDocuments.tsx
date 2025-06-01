import * as React from "react";
import {
  Button,
  Card,
  CardContent,
  Grid,
  Link,
  TextField,
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  CircularProgress
} from "@mui/material";
import { useState } from "react";
import axios from "axios";
import MenuAppBar from "../../../../MenuAppBar";
import { useNavigate } from "react-router-dom";

function SaveDocuments() {
  let navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    kdl: "",
    padId: "",
    kdlName: "",
    documentReference: "",
    fallnummer: "",
    dokumentBeschreibung: ""
  });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");

  // @ts-ignore
  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  // @ts-ignore
  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  // @ts-ignore
  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    const isFormValid = Object.values(formData).every(value => value.trim() !== "");

    if (!isFormValid) {
      setDialogMessage("Bitte füllen Sie alle Textfelder aus.");
      setDialogOpen(true);
      setLoading(false);
      return;
    }

    const data = new FormData();
    if (file) {
      data.append("file", file);
    }
    Object.entries(formData).forEach(([key, value]) => {
      data.append(key, value);
    });

    try {
      const response = await axios.post("http://localhost:8080/docs/upload", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Upload erfolgreich", response.data);
      setDialogMessage(`Upload erfolgreich! Dokument-ID: ${response.data.id}`);
      setDialogOpen(true);
      setFormData({ kdl: "", padId: "", kdlName: "", documentReference: "", fallnummer: "", dokumentBeschreibung: "" });
      setFile(null);
      // @ts-ignore
      document.querySelector('input[type="file"]').value = "";
    } catch (error) {
      console.error("Fehler beim Upload", error);
      setDialogMessage("Fehler beim Upload!");
      setDialogOpen(true);
    }
    setLoading(false);
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
              <Typography variant="h5" gutterBottom>Dokument speichern</Typography>
              <form onSubmit={handleSubmit}>
                <input type="file" onChange={handleFileChange} required />
                <TextField fullWidth margin="normal" label="KDL" name="kdl" value={formData.kdl} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Pad ID" name="padId" value={formData.padId} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="KDL Name" name="kdlName" value={formData.kdlName} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Document Reference" name="documentReference" value={formData.documentReference} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Fallnummer" name="fallnummer" value={formData.fallnummer} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Dokument Beschreibung" name="dokumentBeschreibung" value={formData.dokumentBeschreibung} onChange={handleChange} />
                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }} disabled={loading}>
                  {loading ? <CircularProgress size={24} /> : "Hochladen"}
                </Button>
              </form>
            </CardContent>
          </Card>
        </Grid>
      </Card>
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
        <DialogTitle>Information</DialogTitle>
        <DialogContent>
          <DialogContentText>{dialogMessage}</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} autoFocus>Okay</Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}

export default SaveDocuments;
