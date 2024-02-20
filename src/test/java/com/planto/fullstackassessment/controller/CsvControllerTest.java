package com.planto.fullstackassessment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planto.fullstackassessment.dtos.CsvRowDto;
import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;
import com.planto.fullstackassessment.service.CsvService;

@WebMvcTest(CsvController.class)
public class CsvControllerTest {
    @MockBean
    private CsvService csvService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenMultipartCsvFile_whenUploadCsvFile_thenReturnCsvEntity() throws JsonProcessingException, Exception {
        CsvEntity mockCsvEntity = new CsvEntity();
        when(csvService.uploadCsvFile(any())).thenReturn(mockCsvEntity);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/plain",
                "Mock CSV content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/csv/upload")
                .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(mockCsvEntity)));
    }

    @Test
    void givenOffsetAndLimit_whenGetCsvs_thenReturnPageOfCsvEntities() throws Exception {
        CsvEntity mockCsvEntity = new CsvEntity();
        Page<CsvEntity> mockCsvPage = new PageImpl<>(Collections.singletonList(mockCsvEntity));

        when(csvService.getCsvs(any())).thenReturn(mockCsvPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/csv")
                .param("offset", "0")
                .param("limit", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(mockCsvPage)));
    }

    @Test
    void givenCsvIdOffsetAndLimit_whenGetCsvById_thenReturnMapOfCsvRows() throws Exception {
        CsvEntity mockCsvEntity = new CsvEntity();
        Map<String, Object> mockCsvRows = Collections.singletonMap("csvRows", Collections.singletonList(mockCsvEntity));
        when(csvService.getCsvRowsByCsvId(any(), any())).thenReturn(mockCsvRows);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/csv/{id}", 1L)
                .param("offset", "0")
                .param("limit", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(mockCsvRows)));
    }

    @Test
    void givenCsvIdAndRows_whenPatchCsvById_thenReturnListCsvRowEntity() throws Exception {
        CsvRowEntity mockCsvRowEntity = new CsvRowEntity();
        List<CsvRowEntity> mockCsvRowEntities = Collections.singletonList(mockCsvRowEntity);
        when(csvService.patchCsvById(any(), any())).thenReturn(mockCsvRowEntities);

        List<CsvRowDto> requestDtoList = Collections
                .singletonList(
                        new CsvRowDto(1L, "columnName", "test col 1 ", "test col 2", "test col 3", "test col 4"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/csv/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDtoList)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(mockCsvRowEntities)));
    }

}
