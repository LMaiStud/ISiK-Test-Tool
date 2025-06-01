package com.isik.testsuit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DocHashExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "DocHashExceptionTest";
        DocHashException exception = new DocHashException(errorMessage);

        assertThat(exception).isInstanceOf(DocHashException.class);
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }
}
