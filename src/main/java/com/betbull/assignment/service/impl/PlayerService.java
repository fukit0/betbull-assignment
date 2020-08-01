package com.betbull.assignment.service.impl;

import com.betbull.assignment.model.BetException;
import com.betbull.assignment.model.dto.PlayerDTO;
import com.betbull.assignment.model.entity.Player;
import com.betbull.assignment.model.entity.Team;
import com.betbull.assignment.repository.PlayerRepository;
import com.betbull.assignment.service.IPlayerService;
import com.betbull.assignment.service.ITeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService implements IPlayerService {

    @Autowired
    ITeamService teamService;

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void savePlayer(PlayerDTO playerDTO) {

        Optional<Team> t = teamService.findById(playerDTO.getTeamId());

        if(!t.isPresent()){
            throw new BetException("500", "Belirtilen id için takım bulunamadı. id:" + playerDTO.getTeamId());
        }

        Player player = new Player();
        player.setName(playerDTO.getName());

        playerRepository.save(player);

    }

    @Override
    public void updatePlayer(PlayerDTO playerDTO) {

        Optional<Player> playerOptional = findById(playerDTO.getId());

        if(!playerOptional.isPresent()){
            throw new BetException("500", "Güncellenmek istenen oyuncu bulunamadı. id:" + playerDTO.getId());
        }

        Optional<Team> t =  teamService.findById(playerDTO.getTeamId());

        if(!t.isPresent()){
            throw new BetException("500", "Belirtilen id için takım bulunamadı. id:" + playerDTO.getTeamId());
        }

        Player playerFound = playerOptional.get();
        playerFound.setName(playerDTO.getName());

        playerRepository.save(playerFound);

    }

    @Override
    public Optional<Player> findById(String playerId){

        return playerRepository.findById(playerId);
    }

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> getAllByTeamId(String teamId) {
        if(StringUtils.isEmpty(teamId)){
            return getAll();
        }else{
            return getAll().stream().filter(p -> p.getTeamSet().stream().anyMatch(t -> t.getId().equals(teamId))).collect(Collectors.toList());
        }
    }

}
