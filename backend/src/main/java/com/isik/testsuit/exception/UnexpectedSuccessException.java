package com.isik.testsuit.exception;

public class UnexpectedSuccessException extends Exception {

    /**
     * Exception, wenn ein Test erfolgreich ist, obwohl er fehlschlagen müsste
     *
     * @param message
     */
    public UnexpectedSuccessException(String message) {
        super(message);
    }
}
