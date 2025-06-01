package com.isik.testsuit.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaDataExceptionTest {

    @Test
    void testExceptionMessage() {
        String errorMessage = "MetaDataExceptionTest";
        DocHashException exception = new DocHashException(errorMessage);

        assertThat(exception).isInstanceOf(DocHashException.class);
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }

}
