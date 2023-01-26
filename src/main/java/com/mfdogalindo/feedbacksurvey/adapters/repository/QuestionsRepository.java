package com.mfdogalindo.feedbacksurvey.adapters.repository;

import com.mfdogalindo.feedbacksurvey.domain.models.entities.Questions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionsRepository extends JpaRepository<Questions, Long> {
}
