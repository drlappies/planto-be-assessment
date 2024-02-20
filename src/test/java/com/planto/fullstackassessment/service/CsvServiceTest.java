package com.planto.fullstackassessment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;
import com.planto.fullstackassessment.repository.CsvRepository;
import com.planto.fullstackassessment.repository.CsvRowRepository;

@ExtendWith(MockitoExtension.class)
public class CsvServiceTest {

    @Mock
    private CsvRepository csvRepository;

    @Mock
    private CsvRowRepository csvRowRepository;

    @InjectMocks
    private CsvService csvService;

    @Test
    public void givenCsvFile_whenUploadCsvFile_thenReturnCsvEntity() {
        String filename = "test.csv";
        MockMultipartFile file = new MockMultipartFile("file", filename, "text/csv",
                "column0,column1,column2,column3,column4\nvalue0,value1,value2,value3,value4\n".getBytes());

        CsvEntity result = csvService.uploadCsvFile(file);

        assertNotNull(result);
        assertEquals(filename, result.getFilename());
        verify(csvRepository, times(1)).save(any(CsvEntity.class));
        verify(csvRowRepository, atLeastOnce()).save(any(CsvRowEntity.class));
    }

    @Test
    public void givenNonCsvFile_whenUploadCsvFile_thenThrowBadRequestException() {
        String filename = "test.txt";
        MockMultipartFile file = new MockMultipartFile("file", filename, "text/plain", new byte[0]);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> csvService.uploadCsvFile(file));

        assertEquals("File is not CSV", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void givenInvalidCsvId_whenGetCsvRowsByCsvId_thenThrowNotFoundException() {
        // Arrange
        Long csvId = 2L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

        when(csvRepository.findById(csvId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> csvService.getCsvRowsByCsvId(csvId, pageable));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(csvRepository, times(1)).findById(csvId);
    }
}