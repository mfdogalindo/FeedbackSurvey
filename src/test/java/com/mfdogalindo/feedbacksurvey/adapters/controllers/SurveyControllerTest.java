package com.mfdogalindo.feedbacksurvey.adapters.controllers;

import com.mfdogalindo.feedbacksurvey.adapters.rest.SurveyControllerImpl;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Question;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import com.mfdogalindo.feedbacksurvey.domain.models.enums.QuestionType;
import com.mfdogalindo.feedbacksurvey.domain.services.SurveyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SurveyControllerTest {

    @Mock
    private SurveyService surveyService;

    @InjectMocks
    private SurveyControllerImpl surveyController;

    @BeforeEach
    void setUp() {
        this.surveyController = new SurveyControllerImpl(this.surveyService);
    }

    @Test
    void GetListSurveysHasResults() {
        List<Survey> surveys = new ArrayList<>();

        List<Question> questions = new ArrayList<>();

        questions.add(Question.builder()
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());
        questions.add(Question.builder()
                .question("¿Te gusta el té?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());

        surveys.add(Survey.builder()
                .name("Encuesta de café")
                .enabled(true)
                .questions(questions)
                .build());

        when(surveyService.listSurveys()).thenReturn(surveys);

        assert(surveyController.listSurveys().size() == surveys.size());
    }

    @Test
    void GetSurveyByIdHasResult(){
        List<Question> questions = new ArrayList<>();

        questions.add(Question.builder()
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());
        questions.add(Question.builder()
                .question("¿Te gusta el té?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());

        Survey survey = Survey.builder()
                .name("Encuesta de café")
                .enabled(true)
                .questions(questions)
                .build();

        when(surveyService.getSurvey(any(Long.class))).thenReturn(survey);


        assert(surveyController.getSurvey(1L).getName().equals(survey.getName()));
    }

    @Test
    void CreateSurveyHasResult(){
        List<Question> questions = new ArrayList<>();

        questions.add(Question.builder()
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());
        questions.add(Question.builder()
                .question("¿Te gusta el té?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build());

        Survey survey = Survey.builder()
                .name("Encuesta de café")
                .enabled(true)
                .questions(questions)
                .build();

        when(surveyService.createSurvey(any(Survey.class))).thenReturn(survey);

        assert(surveyController.createSurvey(survey).getName().equals(survey.getName()));
    }

    @Test
    void deleteSurveyHasResult(){
        when(surveyService.deleteSurvey(any(Long.class))).thenReturn(true);

        assert(surveyController.deleteSurvey(1L));
    }
}
