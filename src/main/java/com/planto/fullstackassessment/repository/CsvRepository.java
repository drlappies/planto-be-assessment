package com.planto.fullstackassessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.planto.fullstackassessment.model.CsvEntity;

@Repository
public interface CsvRepository extends JpaRepository<CsvEntity, Long> {
    CsvEntity findByFilename(String filename);

    Page<CsvEntity> findAll(Pageable pageable);
}