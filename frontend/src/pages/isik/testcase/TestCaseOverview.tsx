import * as React from "react";
import {
  Button,
  Card,
  CardContent,
  Grid,
  Link,
  Typography,
  CircularProgress,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  IconButton,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import DeleteIcon from "@mui/icons-material/Delete";
import MenuAppBar from "../../../MenuAppBar";
import { useLocation, useNavigate } from "react-router-dom";
import { Link as RouterLink } from "react-router-dom";

function TestCaseOverview() {
  const location = useLocation();
  const navigate = useNavigate();
  const [data, setData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = React.useState(false);
  const [selectedTestId, setSelectedTestId] = React.useState(null);

  const [docDescriptions, setDocDescriptions] = React.useState({});

  const fetchData = () => {
    fetch("http://localhost:8080/testcases/getAllTestCase")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Netzwerkfehler");
        }
        return response.json();
      })
      .then((data) => {
        setData(data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  };


  // @ts-ignore
  const fetchDocumentDescription = (docID) => {
    // @ts-ignore
    if (docDescriptions[docID]) return;

    fetch(`http://localhost:8080/docs/${docID}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Fehler beim Abrufen der Dokumentbeschreibung");
        }
        return response.json();
      })
      .then((docData) => {
        setDocDescriptions((prevDescriptions) => ({
          ...prevDescriptions,
          [docID]: docData.documentBeschreibung || "Keine Beschreibung vorhanden",
        }));
      })
      .catch((error) => {
        console.error("Fehler:", error);
      });
  };

  React.useEffect(() => {
    fetchData();
    const intervalId = setInterval(fetchData, 1000);
    return () => clearInterval(intervalId);
  }, []);

  // @ts-ignore
  const executeTest = (id) => {
    fetch(`http://localhost:8080/testExecute/execute?id=${id}`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Fehler beim Ausführen des Tests");
        }
        return response.json();
      })
      .then(() => {
        fetchData();
      })
      .catch((error) => {
        console.error("Fehler:", error);
      });
  };

  const handleDeleteTestCase = () => {
    fetch(`http://localhost:8080/testcases/deleteById?id=${selectedTestId}`, {
      method: "GET",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Fehler beim Löschen des Testcases");
        }
        return response.json();
      })
      .then(() => {
        setDeleteDialogOpen(false);
        fetchData();
      })
      .catch((error) => {
        console.error("Fehler:", error);
      });
  };

  return (
    <Grid style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
      <MenuAppBar />
      <Card style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
        <CardContent>
          <Grid container spacing={1}>
            <Grid item xs={12} sm={2}>
              <Link component="button" variant="body2" onClick={() => navigate("/TestCaseLandingPage")}>Zurück</Link>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="h5">Test Cases</Typography>
              {loading && <CircularProgress />}
              {error && <Typography color="error">{error}</Typography>}

              {data && (
                <TableContainer component={Paper} style={{ marginTop: 20 }}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell><strong>ID</strong></TableCell>
                        <TableCell><strong>Test Name</strong></TableCell>
                        <TableCell><strong>Beschreibung</strong></TableCell>
                        <TableCell><strong>Status</strong></TableCell>
                        <TableCell><strong>Exception</strong></TableCell>
                        <TableCell><strong>Dokumente</strong></TableCell>
                        <TableCell><strong>Aktion</strong></TableCell>
                        <TableCell><strong> </strong></TableCell>
                        <TableCell><strong> </strong></TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {// @ts-ignore
                        data.map((test) => (
                          <TableRow key={test.id}>
                            <TableCell>{test.id}</TableCell>
                            <TableCell>{test.testName}</TableCell>
                            <TableCell>{test.testDescription}</TableCell>
                            <TableCell>
                              {test.status === 0 && <Typography color="textSecondary">Test noch nicht durchgeführt</Typography>}
                              {test.status === 1 && <Typography style={{ color: "green" }}>Test erfolgreich ✅</Typography>}
                              {test.status === 2 && <Typography style={{ display: "flex", alignItems: "center" }}><PlayArrowIcon style={{ marginRight: 5 }} /> Test läuft gerade</Typography>}
                              {test.status === 3 && <Typography style={{ color: "red" }}>Test fehlgeschlagen ❌</Typography>}
                            </TableCell>
                            <TableCell>{test.exception || "Keine"}</TableCell>
                            <TableCell>
                              <Accordion>
                                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                  <Typography>anzeigen</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                  {// @ts-ignore
                                    test.testCaseDocs.map((doc, index) => {
                                      fetchDocumentDescription(doc.docID);
                                      return (
                                        <Grid key={index} container spacing={1}>
                                          <Grid item xs={12}>
                                            <Typography>
                                              <strong>DocID:</strong> {doc.docID},
                                              <strong> TestType:</strong> {doc.testType ? "✅" : "❌"}
                                            </Typography>
                                          </Grid>
                                          <Grid item xs={12}>
                                            <Typography>
                                              <strong>Beschreibung:</strong> {
                                              // @ts-ignore
                                              docDescriptions[doc.docID] || "Lade..."}
                                            </Typography>
                                          </Grid>
                                        </Grid>
                                      );
                                    })
                                  }
                                </AccordionDetails>
                              </Accordion>
                            </TableCell>
                            <TableCell>
                              <Button variant="contained" color="primary" onClick={() => executeTest(test.id)} disabled={test.status === 2}>ausführen</Button>
                            </TableCell>
                            <TableCell>
                              <RouterLink to={`/UpdateTestCase/${test.id}`}>
                                <Button variant="contained" color="secondary">Bearbeiten</Button>
                              </RouterLink>
                            </TableCell>
                            <TableCell>
                              <IconButton color="error" onClick={() => { setSelectedTestId(test.id); setDeleteDialogOpen(true); }}>
                                <DeleteIcon />
                              </IconButton>
                            </TableCell>
                          </TableRow>
                        ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              )}
            </Grid>
          </Grid>
        </CardContent>
      </Card>

      <Dialog open={deleteDialogOpen} onClose={() => setDeleteDialogOpen(false)}>
        <DialogTitle>Testfall löschen</DialogTitle>
        <DialogContent>
          <DialogContentText>Sind Sie sicher, dass Sie diesen Testfall löschen möchten?</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)} color="primary">Abbrechen</Button>
          <Button onClick={handleDeleteTestCase} color="secondary">Löschen</Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}

export default TestCaseOverview;
