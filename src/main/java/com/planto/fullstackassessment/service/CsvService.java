package com.planto.fullstackassessment.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.planto.fullstackassessment.dtos.CsvRowDto;
import com.planto.fullstackassessment.model.CsvEntity;
import com.planto.fullstackassessment.model.CsvRowEntity;
import com.planto.fullstackassessment.repository.CsvRepository;
import com.planto.fullstackassessment.repository.CsvRowRepository;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvService {
    private final CsvRepository csvRepository;
    private final CsvRowRepository csvRowRepository;
    private static final int BATCH_SIZE = 1000;

    public CsvEntity uploadCsvFile(MultipartFile file) {
        String filename = file.getOriginalFilename();

        CsvEntity csvEntity = new CsvEntity();
        csvEntity.setFilename(filename);
        csvRepository.save(csvEntity);
        csvRepository.flush();

        try {
            InputStream inputStream = file.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));

            String row;
            int batchCount = 0;

            while ((row = bufferedReader.readLine()) != null) {
                String[] columns = row.split(",");

                CsvRowEntity csvRowEntity = new CsvRowEntity();
                csvRowEntity.setParent(csvEntity);
                csvRowEntity.setColumn0(columns[0]);
                csvRowEntity.setColumn1(columns[1]);
                csvRowEntity.setColumn2(columns[2]);
                csvRowEntity.setColumn3(columns[3]);
                csvRowEntity.setColumn4(columns[4]);

                csvRowRepository.save(csvRowEntity);
                batchCount++;

                if (batchCount % BATCH_SIZE == 0) {
                    csvRowRepository.flush();
                }
            }

            bufferedReader.close();

            return csvEntity;
        } catch (IOException ioException) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ioException.getMessage());
        }
    }

    public Map<String, Object> getCsvRowsByCsvId(Long csvId, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        Optional<CsvEntity> csvEntity = csvRepository.findById(csvId);

        if (!csvEntity.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page<CsvRowEntity> csvRowEntity = csvRowRepository.findByParentId(csvId,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));

        response.put("csv", csvEntity);
        response.put("rows", csvRowEntity);

        return response;
    }

    public Page<CsvEntity> getCsvs(Pageable pageable) {
        return csvRepository.findAll(pageable);
    }

    public List<CsvRowEntity> patchCsvById(Long id, List<CsvRowDto> csvRowDtos) {
        List<Long> rowIds = csvRowDtos.stream().map(CsvRowDto::getId).collect(Collectors.toList());
        List<CsvRowEntity> currentRows = csvRowRepository.findAllById(rowIds);
        Map<Long, CsvRowEntity> map = new HashMap<>();

        for (CsvRowEntity row : currentRows) {
            map.put(row.getId(), row);
        }

        for (CsvRowDto csvRowDto : csvRowDtos) {
            CsvRowEntity csvRowEntity = map.get(csvRowDto.getId());

            if (csvRowEntity != null) {
                csvRowEntity.setColumn0(csvRowDto.getColumn0());
                csvRowEntity.setColumn1(csvRowDto.getColumn1());
                csvRowEntity.setColumn2(csvRowDto.getColumn2());
                csvRowEntity.setColumn3(csvRowDto.getColumn3());
                csvRowEntity.setColumn4(csvRowDto.getColumn4());
                csvRowEntity.setUpdatedAt(LocalDateTime.now());

                csvRowRepository.save(csvRowEntity);
            }
        }

        csvRowRepository.flush();

        return currentRows;
    }
}