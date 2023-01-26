package com.mfdogalindo.feedbacksurvey.domain.services;

import com.mfdogalindo.feedbacksurvey.adapters.repository.SurveyRepository;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService{

    private final SurveyRepository surveyRepository;

    SurveyServiceImpl(SurveyRepository surveyRepository){
        this.surveyRepository = surveyRepository;
    }
    @Override
    public List<Survey> listSurveys() {
        return this.surveyRepository.findAllEnabled();
    }

    @Override
    public Survey getSurvey(Long id) {
        return this.surveyRepository.findById(id).orElse(null);
    }

    @Override
    public Survey createSurvey(Survey survey) {
        return this.surveyRepository.save(survey);
    }

    @Override
    public void deleteSurvey(Long id) {
        this.surveyRepository.updateEnabledById(id, false);
    }
}
