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

    public String verifierEtMettreAJourDefis(Integer idUser, Integer idGame, String score, String difficultyLibelle) {

        if(true)
            return "test";

        // Convertir le score en entier pour la comparaison
        Integer scorePartie = Integer.parseInt(score);

        // Récupérer toutes les sessions de jeu de l'utilisateur
        Iterable<GameSession> sessions = gameSessionRepository.getGameSessionsByIdUser(idUser);

        // Récupérer tous les défis associés à cet utilisateur
        List<ParticipationDefi> defis = defiRepository.findAllByJoueurAssocie(idUser);

        for (ParticipationDefi defi : defis) {
            // Vérifier si le défi est déjà accompli
            if (defi.getStatut() == 1) continue; // Supposons que le statut 1 signifie accompli

            boolean conditionScore = scorePartie >= defi.getScoreCondition(); // Vérifie si le score de la partie remplit la condition du défi
            boolean conditionNbParties = ((List<GameSession>) sessions).size() >= defi.getNbParties(); // Vérifie si le nombre de parties jouées remplit la condition

            // Si les conditions sont remplies, mettre à jour le statut du défi
            if (conditionScore && conditionNbParties) {
                defi.setStatut(1); // Mettre à jour le statut comme accompli
                defiRepository.save(defi); // Sauvegarder la mise à jour dans la base de données
                // Vous pouvez ajouter ici la logique pour attribuer des récompenses
                return "succes";
            }
        }
        return score;
    }
}
