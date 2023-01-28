package com.mfdogalindo.feedbacksurvey.domain.services;

import com.mfdogalindo.feedbacksurvey.adapters.repository.AnswerRepository;
import com.mfdogalindo.feedbacksurvey.adapters.repository.SurveyRepository;
import com.mfdogalindo.feedbacksurvey.adapters.repository.QuestionRepository;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.AnswerNotValidException;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.NoResultsException;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.QuestionNotFoundException;
import com.mfdogalindo.feedbacksurvey.domain.exceptions.SurveyNotFoundException;
import com.mfdogalindo.feedbacksurvey.domain.models.commands.CreateAnswersCommand;
import com.mfdogalindo.feedbacksurvey.domain.models.commands.QuestionAnswer;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Answer;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Question;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Survey;
import com.mfdogalindo.feedbacksurvey.domain.models.enums.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @BeforeEach
    void setUp() {
        this.answerService = new AnswerServiceImpl(this.answerRepository, this.questionRepository, this.surveyRepository);
    }

    @Test
    void GetListAnswersHasResults() {
        List<Answer> answerList = new ArrayList<>();

        answerList.add(Answer.builder()
                .id(1L)
                .email("user@example.com")
                .question(Question.builder()
                        .id(1L)
                        .question("¿Te gusta el café?")
                        .type(QuestionType.CHECKBOX)
                        .options(List.of("Si", "No"))
                        .build())
                .answers(List.of("Si"))
                .build());

        when(answerRepository.findAll()).thenReturn(answerList);

        List<Answer> result = answerService.findAll();
        assert result.size() == answerList.size();
        assert Objects.equals(result.get(0).getId(), answerList.get(0).getId());
    }

    @Test
    void GetListAnswersHasNoResults() {
        List<Answer> answerList = new ArrayList<>();

        when(answerRepository.findAll()).thenReturn(answerList);

        List<Answer> result = answerService.findAll();
        assert result.size() == answerList.size();
    }

    @Test
    void GetListAnswersByEmailHasResults() {
        List<Answer> answerList = new ArrayList<>();

        answerList.add(Answer.builder()
                .id(1L)
                .email("other@example.com")
                .question(Question.builder()
                        .id(1L)
                        .question("¿Te gusta el té?")
                        .type(QuestionType.CHECKBOX)
                        .options(List.of("Si", "No"))
                        .build())
                .answers(List.of("Si"))
                .build());

        when(answerRepository.findByEmail(anyString())).thenReturn(answerList);

        List<Answer> result = answerService.findByEmail("other@example.com");
        assert result.size() == answerList.size();
        assert Objects.equals(result.get(0).getId(), answerList.get(0).getId());
    }

    @Test
    void GetListAnswersByEmailHasNoResults() {
        List<Answer> answerList = new ArrayList<>();

        when(answerRepository.findByEmail(anyString())).thenReturn(answerList);

        assertThrows(NoResultsException.class, () -> {
            answerService.findByEmail("other@mail.com");
        });
    }

    @Test
    void SaveAnswersHasResults() {
        Question question = Question.builder()
                .id(1L)
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build();

        Survey survey = Survey.builder()
                .id(1L)
                .name("Encuesta de café")
                .questions(List.of(question))
                .build();

        CreateAnswersCommand answersCmd = CreateAnswersCommand.builder()
                .email("user@mail.com")
                .surveyId(1L)
                .questionAnswers(List.of(
                        QuestionAnswer.builder().
                                questionId(1L)
                                .answers(List.of("Si"))
                                .build()
                ))
                .build();

        when(surveyRepository.findByIdAndEnabledTrue(any(Long.class))).thenReturn(Optional.ofNullable(survey));
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        when(answerRepository.save(any(Answer.class))).thenReturn(Answer.builder().build());

        long insertedElements = answerService.save(answersCmd);

        assert insertedElements == answersCmd.getQuestionAnswers().size();
    }

    @Test
    void SaveAnswersWhenSurveyDoesNotExist() {
        CreateAnswersCommand answersCmd = CreateAnswersCommand.builder()
                .email("user@mail.com")
                .surveyId(1L)
                .questionAnswers(List.of(
                        QuestionAnswer.builder().
                                questionId(1L)
                                .answers(List.of("Si"))
                                .build()
                ))
                .build();

        when(surveyRepository.findByIdAndEnabledTrue(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(SurveyNotFoundException.class, () -> {
            answerService.save(answersCmd);
        });
    }

    @Test
    void SaveAnswersWhenQuestionDoesNotExist() {
        Question question = Question.builder()
                .id(1L)
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of("Si", "No"))
                .build();

        Survey survey = Survey.builder()
                .id(1L)
                .name("Encuesta de café")
                .questions(List.of(question))
                .build();

        CreateAnswersCommand answersCmd = CreateAnswersCommand.builder()
                .email("user@mail.com")
                .surveyId(1L)
                .questionAnswers(List.of(
                        QuestionAnswer.builder().
                                questionId(1L)
                                .answers(List.of("Si"))
                                .build()
                ))
                .build();

        when(surveyRepository.findByIdAndEnabledTrue(any(Long.class))).thenReturn(Optional.ofNullable(survey));
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> {
            answerService.save(answersCmd);
        });
    }

    @Test
    void SaveAnswersWhenQuestionHasNoValidOptions() {
        Question question = Question.builder()
                .id(1L)
                .question("¿Te gusta el café?")
                .type(QuestionType.CHECKBOX)
                .options(List.of())
                .build();

        Survey survey = Survey.builder()
                .id(1L)
                .name("Encuesta de café")
                .questions(List.of(question))
                .build();

        CreateAnswersCommand answersCmd = CreateAnswersCommand.builder()
                .email("user@mail.com")
                .surveyId(1L)
                .questionAnswers(List.of(
                        QuestionAnswer.builder().
                                questionId(1L)
                                .answers(List.of("Pan"))
                                .build()
                ))
                .build();

        when(surveyRepository.findByIdAndEnabledTrue(any(Long.class))).thenReturn(Optional.ofNullable(survey));
        when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));

        assertThrows(AnswerNotValidException.class, () -> {
            answerService.save(answersCmd);
        });
    }

}