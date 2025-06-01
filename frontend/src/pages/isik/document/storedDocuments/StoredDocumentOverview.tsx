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
  AccordionDetails, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, IconButton
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import MenuAppBar from "../../../../MenuAppBar";
import { useNavigate } from "react-router-dom";
import ReactJson from "react-json-view";
import DeleteIcon from "@mui/icons-material/Delete";

function StoredDocumentOverview() {
  const navigate = useNavigate();
  const [data, setData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  const [loadingDocumentId, setLoadingDocumentId] = React.useState(null); // ID des ladenden Dokuments
  const [deleteDialogOpen, setDeleteDialogOpen] = React.useState(false);
  const [selectedTestId, setSelectedTestId] = React.useState(null);

  React.useEffect(() => {
    fetch("http://localhost:8080/docs/getAllDoc")
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
  }, []);

  // @ts-ignore
  const fetchDocument = async (id) => {
    setLoadingDocumentId(id);

    try {
      const response = await fetch(`http://localhost:8080/docs/getDocumentAsBase64ById?id=${id}`);

      if (!response.ok) {
        throw new Error("Fehler beim Laden des Dokuments");
      }

      const blob = await response.blob();
      const blobUrl = URL.createObjectURL(blob);
      window.open(blobUrl, "_blank");
    } catch (error) {
      console.error("Fehler:", error);
    } finally {
      setLoadingDocumentId(null);
    }
  };

  const fetchDocuments = () => {
    setLoading(true);
    fetch("http://localhost:8080/docs/getAllDoc")
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


  const handleDeleteDocument = () => {
    fetch(`http://localhost:8080/docs/deleteById?id=${selectedTestId}`, {
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
        fetchDocuments();
      })
      .catch((error) => {
        console.error("Fehler:", error);
      });
  };

  return (
    <Grid
      style={{
        maxWidth: 1500,
        padding: "5px",
        margin: "0 auto",
        marginTop: 5,
      }}
    >
      <MenuAppBar />
      <Card
        style={{
          maxWidth: 1500,
          padding: "5px",
          margin: "0 auto",
          marginTop: 5,
        }}
      >
        <CardContent>
          <Grid container spacing={1}>
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
            <Grid item xs={12}>
              <Typography variant="h5">Dokumente</Typography>
              {loading && <CircularProgress />}
              {error && <Typography color="error">{error}</Typography>}

              {data && (
                <TableContainer component={Paper} style={{ marginTop: 20 }}>
                  <Table>
                    <TableHead>
                      <TableRow>
                        <TableCell><strong>ID</strong></TableCell>
                        <TableCell><strong>Beschreibung</strong></TableCell>
                        <TableCell><strong>KDL</strong></TableCell>
                        <TableCell><strong>PAD-ID</strong></TableCell>
                        <TableCell><strong>KDL-Name</strong></TableCell>
                        <TableCell><strong>Fallnummer</strong></TableCell>
                        <TableCell><strong>DocRef</strong></TableCell>
                        <TableCell><strong>DocRef-Code</strong></TableCell>
                        <TableCell><strong>Hash-Wert</strong></TableCell>
                        <TableCell><strong>anzeigen</strong></TableCell>
                        <TableCell><strong> </strong></TableCell>
                        <TableCell><strong>mimeType</strong></TableCell>
                      </TableRow>
                    </TableHead>
                    <TableBody>
                      {// @ts-ignore
                        data.map((test) => (
                        <TableRow key={test.id}>
                          <TableCell>{test.id}</TableCell>
                          <TableCell>{test.documentBeschreibung}</TableCell>
                          <TableCell>{test.kdl}</TableCell>
                          <TableCell>{test.padId}</TableCell>
                          <TableCell>{test.kdlName}</TableCell>
                          <TableCell>{test.fallNummer}</TableCell>
                          <TableCell>
                            <Accordion>
                              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography>DocRef anzeigen</Typography>
                              </AccordionSummary>
                              <AccordionDetails>
                                <ReactJson
                                  src={typeof test.docRef_json === "string" ? JSON.parse(test.docRef_json) : test.docRef_json}
                                  theme="bright"
                                  collapsed={1}
                                  enableClipboard={true}
                                  displayDataTypes={false}
                                />
                              </AccordionDetails>
                            </Accordion>
                          </TableCell>
                          <TableCell>{test.documentReferenceCode}</TableCell>
                          <TableCell>
                            <Accordion>
                              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography>Hash</Typography>
                              </AccordionSummary>
                              <AccordionDetails>
                                <Typography>
                                  <pre
                                    style={{
                                      whiteSpace: "pre-wrap",
                                      wordBreak: "break-word",
                                      maxHeight: "300px",
                                      overflowY: "auto",
                                    }}
                                  >
                                    {JSON.stringify(test.hash, null, 2)}
                                  </pre>
                                </Typography>
                              </AccordionDetails>
                            </Accordion>
                          </TableCell>
                          <TableCell>
                            <Button
                              variant="contained"
                              color="primary"
                              onClick={() => fetchDocument(test.id)}
                              disabled={loadingDocumentId === test.id}
                            >
                              {loadingDocumentId === test.id ? <CircularProgress size={20} /> : "Anzeigen"}
                            </Button>
                          </TableCell>
                          <TableCell>
                            <IconButton color="error" onClick={() => { setSelectedTestId(test.id); setDeleteDialogOpen(true); }}>
                              <DeleteIcon />
                            </IconButton>
                          </TableCell>
                          <TableCell>{test.mimeType}</TableCell>
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
          <DialogContentText>Sind Sie sicher, dass Sie dieses Dokument löschen möchten?</DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialogOpen(false)} color="primary">Abbrechen</Button>
          <Button onClick={handleDeleteDocument} color="secondary">Löschen</Button>
        </DialogActions>
      </Dialog>
    </Grid>
  );
}

export default StoredDocumentOverview;
