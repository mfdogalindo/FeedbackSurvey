package com.mfdogalindo.feedbacksurvey.adapters.rest;

import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import com.mfdogalindo.feedbacksurvey.domain.services.SurveyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/surveys")
public class SurveyControllerImpl implements SurveyController{

    private final SurveyService surveyService;

    public SurveyControllerImpl(SurveyService surveyService){
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
    public boolean deleteSurvey(@PathVariable Long id){
        return surveyService.deleteSurvey(id);
    }
}
