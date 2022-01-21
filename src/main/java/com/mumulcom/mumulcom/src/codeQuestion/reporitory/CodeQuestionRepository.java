package com.mumulcom.mumulcom.src.codeQuestion.reporitory;

import com.mumulcom.mumulcom.src.codeQuestion.domain.CodeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeQuestionRepository extends JpaRepository<CodeQuestion, Long> {

}
