package com.suma.carepoint.models.utility;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {
    private List<?> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static PageResponse buildPageResponse(Page<?> pageable, List<?> content) {
        return PageResponse.builder()
                .content(content)
                .page(pageable.getNumber())
                .size(pageable.getSize())
                .totalElements(pageable.getTotalElements())
                .totalPages(pageable.getTotalPages())
                .first(pageable.isFirst())
                .last(pageable.isLast())
                .build();
    }

    public static void validatePagination(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }

}
