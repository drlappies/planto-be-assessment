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

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CsvRepositoryTest {
    @Autowired
    private CsvRepository csvRepository;

    @Test
    @DisplayName("it should return a csv by filename")
    public void givenFilename_whenFindByFilename_thenReturnCsvByFilename() {
        CsvEntity csvEntity = new CsvEntity();
        csvEntity.setFilename("example.csv");
        csvRepository.save(csvEntity);
        CsvEntity csv = csvRepository.findByFilename("example.csv");

        assertEquals(csvEntity, csv);
    }

    @Test
    @DisplayName("it should return a list of csv by pagination")
    public void givePagination_whenFindByFilename_thenReturnPaginatedCsvList() {
        for (int i = 1; i <= 5; i++) {
            CsvEntity csvEntity = new CsvEntity();
            csvEntity.setFilename("dummy" + i + ".csv");
            // Set other properties as needed
            csvRepository.save(csvEntity);
        }

        int pageSize = 2;

        Page<CsvEntity> csvEntities = csvRepository.findAll(PageRequest.of(0, pageSize));
        assertEquals(pageSize, csvEntities.getContent().size());
    }

}
