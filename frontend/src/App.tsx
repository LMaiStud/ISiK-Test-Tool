import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import LandingPage from "./LandingPage";
import TestCaseOverview from "./pages/isik/testcase/TestCaseOverview"
import DocumentTransferHistory from "./pages/isik/document/archive/DocumentTransferHistory";
import StoredDocumentOverview from "./pages/isik/document/storedDocuments/StoredDocumentOverview";
import SaveDocuments from "./pages/isik/document/saveDocuments/SaveDocuments";
import CreateTestCase from "./pages/isik/testcase/CreateTestCase";
import ArchiveDocument from "./pages/isik/document/archive/ArchiveDocument";
import SaveSpecializedDoc from "./pages/isik/document/saveDocuments/SaveSpecializedDoc";
import TestCaseLandingPage from "./pages/isik/testcase/TestCaseLandingPage";
import ArchiveLandingPage from "./pages/isik/document/archive/ArchiveLandingPage";
import LandingPageSaveDocuments from "./pages/isik/document/saveDocuments/LandingPageSaveDocuments";
import UpdateTestCase from "./pages/isik/testcase/UpdateTestCase";



const App: React.FC = () => {
  return (
    <BrowserRouter>
      <div id="app-main-div">
        <div id="app-content-div">
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/TestCaseOverview" element={<TestCaseOverview />} />
            <Route path="/DocumentTransferHistory" element={<DocumentTransferHistory />} />
            <Route path="/ArchiveDocument" element={<ArchiveDocument />} />
            <Route path="/StoredDocumentOverview" element={<StoredDocumentOverview />} />
            <Route path="/SaveDocuments" element={<SaveDocuments />} />
            <Route path="/SaveSpecializedDoc" element={<SaveSpecializedDoc />} />
            <Route path="/CreateTestCase" element={<CreateTestCase />} />
            <Route path="/TestCaseLandingPage" element={<TestCaseLandingPage />} />
            <Route path="/ArchiveLandingPage" element={<ArchiveLandingPage />} />
            <Route path="/LandingPageSaveDocuments" element={<LandingPageSaveDocuments />} />
            <Route path="/UpdateTestCase/:testCaseId" element={<UpdateTestCase />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
};

export default App;
