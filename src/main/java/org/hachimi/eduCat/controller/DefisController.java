package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.entity.principal.Game;
import org.hachimi.eduCat.entity.principal.ParticipationDefi;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.DefiRepository;
import org.hachimi.eduCat.repository.principal.GameRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class DefisController {
    @Autowired
    private DefiRepository defiRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @PostMapping(path = "/getDefisJoueur", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDefisJoueur(@RequestBody String body_str) throws ServerException, InformationsException {

        JSONObject body = new JSONObject(body_str);
        JSONArray returnDefis = new JSONArray();

        try {
            User user = ifLoggedThenReturnUserElseThrowException(body);

            // Tout les defis du joueur
            List<ParticipationDefi> AllDefisJoueur = defiRepository.findAllByJoueurAssocie(user.getId());

            for (ParticipationDefi DefiJoueur : AllDefisJoueur) { // recup chaque defi du joueur

                JSONObject FormattedDefi = new JSONObject();

                FormattedDefi.put("defiData", DefiJoueur.GetInfos());

                if (DefiJoueur.getJeuAssocie() != null) {
                    FormattedDefi.put("NomJeu", DefiJoueur.getJeuAssocie().getNomJeu());
                    FormattedDefi.put("ImageJeu", DefiJoueur.getJeuAssocie().getImageJeu());
                }

                // Ajouter la description au JSON
                FormattedDefi.put("DescriptionDefi", generateDefiDescription(DefiJoueur));

                returnDefis.put(FormattedDefi);

            }
        } catch (JSONException e) {
            throw new InformationsException();
        } catch (Exception e) {
            returnDefis.put("error");
            e.printStackTrace();
        }

        return returnDefis.toString();
    }

    @PostMapping(path = "/setDefis", produces = MediaType.APPLICATION_JSON_VALUE)
    public String setDefisForJoueur(@RequestBody String body_str) {
        JSONObject body = new JSONObject(body_str);
        JSONObject response = new JSONObject();
        JSONArray defisCrees = new JSONArray();

        try {
            User user = ifLoggedThenReturnUserElseThrowException(body);

            for (int i = 0; i < 10; i++) {
                ParticipationDefi defi = generateRandomDefi(user);
                defiRepository.save(defi);
            }

            response.put("success", true);
            response.put("defis", defisCrees);
        } catch (JSONException e) {
            response.put("error", "Invalid JSON format");
            e.printStackTrace();
        } catch (Exception e) {
            response.put("error", "An error occurred");
            e.printStackTrace();
        }

        return response.toString();
    }

    public ParticipationDefi generateRandomDefi(User user) {
        Random random = new Random();

        // Recup les jeux
        List<Game> allGames = new ArrayList<>();
        gameRepository.findAll().forEach(allGames::add);

        ParticipationDefi defi = new ParticipationDefi();

        // 50% chance d'assigner un jeu au défi
        if (!allGames.isEmpty() && random.nextBoolean()) {
            Game selectedGame = allGames.get(random.nextInt(allGames.size()));
            defi.setJeuAssocie(selectedGame);
        }

        defi.setNbParties(determineNbParties());
        defi.setScoreCondition(determineScore());
        defi.setJoueurAssocie(user.getId());

        double difficulty = calculateDifficultyLevel(defi);
        ArrayList<Integer> recompenses = calculateRecompenses(difficulty);

        defi.setDifficulteDefi(difficulty);
        defi.setRecompenseXp(recompenses.get(0));
        defi.setRecompenseECats(recompenses.get(1));

        return defi;
    }

    private int determineNbParties() {
        Random random = new Random();

        int randPercentage = random.nextInt(100) + 1;
        if (randPercentage <= 70) { // 70% de chance
            return 1;
        } else if (randPercentage <= 90) { // 20% de chance
            return 3;
        } else { // 10% de chance
            return 10;
        }
    }

    private int determineScore() {
        Random random = new Random();
        int randPercentage = random.nextInt(100) + 1;
        // 40% de chances d'attribuer un score de 100%
        if (randPercentage <= 40) {
            return 100;
        } else {
            // Autres scores possibles : 50, 60, 70, 80, 90
            int[] possibleScores = {50, 60, 70, 80, 90};
            int index = random.nextInt(possibleScores.length);
            return possibleScores[index];
        }
    }

    public double calculateDifficultyLevel(ParticipationDefi defi) {
        double difficultyScore = 0.0;

        // Poids pour le nombre de parties
        double partiesFactor = switch (defi.getNbParties()) {
            case 1 -> 0.1; // Moins difficile
            case 3 -> 0.3; // Moyennement difficile
            case 10 -> 0.8; // Plus difficile
            default -> 0.1;
        };

        // Poids pour le score à obtenir
        double scoreFactor = defi.getScoreCondition() / 100.0; // Normalisé entre 0 et 1

        // Calcul du niveau de difficulté basé sur les facteurs
        difficultyScore = 0.5 * partiesFactor + 0.5 * scoreFactor;

        if (defi.getJeuAssocie() != null) {
            difficultyScore += 0.1;
        }

        // Assurez-vous que la difficulté reste entre 0 et 1
        difficultyScore = Math.min(Math.max(difficultyScore, 0.0), 1.0);

        return difficultyScore;
    }

    public ArrayList<Integer> calculateRecompenses(double difficultyScore) {
        ArrayList<Integer> recompenses = new ArrayList<>();

        // XP
        int xpInit = (350 - 10) / 15;
        int integerXpInit = (int) (xpInit * difficultyScore);
        int recompenseXp = 10 + (integerXpInit * 15);

        recompenseXp = Math.min(Math.max(recompenseXp, 10), 350);

        recompenses.add(recompenseXp);

        // E-CAT
        int totalSteps = (40 - 5) / 5;
        int palierIndex = Math.min((int) (difficultyScore * totalSteps), totalSteps);
        int recompenseEcat = 5 + (palierIndex * 5);

        recompenses.add(recompenseEcat);

        return recompenses;
    }

    private String generateDefiDescription(ParticipationDefi DefiJoueur) {
        StringBuilder descriptionDefi = new StringBuilder();
        String nomJeu = (DefiJoueur.getJeuAssocie() != null) ? DefiJoueur.getJeuAssocie().getNomJeu() : "";
        String nbParties;
        if (DefiJoueur.getNbParties() == null || DefiJoueur.getNbParties() == 1) {
            nbParties = "une";
        } else {
            nbParties = String.valueOf(DefiJoueur.getNbParties());
        }
        Integer scoreCondition = DefiJoueur.getScoreCondition();

        // Vérifier si le score est spécifié et ajuster le texte selon la valeur
        String scoreTexte = scoreCondition != null && scoreCondition < 100 ? "d'au moins " : "de ";

        if (nbParties != null && scoreCondition != null) {
            // Si le nombre de parties et le score sont spécifiés
            descriptionDefi.append("Obtenez un score ").append(scoreTexte).append(scoreCondition).append("% dans ").append(nbParties).append(" partie(s)");
            if (!nomJeu.equals("")) {
                descriptionDefi.append(" de \"").append(nomJeu).append("\"");
            }
        } else if (nbParties != null) {
            // Si seulement le nombre de parties est spécifié
            descriptionDefi.append("Jouer ").append(nbParties).append(" partie(s)");
            if (!nomJeu.equals("")) {
                descriptionDefi.append(" de \"").append(nomJeu).append("\"");
            }
        } else if (scoreCondition != null) {
            // Si seulement le score est spécifié
            descriptionDefi.append("Obtenez un score ").append(scoreTexte).append(scoreCondition).append("%");
            if (!nomJeu.equals("")) {
                descriptionDefi.append(" à \"").append(nomJeu).append("\"");
            }
        }

// Si aucune condition n'est spécifiée, vous pouvez ajouter une description par défaut
        if (descriptionDefi.length() == 0) {
            descriptionDefi.append("Complétez le défi");
        }

        return descriptionDefi.toString();
    }

    private User ifLoggedThenReturnUserElseThrowException(JSONObject body) throws NotValidJwsException {
        String jws = body.getString("jws");
        JWTService.verifyJWT(jws);
        JSONObject payload = JWTService.getPayload(jws);
        Optional<User> user = userRepository.findById(payload.getInt("id"));

        if (!user.isPresent()) {
            throw new NotValidJwsException();
        }

        return user.get();
    }
}
