package com.mfdogalindo.feedbacksurvey.domain.services;

import com.mfdogalindo.feedbacksurvey.adapters.repository.AnswerRepository;
import com.mfdogalindo.feedbacksurvey.adapters.repository.QuestionRepository;
import com.mfdogalindo.feedbacksurvey.adapters.repository.SurveyRepository;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.AnswerNotValidException;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.QuestionNotFoundException;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.SurveyNotFoundException;
import com.mfdogalindo.feedbacksurvey.domain.models.commands.CreateAnswersCommand;
import com.mfdogalindo.feedbacksurvey.domain.models.commands.QuestionAnswer;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Answer;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Question;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import com.mfdogalindo.feedbacksurvey.domain.models.enums.QuestionType;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository, SurveyRepository surveyRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public int save(CreateAnswersCommand answersCmd) {
        Survey currentSurvey = this.getSurvey(answersCmd.getSurveyId());
        List<Answer> toInsert = answersCmd.getQuestionAnswers().stream().map(questionAnswer -> {
            Question currentQuestion = this.getQuestion(questionAnswer.getQuestionId());
            Answer answer = new Answer();
            answer.setEmail(answersCmd.getEmail());
            answer.setSurvey(currentSurvey);
            answer.setQuestion(currentQuestion);
            this.validateAnswers(currentQuestion, questionAnswer);
            answer.setAnswers(questionAnswer.getAnswers());
            return answer;
        }).toList();

        toInsert.forEach(answerRepository::save);

        return toInsert.size() == answersCmd.getQuestionAnswers().size() ? toInsert.size() : 0 ;
    }

    @Override
    public List<Answer> findByEmail(String email) {
        return this.answerRepository.findByEmail(email);
    }

    @Override
    public List<Answer> findAll() {
        return this.answerRepository.findAll();
    }

    /**
     * Get the survey by id
     * @param surveyId the survey id
     * @return the survey
     * @throws SurveyNotFoundException if the survey is not found
     */
    private Survey getSurvey(Long surveyId) {
        return surveyRepository.findByIdAndEnabledTrue(surveyId)
                .orElseThrow(() -> new SurveyNotFoundException(String.format("Survey %d is not available", surveyId)));
    }

    /**
     * Get the question by id
     * @param questionId the question id
     * @return the question
     * @throws QuestionNotFoundException if the question is not found
     */
    private Question getQuestion(Long questionId){
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new QuestionNotFoundException(String.format("Question %d is not available", questionId)));
    }

    /**
     * Check if the answers are valid
     * @param currentQuestion the question to check
     * @param questionAnswer the answers to check
     * @throws AnswerNotValidException if the answers are not valid
     */
    private void validateAnswers(Question currentQuestion, QuestionAnswer questionAnswer) {
        if (!currentQuestion.getQuestion().equals(QuestionType.TEXT)) {
            questionAnswer.getAnswers().stream().forEach(option -> {
                        if (!currentQuestion.getOptions().contains(option)) {
                            throw new AnswerNotValidException(String.format("Answer - %s - is not valid for question %d", option, questionAnswer.getQuestionId()));
                        }
                    }
            );
        }
    }

}
