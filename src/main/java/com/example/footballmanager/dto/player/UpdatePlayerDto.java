package com.example.footballmanager.dto.player;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePlayerDto {
    @NotBlank
    private String name;
    @NotNull
    @Min(18)
    private int age;
    @NotNull
    @Min(0)
    private int experience;
}
