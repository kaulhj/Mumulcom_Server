package com.mumulcom.mumulcom.src.mycategory.repository;

import com.mumulcom.mumulcom.src.mycategory.domain.MyCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyCategoryRepository extends JpaRepository<MyCategory, Long> {

    List<MyCategory> findByUserIdx(Long userIdx);
}
