package com.example.footballmanager.dto.player;

import lombok.Data;

@Data
public class PlayerDto {
    private String name;
    private int age;
    private int experience;
    private Long teamId;
}
