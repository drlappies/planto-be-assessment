package com.planto.fullstackassessment.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "csv")
@Getter
@Setter
@NoArgsConstructor
public class CsvEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "csv_id_seq")
    private Long id;

    @Column(nullable = false)
    private String filename;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<CsvRowEntity> rows;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
