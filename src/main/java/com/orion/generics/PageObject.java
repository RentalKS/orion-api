package com.orion.generics;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.orion.dto.file.FileData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageObject<T> {

    private Integer page;

    private Integer size;

    private Integer totalPages;

    private Long totalSize;

    private Long date;

    private List<T> data;

    private T details;

    private List<FileData> files;

}
