package com.planto.fullstackassessment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.planto.fullstackassessment.model.CsvRowEntity;

@Repository
public interface CsvRowRepository extends JpaRepository<CsvRowEntity, Long> {
    Page<CsvRowEntity> findByParentId(Long csvId, Pageable pageable);
}
