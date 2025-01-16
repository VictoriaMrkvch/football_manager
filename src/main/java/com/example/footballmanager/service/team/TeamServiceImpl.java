package com.example.footballmanager.service.team;

import com.example.footballmanager.dto.team.CreateTeamDto;
import com.example.footballmanager.dto.team.TeamDto;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.exception.NotEnoughFundsException;
import com.example.footballmanager.mapper.team.TeamMapper;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.team.TeamRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public TeamDto save(CreateTeamDto createTeamDto) {
        Team team = teamMapper.toModel(createTeamDto);
        return teamMapper.toDto(teamRepository.save(team));
    }

    @Override
    public List<TeamDto> findAll() {
        return teamRepository.findAll()
                .stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @Override
    public TeamDto findById(Long id) {
        Team team = teamRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find team by id " + id));
        return teamMapper.toDto(team);
    }

    @Override
    public TeamDto updateById(Long id, CreateTeamDto createTeamDto) {
        teamRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find team by id " + id));
        Team updatedTeam = teamMapper.toModel(createTeamDto);
        updatedTeam.setId(id);
        return teamMapper.toDto(teamRepository.save(updatedTeam));
    }

    @Override
    @Transactional
    public Team transferMoney(Long oldTeamId, Long newTeamId, double finalTransferCost) {
        Team oldTeam = teamRepository.findById(oldTeamId).orElseThrow(() ->
                new EntityNotFoundException("Can't find team by id " + oldTeamId));
        Team newTeam = teamRepository.findById(newTeamId).orElseThrow(() ->
                new EntityNotFoundException("Can't find team by id " + newTeamId));
        if (newTeam.getAccountBalance().compareTo(BigDecimal.valueOf(finalTransferCost)) < 0) {
            // Баланс нової команди менший за суму трансферу
            throw new NotEnoughFundsException("Not enough funds in the team's balance");
        }
        newTeam.setAccountBalance(newTeam.getAccountBalance()
                .subtract(BigDecimal.valueOf(finalTransferCost)));
        oldTeam.setAccountBalance(oldTeam.getAccountBalance()
                .add(BigDecimal.valueOf(finalTransferCost)));
        teamRepository.save(oldTeam);
        return teamRepository.save(newTeam);
    }

    @Override
    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }
}
