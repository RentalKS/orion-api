package com.orion.dto.rates;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatesDto {
    private Long id;
    private String name;
    private Double dailyRate;
    private Double weeklyRate;
    private Double monthlyRate;
}
