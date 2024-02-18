package com.planto.fullstackassessment.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "csv_row")
@Getter
@Setter
@NoArgsConstructor
public class CsvRowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csv_content_id_seq")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "csv_id", nullable = false)
    private CsvEntity parent;

    @Column
    private String column0;

    @Column
    private String column1;

    @Column
    private String column2;

    @Column
    private String column3;

    @Column
    private String column4;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
