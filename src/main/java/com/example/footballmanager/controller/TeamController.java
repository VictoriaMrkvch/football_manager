package com.example.footballmanager.controller;

import com.example.footballmanager.dto.team.CreateTeamDto;
import com.example.footballmanager.dto.team.TeamDto;
import com.example.footballmanager.service.team.TeamService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public List<TeamDto> findAll() {
        return teamService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamDto save(@RequestBody @Valid CreateTeamDto createTeamDto) {
        return teamService.save(createTeamDto);
    }

    @GetMapping("/{id}")
    public TeamDto findById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @PutMapping("/{id}")
    public TeamDto updateById(@PathVariable Long id,
                              @RequestBody @Valid CreateTeamDto createTeamDto) {
        return teamService.updateById(id, createTeamDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        teamService.deleteById(id);
    }
}
