package com.isik.testsuit.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FetchAccessTokenTest {

    @InjectMocks
    private FetchAccessToken fetchAccessToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtractAccessToken() throws Exception {

        String responseBody = "{\"accessToken\": \"validAccessToken\"}";
        FetchAccessToken spyFetchAccessToken = spy(fetchAccessToken);

        String extractedToken = spyFetchAccessToken.extractAccessToken(responseBody);

        assertEquals("validAccessToken", extractedToken);
    }

    @Test
    void testExtractAccessToken_InvalidJson() throws Exception {
        String invalidResponseBody = "invalid json";

        String extractedToken = fetchAccessToken.extractAccessToken(invalidResponseBody);

        assertNull(extractedToken);
    }
}