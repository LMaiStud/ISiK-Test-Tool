import * as React from "react";
import {
  Card,
  CardContent,
  Grid,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import MenuAppBar from "./MenuAppBar";
import { useNavigate } from "react-router-dom";

function LandingPage() {
  let navigate = useNavigate();
  const menuItems = [
    { text: "Dokumente verwalten", path: "/LandingPageSaveDocuments" },
    { text: "Testpläne verwalten", path: "/TestCaseLandingPage" },
    { text: "Dokument ablegen/hochladen", path: "/ArchiveLandingPage" },
  ];

  // @ts-ignore
  const handleNavigate = (path) => {
    navigate(path);
  };

  return (
    <>
      <Grid style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
        <MenuAppBar />
        <Card style={{ maxWidth: 1500, padding: "5px", margin: "0 auto", marginTop: 5 }}>
          <CardContent>
      <Grid
        container
        direction="column"
        alignItems="center"
        justifyContent="center"
      >
        <Card style={{ minWidth: "300px", textAlign: "center" }}>
          <CardContent>
            <h3>Verfügbare Funktionen</h3>
            <List>
              {menuItems.map((item, index) => (
                <ListItem key={index} button onClick={() => handleNavigate(item.path)} divider>
                  <ListItemText primary={item.text} />
                </ListItem>
              ))}
            </List>
          </CardContent>
        </Card>
      </Grid>
          </CardContent>
        </Card>
      </Grid>
    </>
  );
}

export default LandingPage;
