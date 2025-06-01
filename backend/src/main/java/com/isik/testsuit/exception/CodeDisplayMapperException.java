package com.isik.testsuit.exception;

public class CodeDisplayMapperException extends Exception {

    /**
     * Exception wenn kein Code zum Display gefunden wurde
     * Exception wenn kein Display zum Code gefunden wurde
     *
     * @param message
     */
    public CodeDisplayMapperException(String message) {
        super(message);
    }
}
