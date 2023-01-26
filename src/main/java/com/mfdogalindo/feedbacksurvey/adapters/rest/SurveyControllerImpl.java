package com.mfdogalindo.feedbacksurvey.adapters.rest;

import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import com.mfdogalindo.feedbacksurvey.domain.services.SurveyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyControllerImpl implements SurveyController{

    private final SurveyService surveyService;

    SurveyControllerImpl(SurveyService surveyService){
        this.surveyService = surveyService;
    }

    @Override
    @GetMapping(produces = "application/json")
    public List<Survey> listSurveys(){
        return surveyService.listSurveys();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public Survey getSurvey(@PathVariable Long id){
        return surveyService.getSurvey(id);
    }

    @Override
    @PostMapping(produces = "application/json", consumes = "application/json")
    public Survey createSurvey(@RequestBody Survey survey){
        return surveyService.createSurvey(survey);
    }

    @Override
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public void deleteSurvey(@PathVariable Long id){
        surveyService.deleteSurvey(id);
    }
}
