package com.example.footballmanager.dto.team;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateTeamDto {
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal accountBalance;
}
