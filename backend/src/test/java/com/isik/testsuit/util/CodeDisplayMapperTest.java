package com.isik.testsuit.util;

import com.isik.testsuit.exception.CodeDisplayMapperException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodeDisplayMapperTest {

    private CodeDisplayMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = CodeDisplayMapper.getInstance();
    }

    @Test
    public void testGetDisplay() throws CodeDisplayMapperException {
        String code = "AD010102";
        String expectedDisplay = "Durchgangsarztbericht";

        assertEquals(expectedDisplay, mapper.getDisplay(code));
    }

    @Test
    public void testGetCode() throws CodeDisplayMapperException {
        String display = "Durchgangsarztbericht";
        String expectedCode = "AD010102";

        assertEquals(expectedCode, mapper.getCode(display));
    }

    @Test
    public void testGetDisplayWithUnknownCode() {
        String code = "UNKNOWN_CODE";

        assertThrows(CodeDisplayMapperException.class, () -> {
            mapper.getDisplay(code);
        });
    }

    @Test
    public void testGetCodeWithUnknownDisplay() {
        String display = "Unbekannter Display";

        assertThrows(CodeDisplayMapperException.class, () -> {
            mapper.getCode(display);
        });
    }
}