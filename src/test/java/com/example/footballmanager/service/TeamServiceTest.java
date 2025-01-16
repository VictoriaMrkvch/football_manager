package com.example.footballmanager.service;

import com.example.footballmanager.dto.team.CreateTeamDto;
import com.example.footballmanager.exception.EntityNotFoundException;
import com.example.footballmanager.exception.NotEnoughFundsException;
import com.example.footballmanager.mapper.team.TeamMapper;
import com.example.footballmanager.model.Team;
import com.example.footballmanager.repository.team.TeamRepository;
import com.example.footballmanager.service.team.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    @InjectMocks
    private TeamServiceImpl teamService;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private TeamMapper teamMapper;

    @Test
    public void findById_WithNonExistingTeamId_ShouldThrowException() {
        // Given
        Long playerId = 100L;

        when(teamRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.findById(playerId));

        // Then
        String expected = "Can't find team by id " + playerId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(teamRepository, times(1)).findById(playerId);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void updateById_WithNonExistingTeamId_ShouldThrowException() {
        // Given
        Long playerId = 100L;
        CreateTeamDto updateTeamDto = new CreateTeamDto();
        updateTeamDto.setName("Veres");
        updateTeamDto.setAccountBalance(BigDecimal.valueOf(1000000));

        when(teamRepository.findById(playerId)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> teamService.updateById(playerId, updateTeamDto));

        // Then
        String expected = "Can't find team by id " + playerId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(teamRepository, times(1)).findById(playerId);
        verifyNoMoreInteractions(teamRepository);
    }

    @Test
    public void transferMoney_WithValidData_ShouldTransferFunds() {
        // Given
        Long oldTeamId = 1L;
        Long newTeamId = 2L;
        double transferCost = 500000;

        Team oldTeam = new Team();
        oldTeam.setId(oldTeamId);
        oldTeam.setName("Veres");
        oldTeam.setAccountBalance(BigDecimal.valueOf(1000000));

        Team newTeam = new Team();
        newTeam.setId(newTeamId);
        newTeam.setName("Rukh");
        newTeam.setAccountBalance(BigDecimal.valueOf(700000));

        when(teamRepository.findById(oldTeamId)).thenReturn(Optional.of(oldTeam));
        when(teamRepository.findById(newTeamId)).thenReturn(Optional.of(newTeam));
        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Team result = teamService.transferMoney(oldTeamId, newTeamId, transferCost);

        // Then
        assertNotNull(result, "Resulting team should not be null");
        assertEquals(BigDecimal.valueOf(1500000.0), oldTeam.getAccountBalance(),
                "Old team's balance should increase by transfer cost");
        assertEquals(BigDecimal.valueOf(200000.0), newTeam.getAccountBalance(),
                "New team's balance should decrease by transfer cost");

        verify(teamRepository, times(1)).findById(oldTeamId);
        verify(teamRepository, times(1)).findById(newTeamId);
        verify(teamRepository, times(2)).save(any(Team.class));
    }

    @Test
    public void transferMoney_WithInsufficientFunds_ShouldThrowException() {
        // Given
        Long oldTeamId = 1L;
        Long newTeamId = 2L;
        double transferCost = 8000000;

        Team oldTeam = new Team();
        oldTeam.setId(oldTeamId);
        oldTeam.setName("Veres");
        oldTeam.setAccountBalance(BigDecimal.valueOf(1000000));

        Team newTeam = new Team();
        newTeam.setId(newTeamId);
        newTeam.setName("Rukh");
        newTeam.setAccountBalance(BigDecimal.valueOf(700000));

        when(teamRepository.findById(oldTeamId)).thenReturn(Optional.of(oldTeam));
        when(teamRepository.findById(newTeamId)).thenReturn(Optional.of(newTeam));

        // When
        Exception exception = assertThrows(NotEnoughFundsException.class,
                () -> teamService.transferMoney(oldTeamId, newTeamId, transferCost));

        // Then
        String expected = "Not enough funds in the team's balance";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(teamRepository, times(1)).findById(oldTeamId);
        verify(teamRepository, times(1)).findById(newTeamId);
        verify(teamRepository, never()).save(any(Team.class));
    }
}
