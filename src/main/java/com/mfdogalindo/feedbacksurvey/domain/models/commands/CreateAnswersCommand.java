package com.mfdogalindo.feedbacksurvey.domain.models.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CreateAnswersCommand {

    private String email;
    private Long surveyId;
    private List<QuestionAnswer> questionAnswers;
}
