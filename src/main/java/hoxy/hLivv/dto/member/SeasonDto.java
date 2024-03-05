package hoxy.hLivv.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeasonDto {

    private Season season;

    public static SeasonDto from(Season season) {
        return new SeasonDto(season);
    }
}
