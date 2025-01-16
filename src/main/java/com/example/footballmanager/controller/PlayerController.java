package com.example.footballmanager.controller;

import com.example.footballmanager.dto.player.CreatePlayerDto;
import com.example.footballmanager.dto.player.PlayerDto;
import com.example.footballmanager.dto.player.UpdatePlayerDto;
import com.example.footballmanager.service.player.PlayerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    @GetMapping
    public List<PlayerDto> findAll() {
        return playerService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerDto save(@RequestBody @Valid CreatePlayerDto createPlayerDto) {
        return playerService.save(createPlayerDto);
    }

    @GetMapping("/team/{id}")
    public List<PlayerDto> findAllByTeamId(@PathVariable Long id) {
        return playerService.findAllByTeamId(id);
    }

    @GetMapping("/{id}")
    public PlayerDto findById(@PathVariable Long id) {
        return playerService.findById(id);
    }

    @PatchMapping("/{id}")
    public PlayerDto transferPlayer(@PathVariable Long id,
                                    @RequestParam("newTeamId") Long newTeamId,
                                    @RequestParam("commission") int commission) {
        return playerService.transferPlayer(id, newTeamId, commission);
    }

    @PutMapping("/{id}")
    public PlayerDto updateById(@PathVariable Long id,
                                @RequestBody @Valid UpdatePlayerDto updatePlayerDto) {
        return playerService.updateById(id, updatePlayerDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        playerService.deleteById(id);
    }
}
