package com.isik.testsuit.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchAccessToken {

    private static FetchAccessToken instance;
    private static final Object LOCK = new Object();

    private static final String CONTENT_TYPE = "application/json";

    private FetchAccessToken() {
    }

    public static FetchAccessToken getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new FetchAccessToken();
                }
            }
        }
        return instance;
    }

    /**
     * Holt den Access Token anhand der config Datei vom ISiK Server
     *
     * @return
     */
    public String getAccessToken() {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String requestBody = "{\"username\":\"" + ConfigLoader.getInstance().getUsername() + "\",\"password\":\"" + ConfigLoader.getInstance().getPwd() + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ConfigLoader.getInstance().getBaseUrl() + ConfigLoader.getInstance().getPort() + ConfigLoader.getInstance().getEndPointLogin()))
                    .header("Content-Type", CONTENT_TYPE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return extractAccessToken(response.body());
            } else {
                System.out.println("Error: " + response.statusCode() + " - " + response.body());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Zieht den Access Token aus dem responseBody
     *
     * @param responseBody
     * @return
     */
    String extractAccessToken(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.path("accessToken").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
