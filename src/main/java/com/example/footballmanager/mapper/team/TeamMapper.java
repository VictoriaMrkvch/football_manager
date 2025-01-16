package com.example.footballmanager.mapper.team;

import com.example.footballmanager.config.MapperConfig;
import com.example.footballmanager.dto.team.CreateTeamDto;
import com.example.footballmanager.dto.team.TeamDto;
import com.example.footballmanager.model.Team;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface TeamMapper {
    TeamDto toDto(Team team);

    Team toModel(CreateTeamDto createTeamDto);
}
