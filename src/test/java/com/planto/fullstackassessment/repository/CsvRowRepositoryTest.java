package com.planto.fullstackassessment.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CsvRowRepositoryTest {
    @Autowired
    private CsvRowRepository csvRowRepository;

    @Autowired
    private CsvRepository csvRepository;

    @Test
    @DisplayName("it should return a list of csv row by its parent id")
    public void whenFindByParentId_thenReturnCsvList() {
        CsvEntity csvEntity = new CsvEntity();
        csvEntity.setFilename("example.csv");
        csvRepository.save(csvEntity);

        for (int i = 1; i <= 5; i++) {
            CsvRowEntity csvRowEntity = new CsvRowEntity();
            csvRowEntity.setParent(csvEntity);
            csvRowRepository.save(csvRowEntity);
        }

        Page<CsvRowEntity> csvRowEntities = csvRowRepository.findByParentId(csvEntity.getId(), PageRequest.of(0, 5));

        for (CsvRowEntity csvRowEntity : csvRowEntities.getContent()) {
            assertEquals(csvEntity.getId(), csvRowEntity.getParent().getId());
        }
    }

    @Test
    @DisplayName("it should return a list of csv row by its parent id")
    public void givenPagination_whenFindByParentId_thenReturnPaginatedCsvList() {
        CsvEntity csvEntity = new CsvEntity();
        csvEntity.setFilename("example.csv");
        csvRepository.save(csvEntity);

        int pageSize = 3;

        for (int i = 1; i <= 5; i++) {
            CsvRowEntity csvRowEntity = new CsvRowEntity();
            csvRowEntity.setParent(csvEntity);
            csvRowRepository.save(csvRowEntity);
        }

        Page<CsvRowEntity> csvRowEntites = csvRowRepository.findByParentId(csvEntity.getId(),
                PageRequest.of(0, pageSize));

        assertEquals(pageSize, csvRowEntites.getContent().size());
    }
}
