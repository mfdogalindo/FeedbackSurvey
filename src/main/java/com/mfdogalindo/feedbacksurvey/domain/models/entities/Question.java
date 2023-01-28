package com.mfdogalindo.feedbacksurvey.domain.models.entities;

import com.mfdogalindo.feedbacksurvey.domain.models.enums.QuestionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private QuestionType type = QuestionType.CHECKBOX;

    @Column(nullable = false)
    private List<String> options = List.of("Si", "No");


}
