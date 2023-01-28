package com.mfdogalindo.feedbacksurvey.adapters.repository;

import com.mfdogalindo.feedbacksurvey.domain.models.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
