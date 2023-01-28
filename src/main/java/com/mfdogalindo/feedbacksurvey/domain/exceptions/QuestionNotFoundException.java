package com.mfdogalindo.feedbacksurvey.domain.exceptions;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(String message) {
        super(message);
    }

}

