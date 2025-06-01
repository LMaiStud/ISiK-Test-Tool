package com.isik.testsuit.exception;

public class MetaDataException extends Exception {

    /**
     * Exception, wenn die Metadaten des gesendeten Dokuments nicht mit denen des empfangenen übereinstimmen
     *
     * @param errorMessage
     */
    public MetaDataException(String errorMessage) {
        super(errorMessage);
    }
}
