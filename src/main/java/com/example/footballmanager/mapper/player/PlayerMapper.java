package com.example.footballmanager.mapper.player;

import com.example.footballmanager.config.MapperConfig;
import com.example.footballmanager.dto.player.CreatePlayerDto;
import com.example.footballmanager.dto.player.PlayerDto;
import com.example.footballmanager.model.Player;
import com.example.footballmanager.model.Team;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface PlayerMapper {
    @Mapping(target = "teamId", source = "team.id")
    PlayerDto toDto(Player player);

    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamFromId")
    Player toModel(CreatePlayerDto createPlayerDto);

    @Named("teamFromId")
    default Team teamFromId(Long id) {
        return Optional.ofNullable(id)
                .map(Team::new)
                .orElse(null);
    }
}
