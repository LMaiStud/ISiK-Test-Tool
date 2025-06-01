import * as React from "react";
import {
  Card,
  CardContent,
  Grid,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import MenuAppBar from "../../../MenuAppBar";
import { useNavigate } from "react-router-dom";

function TestCaseLandingPage() {
  let navigate = useNavigate();
  const menuItems = [
    { text: "Testpläne erstellen", path: "/CreateTestCase" },
    { text: "Testpläne ausführen", path: "/TestCaseOverview" },
    { text: "zurück", path: "/" },
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
                  <h3>Testpläne verwalten</h3>
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

export default TestCaseLandingPage;
