package com.mfdogalindo.feedbacksurvey.domain.exceptions;

public class AnswerNotValidException extends RuntimeException {
    public AnswerNotValidException(String message) {
        super(message);
    }
}
