package org.hachimi.eduCat.service;

import org.hachimi.eduCat.entity.principal.GameSession;
import org.hachimi.eduCat.entity.principal.ParticipationDefi;
import org.hachimi.eduCat.repository.principal.DefiRepository;
import org.hachimi.eduCat.repository.principal.GameSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

@Service
public class DefiService {
    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private DefiRepository defiRepository;

    public Boolean sessionMeetsDefiConditions(GameSession partie, ParticipationDefi defi) {
        if (defi.getStatut() == 1)
            return false;

        // Check if the challenge is associated with a game and if the game ID matches
        if (defi.getJeuAssocie() != null && !partie.getGameId().equals(defi.getJeuAssocie().getId())) {
            return false;
        }

        // Check if the score condition is met
        if (defi.getScoreCondition() != null && convertToScore(partie.getScore()) < defi.getScoreCondition()) {
            return false;
        }

        // Calculate the number of games played by the user for this game ID
        Integer countGamesByUserIdAndGameId = gameSessionRepository.countGamesByUserIdAndGameId(partie.getUserId(), partie.getGameId());

        // Check if the number of games played condition is met
        if (defi.getNbPartiesCondition() != null && countGamesByUserIdAndGameId < defi.getNbPartiesCondition()) {
            return false;
        }

        // If all conditions are met
        return true;
    }


    public static int convertToScore(String fraction) {
        // Séparation du numérateur et du dénominateur
        String[] parts = fraction.split("/");
        int numerator = Integer.parseInt(parts[0]);
        int denominator = Integer.parseInt(parts[1]);

        // Calcul du score
        double score = ((double) numerator / denominator) * 100;

        // Arrondi du score
        int roundedScore = (int) Math.round(score);

        return roundedScore;
    }
}
