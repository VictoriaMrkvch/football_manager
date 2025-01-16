package com.example.footballmanager.service.player;

import com.example.footballmanager.dto.player.CreatePlayerDto;
import com.example.footballmanager.dto.player.PlayerDto;
import com.example.footballmanager.dto.player.UpdatePlayerDto;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.mapper.player.PlayerMapper;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.player.PlayerRepository;
import com.example.footballmanager.service.team.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final TeamService teamService;

    @Override
    public PlayerDto save(CreatePlayerDto createPlayerDto) {
        Player player = playerMapper.toModel(createPlayerDto);
        return playerMapper.toDto(playerRepository.save(player));
    }

    @Override
    public List<PlayerDto> findAll() {
        return playerRepository.findAll()
                .stream()
                .map(playerMapper::toDto)
                .toList();
    }

    @Override
    public List<PlayerDto> findAllByTeamId(Long id) {
        teamService.findById(id);
        return playerRepository.findAllByTeamId(id)
                .stream()
                .map(playerMapper::toDto)
                .toList();
    }

    @Override
    public PlayerDto findById(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find player by id " + id));
        return playerMapper.toDto(player);
    }

    @Override
    public PlayerDto updateById(Long id, UpdatePlayerDto updatePlayerDto) {
        Player player = playerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find player by id " + id));
        Player updatedPlayer = new Player();
        updatedPlayer.setAge(updatePlayerDto.getAge());
        updatedPlayer.setName(updatePlayerDto.getName());
        updatedPlayer.setExperience(updatePlayerDto.getExperience());
        updatedPlayer.setTeam(player.getTeam());
        updatedPlayer.setId(id);
        return playerMapper.toDto(playerRepository.save(updatedPlayer));
    }

    @Override
    @Transactional
    public PlayerDto transferPlayer(Long id, Long newTeamId, int commission) {
        Player player = playerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't find player by id " + id));
        double finalCost = calculateTheFinalCost(player, commission);
        Team team = teamService.transferMoney(player.getTeam().getId(), newTeamId, finalCost);
        player.setTeam(team);
        return playerMapper.toDto(playerRepository.save(player));
    }

    @Override
    public void deleteById(Long id) {
        playerRepository.deleteById(id);
    }

    private double calculateTheFinalCost(Player player, int commission) {
        double transferCost = (double) (player.getExperience() * 100000) / player.getAge();
        double commissionCost = transferCost * ((double) commission / 100);
        return transferCost + commissionCost;
    }
}
