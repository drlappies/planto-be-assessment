package com.planto.fullstackassessment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.planto.fullstackassessment.dtos.CsvRowDto;
import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;
import com.planto.fullstackassessment.repository.CsvRepository;
import com.planto.fullstackassessment.repository.CsvRowRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CsvServiceTest {

    @Mock
    private CsvRepository csvRepository;

    @Mock
    private CsvRowRepository csvRowRepository;

    @InjectMocks
    private CsvService csvService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadCsvFile() throws IOException {
        // Arrange
        String filename = "example.csv";
        MockMultipartFile file = new MockMultipartFile("file", filename, "text/csv", "test,csv,data".getBytes());

        CsvEntity existingCsvEntity = new CsvEntity();
        existingCsvEntity.setId(1L);

        when(csvRepository.findByFilename(eq(filename))).thenReturn(existingCsvEntity);
        when(csvRepository.save(any())).thenReturn(new CsvEntity());
        when(csvRowRepository.save(any())).thenReturn(new CsvRowEntity());

        // Act
        csvService.uploadCsvFile(file);

        // Assert
        verify(csvRepository, times(1)).findByFilename(eq(filename));
        verify(csvRepository, times(1)).save(any());
        verify(csvRowRepository, atLeastOnce()).save(any());
    }

    @Test
    void testUploadCsvFileDuplicateFilename() throws IOException {
        // Arrange
        String filename = "example.csv";
        MockMultipartFile file = new MockMultipartFile("file", filename, "text/csv", "test,csv,data".getBytes());

        when(csvRepository.findByFilename(eq(filename))).thenReturn(new CsvEntity());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> csvService.uploadCsvFile(file));
    }

    @Test
    void testGetCsvRowsByCsvId() {
        // Arrange
        Long csvId = 1L;
        PageRequest pageable = PageRequest.of(0, 5);
        Page<CsvRowEntity> expectedPage = mock(Page.class);

        when(csvRowRepository.findByParentId(eq(csvId), eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<CsvRowEntity> result = csvService.getCsvRowsByCsvId(csvId, pageable);

        // Assert
        assertEquals(expectedPage, result);
    }

    @Test
    void testGetCsvs() {
        // Arrange
        PageRequest pageable = PageRequest.of(0, 5);
        Page<CsvEntity> expectedPage = mock(Page.class);

        when(csvRepository.findAll(eq(pageable))).thenReturn(expectedPage);

        // Act
        Page<CsvEntity> result = csvService.getCsvs(pageable);

        // Assert
        assertEquals(expectedPage, result);
    }

    @Test
    void testPatchCsvById() {
        // Arrange
        Long csvId = 1L;
        CsvRowDto csvRowDto = new CsvRowDto();
        csvRowDto.setId(1L);
        List<CsvRowDto> csvRowDtos = List.of(csvRowDto);

        CsvRowEntity existingCsvRowEntity = new CsvRowEntity();
        existingCsvRowEntity.setId(1L);

        when(csvRowRepository.findAllById(any())).thenReturn(List.of(existingCsvRowEntity));
        when(csvRowRepository.save(any())).thenReturn(new CsvRowEntity());

        // Act
        List<CsvRowEntity> result = csvService.patchCsvById(csvId, csvRowDtos);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(csvRowRepository, times(1)).save(any());
    }
}
