package com.mfdogalindo.feedbacksurvey.domain.models.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionAnswer {
    private Long questionId;
    private List<String> answers;
}
