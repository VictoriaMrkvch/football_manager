package com.example.footballmanager.service.player;

import com.example.footballmanager.dto.player.CreatePlayerDto;
import com.example.footballmanager.dto.player.PlayerDto;
import com.example.footballmanager.dto.player.UpdatePlayerDto;
import java.util.List;

public interface PlayerService {
    PlayerDto save(CreatePlayerDto createPlayerDto);

    List<PlayerDto> findAll();

    List<PlayerDto> findAllByTeamId(Long id);

    PlayerDto findById(Long id);

    PlayerDto updateById(Long id, UpdatePlayerDto updatePlayerDto);

    PlayerDto transferPlayer(Long id, Long newTeamId, int commission);

    void deleteById(Long id);
}
