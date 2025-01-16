package com.example.footballmanager.service;

import com.example.footballmanager.dto.player.PlayerDto;
import com.example.footballmanager.dto.player.UpdatePlayerDto;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.mapper.player.PlayerMapper;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.player.PlayerRepository;
import com.example.footballmanager.service.player.PlayerServiceImpl;
import com.example.footballmanager.service.team.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    @InjectMocks
    private PlayerServiceImpl playerService;
    @Mock
    private TeamServiceImpl teamService;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerMapper playerMapper;


    @Test
    public void findById_WithNonExistingPlayerId_ShouldThrowException() {
        // Given
        Long playerId = 100L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> playerService.findById(playerId));

        // Then
        String expected = "Can't find player by id " + playerId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(playerRepository, times(1)).findById(playerId);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    public void updateById_WithNonExistingPlayerId_ShouldThrowException() {
        // Given
        Long playerId = 100L;
        UpdatePlayerDto updatePlayerDto = new UpdatePlayerDto();
        updatePlayerDto.setAge(25);
        updatePlayerDto.setName("Vasyl Sych");
        updatePlayerDto.setExperience(36);

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> playerService.updateById(playerId, updatePlayerDto));

        // Then
        String expected = "Can't find player by id " + playerId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(playerRepository, times(1)).findById(playerId);
        verifyNoMoreInteractions(playerRepository);
    }

    @Test
    public void transferPlayer_WithNonExistingPlayerId_ShouldThrowException() {
        // Given
        Long playerId = 100L;
        Long newTeamId = 2L;
        int commission = 2;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> playerService.transferPlayer(playerId, newTeamId, commission));

        // Then
        String expected = "Can't find player by id " + playerId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(playerRepository, times(1)).findById(playerId);
        verifyNoMoreInteractions(playerRepository, teamService);
    }

    @Test
    public void transferPlayer_WithValidData_ShouldTransferPlayer() {
        // Given
        Long playerId = 1L;
        Long newTeamId = 2L;
        int commission = 2;

        Player player = new Player();
        player.setId(playerId);
        player.setAge(22);
        player.setExperience(30);
        player.setName("Vasyl Sych");

        Team oldTeam = new Team();
        oldTeam.setId(1L);
        oldTeam.setName("Veres");
        oldTeam.setAccountBalance(BigDecimal.valueOf(1000000));

        player.setTeam(oldTeam);

        Team newTeam = new Team();
        newTeam.setId(newTeamId);
        newTeam.setName("Rukh");
        newTeam.setAccountBalance(BigDecimal.valueOf(2000000));

        PlayerDto expectedPlayerDto = new PlayerDto();
        expectedPlayerDto.setName("Vasyl Sych");
        expectedPlayerDto.setTeamId(newTeamId);

        double finalCost = (30 * 100000.0 / 22) * (1 + commission / 100.0);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(teamService.transferMoney(oldTeam.getId(), newTeamId, finalCost)).thenReturn(newTeam);
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(playerMapper.toDto(any(Player.class))).thenReturn(expectedPlayerDto);

        // When
        PlayerDto result = playerService.transferPlayer(playerId, newTeamId, commission);

        // Then
        assertNotNull(result);
        assertEquals(newTeamId, result.getTeamId());
        verify(playerRepository, times(1)).findById(playerId);
        verify(teamService, times(1)).transferMoney(oldTeam.getId(), newTeamId, finalCost);
        verify(playerRepository, times(1)).save(any(Player.class));
        verifyNoMoreInteractions(playerRepository, teamService);
    }

}
