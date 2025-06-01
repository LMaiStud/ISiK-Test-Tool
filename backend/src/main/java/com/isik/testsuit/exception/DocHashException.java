package com.isik.testsuit.exception;

public class DocHashException extends Exception {

    /**
     * Exception, wenn der Hash des gesendeten Dokuments nicht mit dem empfangenen übereinstimmt
     *
     * @param message
     */
    public DocHashException(String message) {
        super(message);
    }
}
