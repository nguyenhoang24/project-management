package utc.edu.thesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private Long id;
    private String valueSearch;
    private String conditionSearch;

    public SearchDto(String valueSearch, String conditionSearch) {
        this.valueSearch = valueSearch;
        this.conditionSearch = conditionSearch;
    }
}
