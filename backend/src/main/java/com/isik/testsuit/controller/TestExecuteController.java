package com.isik.testsuit.controller;

import com.isik.testsuit.service.TestExecuteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testExecute")
public class TestExecuteController {

    TestExecuteService testExecuteService;

    public TestExecuteController(TestExecuteService testExecuteService) {
        this.testExecuteService = testExecuteService;
    }

    /**
     * führt den TestCase mit der id aus und gibt status zurück
     * <p>
     *
     * @param id
     * @return
     */
    @PostMapping("/execute")
    public ResponseEntity<String> executeTest(@RequestParam("id") Long id) {
        testExecuteService.executeAsync(id);
        return new ResponseEntity<>("Test Execute Start", HttpStatus.CREATED);
    }

}
