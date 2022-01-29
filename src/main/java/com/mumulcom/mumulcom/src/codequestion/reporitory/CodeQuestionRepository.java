package com.mumulcom.mumulcom.src.codequestion.reporitory;

import com.mumulcom.mumulcom.src.codequestion.domain.CodeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeQuestionRepository extends JpaRepository<CodeQuestion, Long> {

}
