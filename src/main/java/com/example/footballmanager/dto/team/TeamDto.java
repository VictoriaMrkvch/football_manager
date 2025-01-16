package com.example.footballmanager.dto.team;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TeamDto {
    private String name;
    private BigDecimal accountBalance;
}
