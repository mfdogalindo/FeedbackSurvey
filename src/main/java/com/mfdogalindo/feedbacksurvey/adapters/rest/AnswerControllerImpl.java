package com.mfdogalindo.feedbacksurvey.adapters.rest;

import com.mfdogalindo.feedbacksurvey.domain.models.commands.CreateAnswersCommand;
import com.mfdogalindo.feedbacksurvey.domain.models.entities.Answer;
import com.mfdogalindo.feedbacksurvey.domain.services.AnswerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/answers")
public class AnswerControllerImpl implements AnswerController {

    private final AnswerService answerService;

    AnswerControllerImpl(AnswerService answerService) {
        this.answerService = answerService;
    }

    @Override
    @PostMapping(produces = "application/json", consumes = "application/json")
    public int save(@RequestBody CreateAnswersCommand answersCmd) {
        return this.answerService.save(answersCmd);
    }

    @Override
    @GetMapping(value = "/{email}", produces = "application/json")
    public List<Answer> findByEmail(@PathVariable String email) {
        return this.answerService.findByEmail(email);
    }

    @Override
    @GetMapping(produces = "application/json")
    public List<Answer> findAll() {
        return this.answerService.findAll();
    }
}
