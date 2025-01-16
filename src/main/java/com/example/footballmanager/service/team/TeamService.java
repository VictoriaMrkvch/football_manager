package com.example.footballmanager.service.team;

import com.example.footballmanager.dto.team.CreateTeamDto;
import com.example.footballmanager.dto.team.TeamDto;
import com.example.footballmanager.model.Team;
import java.util.List;

public interface TeamService {
    TeamDto save(CreateTeamDto createTeamDto);

    List<TeamDto> findAll();

    TeamDto findById(Long id);

    TeamDto updateById(Long id, CreateTeamDto createTeamDto);

    Team transferMoney(Long oldTeamId, Long newTeamId, double finalTransferCost);

    void deleteById(Long id);
}
