package com.orion.dto.rates;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class RatesDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Double dailyRate;
    @NotNull
    private Double weeklyRate;
    @NotNull
    private Double monthlyRate;

    public RatesDto(Long id, String name, Double dailyRate, Double weeklyRate, Double monthlyRate){
        this.id = id;
        this.name = name;
        this.dailyRate = dailyRate;
        this.weeklyRate = weeklyRate;
        this.monthlyRate = monthlyRate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()){
            return;
        }
        this.name = name;
    }

    public void setDailyRate(Double dailyRate) {
        if(dailyRate == null){
            return;
        }
        this.dailyRate = dailyRate;
    }

    public void setWeeklyRate(Double weeklyRate) {
        if(weeklyRate == null){
            return;
        }
        this.weeklyRate = weeklyRate;
    }

    public void setMonthlyRate(Double monthlyRate) {
        if(monthlyRate == null){
            return;
        }
        this.monthlyRate = monthlyRate;
    }
}
