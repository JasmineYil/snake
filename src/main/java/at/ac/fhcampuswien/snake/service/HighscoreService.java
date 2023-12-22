package at.ac.fhcampuswien.snake.service;

import at.ac.fhcampuswien.snake.util.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.snake.util.Constants.HIGHSCORE_SEPARATOR;

public class HighscoreService {

    private HighscoreService() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOG = LoggerFactory.getLogger(HighscoreService.class);

    private static File getHighscoresFile() throws IOException {
        String path = "src/main/resources/highscores.txt";
        File highscoreFile = new File(path);
        return highscoreFile.createNewFile() ? highscoreFile : null;
    }

    private static List<String> getFileContent(File file) throws IOException{
        List<String> fileContent = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String lineInFile;

            while ((lineInFile = br.readLine()) != null) {
                fileContent.add(lineInFile);
            }
        } catch (IOException e) {
            LOG.error("Error reading the file content");
            throw new IOException(e);
        }

        return fileContent;
    }

    private static List<Player> getPlayerFromList(List<String> list) {
        List<Player> playerList = new ArrayList<>();

        if (!list.isEmpty()) {
            for (String line : list) {
                String[] parts = line.split(HIGHSCORE_SEPARATOR);
                Player player = new Player(parts[0], Integer.parseInt(parts[1]));
                playerList.add(player);
            }
        }
        return playerList;
    }

    public static List<Player> getSavedPlayerList() {
        List<Player> savedPlayers = null;

        try {
            File highscoreFile = getHighscoresFile();
            List<String> fileContent = getFileContent(highscoreFile);
            savedPlayers = getPlayerFromList(fileContent);
        } catch (IOException ex) {
            LOG.error("Error getting the saved players list");
            ex.printStackTrace();
        }
        return savedPlayers;
    }

    public static void savePlayerHighscore(Player currentPlayer) {
        try {
            File highscoreFile = getHighscoresFile();
            List<String> fileContent = getFileContent(highscoreFile);
            List<Player> players = getPlayerFromList(fileContent);

            if (!players.isEmpty()) {
                players.add(currentPlayer);

                players = players.stream()
                        .sorted(Comparator.comparingInt(Player::getScore).reversed())
                        .collect(Collectors.toList());

                if (players.size() > 5) {
                    Player previousLastPlayer = players.get(players.size() - 2);
                    Player lastPlayer = players.get(players.size() - 1);

                    if (lastPlayer.equals(currentPlayer) &&
                            lastPlayer.getScore() == previousLastPlayer.getScore()) {
                        players.remove(previousLastPlayer);
                    } else players.remove(lastPlayer);
                }
            } else {
                players.add(currentPlayer);
            }

            if (highscoreFile != null) {
                try (FileWriter fileWriter = new FileWriter(highscoreFile)) {
                    fileWriter.write("");
                    StringBuilder sb = new StringBuilder();
                    for (Player player : players) {
                        sb.append(player.getName());
                        sb.append(HIGHSCORE_SEPARATOR);
                        sb.append(player.getScore());

                        fileWriter.append(sb);
                        fileWriter.append(System.lineSeparator());
                        sb.setLength(0);
                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("Error while saving the players list");
            ex.printStackTrace();
        }
    }
}
