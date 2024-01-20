package com.mehdi.databaseJpa.domain.dto;

import com.mehdi.databaseJpa.domain.entities.AuthorEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    public String isbn;

    public String title;

    public AuthorEntity author;
}
