package com.planto.fullstackassessment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.planto.fullstackassessment.dtos.CsvRowDto;
import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;
import com.planto.fullstackassessment.service.CsvService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/csv")
public class CsvController {
    private final CsvService csvService;

    @PostMapping("/upload")
    public CsvEntity uploadCsvFile(@RequestParam("file") MultipartFile file) {
        return this.csvService.uploadCsvFile(file);
    }

    @GetMapping()
    public Page<CsvEntity> getCsvs(@RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int limit) {
        return csvService.getCsvs(PageRequest.of(offset, limit));
    }

    @GetMapping("/{id}")
    public Map<String, Object> getCsvById(@PathVariable Long id, @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "100") int limit) {
        return csvService.getCsvRowsByCsvId(id, PageRequest.of(offset, limit));
    }

    @PostMapping("/{id}")
    public List<CsvRowEntity> patchCsvById(@PathVariable Long id, @RequestBody List<CsvRowDto> rows) {
        return csvService.patchCsvById(id, rows);
    }
}
