package com.isik.testsuit.util;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void testCalculateMD5() throws NoSuchAlgorithmException {
        String input = "Hello, World!";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        String expectedMD5 = "65a8e27d8879283831b664bd8b7f0ad4";

        assertEquals(expectedMD5, Util.calculateMD5(inputBytes), "MD5 Hash does not match expected value.");
    }

    @Test
    void testCalculateHash_SHA256() throws NoSuchAlgorithmException {
        String input = "Hello, World!";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        String expectedSHA256 = "dffd6021bb2bd5b0af676290809ec3a53191dd81c7f70a4b28688a362182986f";

        assertEquals(expectedSHA256, Util.calculateHash(inputBytes, "SHA-256"), "SHA-256 Hash does not match expected value.");
    }

    @Test
    void testCalculateHash_InvalidAlgorithm() {
        String input = "Hello, World!";
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

        assertThrows(NoSuchAlgorithmException.class, () -> Util.calculateHash(inputBytes, "INVALID-HASH"));
    }
}
