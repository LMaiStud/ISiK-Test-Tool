import * as React from "react";
import {
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
  Button
} from "@mui/material";
import MenuAppBar from "../../../../MenuAppBar";
import { useLocation, useNavigate } from "react-router-dom";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ReactJson from "react-json-view";

function DocumentTransferHistory() {
  const location = useLocation();
  const navigate = useNavigate();
  const [data, setData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  const [loadingDocument, setLoadingDocument] = React.useState(null);
  const [token, setToken] = React.useState(null);
  const [loadingToken, setLoadingToken] = React.useState(true);

  React.useEffect(() => {
    fetch("http://localhost:8080/documentTransferHistory/getToken")
      .then((response) => response.ok ? response.text() : Promise.reject("Fehler beim Abrufen des Tokens"))
      .then((token) => {
        // @ts-ignore
        setToken(token);
        setLoadingToken(false);
      })
      .catch((error) => {
        console.error(error);
        setLoadingToken(false);
      });
  }, []);

  React.useEffect(() => {
    fetch("http://localhost:8080/documentTransferHistory/getAllDocumentTransferHistory")
      .then((response) => response.ok ? response.json() : Promise.reject("Netzwerkfehler"))
      .then((data) => {
        setData(data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error);
        setLoading(false);
      });
  }, []);

  // @ts-ignore
  const openDocument = async (url, id) => {
    if (!token) {
      console.error("Kein Token verfügbar");
      return;
    }

    setLoadingDocument(id);
    try {
      const response = await fetch(url, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) throw new Error("Fehler beim Laden des Dokuments");

      const blob = await response.blob();
      const blobUrl = URL.createObjectURL(blob);
      window.open(blobUrl, "_blank");
    } catch (error) {
      console.error(error);
    } finally {
      setLoadingDocument(null);
    }
  };

  // @ts-ignore
  const deleteDocument = async (id) => {
    try {
      const response = await fetch(`http://localhost:8080/documentTransferHistory/deleteById?id=${id}`, {
        method: "GET"
      });
      if (!response.ok) throw new Error("Fehler beim Löschen des Dokuments");
      // @ts-ignore
      setData((prevData) => prevData.filter((item) => item.id !== id));
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Grid style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
      <MenuAppBar />
      <Card style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
        <CardContent>
          <Grid container spacing={1}>
            <Grid item xs={12} sm={2}>
              <Link component="button" variant="body2" onClick={() => window.history.back()}>
                ← Zurück
              </Link>
            </Grid>
            <Grid item xs={12}>
              <Typography variant="h5">Dokumentenübertragungs-Historie</Typography>

              {loadingToken ? (
                <Typography variant="body2" color="textSecondary">
                  Token wird geladen... <CircularProgress size={20} />
                </Typography>
              ) : (
                <>
                  {loading && <CircularProgress />}
                  {error && <Typography color="error">{error}</Typography>}

                  {data && (
                    <TableContainer component={Paper} style={{ marginTop: 20 }}>
                      <Table>
                        <TableHead>
                          <TableRow>
                            <TableCell><strong>ID</strong></TableCell>
                            <TableCell><strong>KDL</strong></TableCell>
                            <TableCell><strong>KDL Name</strong></TableCell>
                            <TableCell><strong>Dokument Referenze</strong></TableCell>
                            <TableCell><strong>Fallnummer</strong></TableCell>
                            <TableCell><strong>Dokument</strong></TableCell>
                            <TableCell><strong>Patienten-ID</strong></TableCell>
                            <TableCell><strong>Erstellt am</strong></TableCell>
                            <TableCell><strong>Aktionen</strong></TableCell>
                          </TableRow>
                        </TableHead>
                        <TableBody>
                          {  // @ts-ignore
                            data.map((test) => (
                            <TableRow key={test.id}>
                              <TableCell>{test.id}</TableCell>
                              <TableCell>{test.kdl}</TableCell>
                              <TableCell>{test.kdlName}</TableCell>
                              <TableCell>
                                <Accordion>
                                  <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                    <Typography>anzeigen</Typography>
                                  </AccordionSummary>
                                  <AccordionDetails>
                                    <ReactJson
                                      src={typeof test.documentReference === "string" ? JSON.parse(test.documentReference) : test.documentReference}
                                      theme="bright"
                                      collapsed={1}
                                      enableClipboard={true}
                                      displayDataTypes={false}
                                    />
                                  </AccordionDetails>
                                </Accordion>
                              </TableCell>
                              <TableCell>{test.fallnummer}</TableCell>
                              <TableCell>
                                {loadingDocument === test.id ? (
                                  <CircularProgress size={20} />
                                ) : (
                                  <Link component="button" onClick={() => openDocument(test.url, test.id)}>
                                    anzeigen
                                  </Link>
                                )}
                              </TableCell>
                              <TableCell>{test.padId}</TableCell>
                              <TableCell>{new Date(test.createdAt).toLocaleString()}</TableCell>
                              <TableCell>
                                <Button color="error" onClick={() => deleteDocument(test.id)}>Löschen</Button>
                              </TableCell>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </TableContainer>
                  )}
                </>
              )}
            </Grid>
          </Grid>
        </CardContent>
      </Card>
    </Grid>
  );
}

export default DocumentTransferHistory;
