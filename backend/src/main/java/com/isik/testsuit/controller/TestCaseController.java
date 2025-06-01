package com.isik.testsuit.controller;

import com.isik.testsuit.entity.TestCase;
import com.isik.testsuit.service.TestCaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/testcases")
public class TestCaseController {

    private final TestCaseService testCaseService;

    public TestCaseController(TestCaseService testCaseService) {
        this.testCaseService = testCaseService;
    }

    /**
     * neuen TestCase erstellen
     *
     * @param testName
     * @param testDescription
     * @param status
     * @param result
     * @param doc
     * @param testType
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/createTestCase")
    public ResponseEntity<TestCase> createTestCase(
            @RequestParam String testName,
            @RequestParam String testDescription,
            @RequestParam int status,
            @RequestParam int result,
            @RequestParam int[] doc,
            @RequestParam boolean[] testType) throws IOException, NoSuchAlgorithmException {

        TestCase savedTestCase = testCaseService.save(testName, testDescription, status, result, doc, testType);
        return ResponseEntity.ok(savedTestCase);
    }

    /**
     * update des TestCase mit der id
     *
     * @param testName
     * @param testDescription
     * @param status
     * @param result
     * @param doc
     * @param testType
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/updateTestCase")
    public ResponseEntity<TestCase> updateTestCase(
            @RequestParam Long id,
            @RequestParam String testName,
            @RequestParam String testDescription,
            @RequestParam int status,
            @RequestParam int result,
            @RequestParam int[] doc,
            @RequestParam boolean[] testType) throws IOException, NoSuchAlgorithmException {

        TestCase savedTestCase = testCaseService.update(id, testName, testDescription, status, result, doc, testType);
        return ResponseEntity.ok(savedTestCase);
    }


    /**
     * Gibt den TestCase mit der id zurück
     *
     * @param id
     * @return
     */
    @GetMapping("/getTestCase")
    public ResponseEntity<TestCase> getTestCase(@RequestParam Long id) {
        Optional<TestCase> testCase = testCaseService.findById(id);
        return testCase.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Gibt alle TestCases zurück
     *
     * @return
     */
    @GetMapping("/getAllTestCase")
    public ResponseEntity<List<TestCase>> getAllTestCase() {
        List<TestCase> testCases = testCaseService.getAll();
        if (testCases.isEmpty()) {
            return ResponseEntity.noContent().build(); // Falls keine Testfälle existieren
        }
        return ResponseEntity.ok(testCases);
    }

    /**
     * löscht den TestCase mit der id
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public ResponseEntity<Boolean> deleteById(@RequestParam Long id) {
        boolean output = testCaseService.deleteById(id);
        return ResponseEntity.ok(output);
    }
}
