package com.isik.testsuit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnexpectedSuccessExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "UnexpectedSuccessExceptionTest";
        DocHashException exception = new DocHashException(errorMessage);

        assertThat(exception).isInstanceOf(DocHashException.class);
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }
}
