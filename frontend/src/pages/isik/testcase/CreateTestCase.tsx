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
  IconButton,
  CircularProgress,
} from "@mui/material";
import { useState, useEffect } from "react";
import axios from "axios";
import MenuAppBar from "../../../MenuAppBar";
import { useNavigate } from "react-router-dom";
import DeleteIcon from '@mui/icons-material/Delete';

function CreateTestCase() {
  let navigate = useNavigate();
  const [formData, setFormData] = useState({
    testName: "",
    testDescription: "",
    docIDs: [],
    testTypes: [],
    selectedDocID: "",
    selectedTestType: "",
    availableDocs: []
  });
  const [dialogOpen, setDialogOpen] = useState(false);
  const [dialogMessage, setDialogMessage] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchDocuments = async () => {
      try {
        const response = await axios.get("http://localhost:8080/docs/getAllDoc");
        // @ts-ignore
        setFormData((prevState) => ({
          ...prevState,
          availableDocs: Array.isArray(response.data) ? response.data : []
        }));
      } catch (error) {
        console.error("Fehler beim Abrufen der Dokumente", error);
        setDialogMessage("Fehler beim Abrufen der Dokumente!");
        setDialogOpen(true);
      }
    };

    fetchDocuments();
  }, []);

  // @ts-ignore
  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  const handleAddDoc = () => {
    if (formData.selectedDocID && formData.selectedTestType !== "") {
      const newDocID = formData.selectedDocID;
      const newTestType = formData.selectedTestType === "true";
      setFormData({
        ...formData,
        // @ts-ignore
        docIDs: [...formData.docIDs, newDocID],
        // @ts-ignore
        testTypes: [...formData.testTypes, newTestType],
        selectedDocID: "",
        selectedTestType: "",
      });
    }
  };

  // @ts-ignore
  const handleRemoveDoc = (index) => {
    const newDocIDs = [...formData.docIDs];
    const newTestTypes = [...formData.testTypes];
    newDocIDs.splice(index, 1);
    newTestTypes.splice(index, 1);
    setFormData({
      ...formData,
      docIDs: newDocIDs,
      testTypes: newTestTypes,
    });
  };

  // @ts-ignore
  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    const data = new FormData();
    data.append('testName', formData.testName);
    data.append('testDescription', formData.testDescription);
    data.append('status', '0');
    data.append('result', '0');
    data.append('exception', '');
    data.append('doc', formData.docIDs.join(','));
    data.append('testType', formData.testTypes.map(type => type ? 'true' : 'false').join(','));

    const headers = {
      'Content-Type': 'multipart/form-data',
    };

    const config = {
      method: 'post',
      maxBodyLength: Infinity,
      url: 'http://localhost:8080/testcases/createTestCase',
      headers: headers,
      data: data,
    };

    try {
      const response = await axios.request(config);
      console.log("Testfall erfolgreich erstellt", response.data);
      setDialogMessage(`Testfall erfolgreich erstellt! Testfall-ID: ${response.data.id}`);
      setDialogOpen(true);
      // @ts-ignore
      setFormData({ testName: "", testDescription: "", docIDs: [], testTypes: [], selectedDocID: "", selectedTestType: "" });
    } catch (error) {
      console.error("Fehler beim Erstellen des Testfalls", error);
      setDialogMessage("Fehler beim Erstellen des Testfalls!");
      setDialogOpen(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Grid style={{ maxWidth: 1500, padding: "5px 5px", margin: "0 auto", marginTop: 5 }}>
      <MenuAppBar />
      <Card style={{ maxWidth: 1500, padding: "20px", margin: "0 auto", marginTop: 5 }}>
        <Grid item xs={12} sm={2}>
          <Link component="button" variant="body2" onClick={() => navigate("/TestCaseLandingPage")}>
            Zurück
          </Link>
        </Grid>
        <Grid container justifyContent="center" style={{ marginTop: 20 }}>
          <Card style={{ padding: 20, width: "100%", minHeight: "80vh" }}>
            <CardContent>
              <Typography variant="h5" gutterBottom>
                Testfall erstellen
              </Typography>
              <form onSubmit={handleSubmit}>
                <TextField fullWidth margin="normal" label="Testname" name="testName" value={formData.testName} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Testbeschreibung" name="testDescription" value={formData.testDescription} onChange={handleChange} />

                <Grid container spacing={2} alignItems="center" style={{ marginTop: 20 }}>
                  <Grid item xs={5}>
                    <FormControl fullWidth>
                      <InputLabel>Dokument ID</InputLabel>
                      <Select value={formData.selectedDocID} onChange={handleChange} name="selectedDocID" label="Dokument ID">
                        {Array.isArray(formData.availableDocs) && formData.availableDocs.map((doc) => (
                          <MenuItem
                            // @ts-ignore
                            key={doc.id} value={doc.id}>
                            {// @ts-ignore
                              doc.id} - {doc.documentBeschreibung}
                          </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </Grid>

                  <Grid item xs={5}>
                    <FormControl fullWidth>
                      <InputLabel>Testtyp</InputLabel>
                      <Select value={formData.selectedTestType} onChange={handleChange} name="selectedTestType" label="Testtyp">
                        <MenuItem value="true">True</MenuItem>
                        <MenuItem value="false">False</MenuItem>
                      </Select>
                    </FormControl>
                  </Grid>

                  <Grid item xs={2}>
                    <Button variant="contained" color="primary" fullWidth style={{ marginTop: 20 }} onClick={handleAddDoc}>
                      Hinzufügen
                    </Button>
                  </Grid>
                </Grid>

                {formData.docIDs.length > 0 && (
                  <div style={{ marginTop: 20 }}>
                    <Typography variant="body1" gutterBottom>
                      Hinzugefügte Dokumente:
                    </Typography>
                    {formData.docIDs.map((docID, index) => (
                      <Grid key={index} container spacing={2} alignItems="center">
                        <Grid item xs={10}>
                          <Typography variant="body2">
                            Dokument ID {docID} (Testtyp: {formData.testTypes[index] ? "True" : "False"})
                          </Typography>
                        </Grid>
                        <Grid item xs={2}>
                          <IconButton onClick={() => handleRemoveDoc(index)} color="secondary">
                            <DeleteIcon />
                          </IconButton>
                        </Grid>
                      </Grid>
                    ))}
                  </div>
                )}

                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: 20 }} disabled={loading}>
                  {loading ? <CircularProgress size={24} /> : "Testfall erstellen"}
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

export default CreateTestCase;
