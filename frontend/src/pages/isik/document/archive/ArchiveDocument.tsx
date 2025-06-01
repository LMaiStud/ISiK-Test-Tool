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
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  CircularProgress
} from "@mui/material";
import { useState } from "react";
import axios from "axios";
import MenuAppBar from "../../../../MenuAppBar";
import { useNavigate } from "react-router-dom";

function ArchiveDocument() {
  let navigate = useNavigate();
  const [file, setFile] = useState(null);
  const [formData, setFormData] = useState({
    kdl: "",
    padId: "",
    kdlName: "",
    documentReference: "",
    fallnummer: "",
  });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const kdlOptions = [
    { kdl: "PT130102", kdlName: "Molekularpathologiebefund" },
    { kdl: "OP150106", kdlName: "OP-Protokoll" },
    { kdl: "OP150105", kdlName: "OP-Checkliste" },
    { kdl: "AU050199", kdlName: "Sonstiges Einweisungs-/Überweisungsdokument" },
    { kdl: "AU190103", kdlName: "Notaufnahmebogen" },
    { kdl: "ED110111", kdlName: "eMutterpass" },
    { kdl: "ED110109", kdlName: "Pflegebericht" },
  ];

  // @ts-ignore
  const handleChange = (event) => {
    const [selectedKdl, selectedKdlName] = event.target.value.split("-");
    setFormData({ ...formData, kdl: selectedKdl, kdlName: selectedKdlName });
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
      data.append(key, value);
    });

    try {
      const response = await axios.post("http://localhost:8080/docs/archive", data, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      console.log("Upload erfolgreich", response.data);
      setDialogMessage(`Upload erfolgreich! Dokument-ID: ${response.data.id}`);
      setDialogOpen(true);
      setFormData({ kdl: "", padId: "", kdlName: "", documentReference: "", fallnummer: "" });
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
              <Typography variant="h5" gutterBottom>Dokument archivieren</Typography>
              <form onSubmit={handleSubmit}>
                <input type="file" onChange={handleFileChange} required />
                <FormControl fullWidth margin="normal">
                  <InputLabel>KDL & KDL Name</InputLabel>
                  <Select
                    name="kdlCombo"
                    value={`${formData.kdl}-${formData.kdlName}`}
                    onChange={handleChange}
                  >
                    {kdlOptions.map(({ kdl, kdlName }) => (
                      <MenuItem key={kdl} value={`${kdl}-${kdlName}`}>
                        {kdl} - {kdlName}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                <TextField fullWidth margin="normal" label="Pad ID" name="padId" value={formData.padId} onChange={(e) => setFormData({ ...formData, padId: e.target.value })} />
                <TextField fullWidth margin="normal" label="Fallnummer" name="fallnummer" value={formData.fallnummer} onChange={(e) => setFormData({ ...formData, fallnummer: e.target.value })} />
                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }} disabled={loading}>
                  {loading ? <CircularProgress size={24} /> : "Archivieren"}
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

export default ArchiveDocument;
