package com.planto.fullstackassessment.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CsvRowDto {
    private Long id;
    private String column0;
    private String column1;
    private String column2;
    private String column3;
    private String column4;
}
