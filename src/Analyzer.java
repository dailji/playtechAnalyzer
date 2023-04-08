import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
public class Analyzer {

    static ArrayList<String> data = new ArrayList<>();
    static ArrayList<String> faultyLines = new ArrayList<>();
    static ArrayList<GameSession> games = new ArrayList<>();

    public static void main(String[] args) {
        /*
        In main function I start all needed methods for checking games.
         */
//        String fileName = "game_data_0.txt";
//        String fileName = "game_data_1.txt";
        String fileName = "game_data_2.txt";
        readFromFile(fileName);
        generateInstances();
        checkGames();
//        createAndWriteToFile("my_analyzer_output_0.txt");
//        createAndWriteToFile("my_analyzer_output_1.txt");
        createAndWriteToFile("my_analyzer_output_2.txt");
    }

    private static void checkGames() {
        /*
        This function is called to provoke the checking function in game itself.
         */
        games = new ArrayList<>(games.stream().sorted(Comparator.comparing(GameSession::getId)).toList());
        for (GameSession game : games) {
            game.sortTurns();
            game.checkGameLogic();
            String faultyLine = game.presentFaultyLine();
            if (!faultyLine.equals("")){
                faultyLines.add(faultyLine);
            }
        }
    }

    public static void generateInstances() {
        /*
        This function creates needed for instances for checking games.
         */
        for (String line : data) {
            ArrayList<String> lineData = new ArrayList<>(Arrays.asList(line.split(",")));
            if (lineData.size() == 6 && !lineData.contains(" ")){
                int id = Integer.parseInt(lineData.get(1));
                GameSession game;
                Optional<GameSession> checkIfGameIsThere = findCreatedGameSession(id, lineData.get(2));
                if (checkIfGameIsThere.isEmpty()){
                    game = new GameSession(id, lineData.get(2));
                    games.add(game);
                } else {
                    game = checkIfGameIsThere.get();
                }
                Turn currentTurn = new Turn(Integer.parseInt(lineData.get(0)), lineData.get(3));

                ArrayList<Card> dealerHand = Card.generateCardsFromHand(lineData.get(4));
                ArrayList<Card> playerHand = Card.generateCardsFromHand(lineData.get(5));

                currentTurn.addCardsToPlayer(playerHand);
                currentTurn.addCardsToDealer(dealerHand);

                game.addTurn(currentTurn);
            }
        }
    }

    public static Optional<GameSession> findCreatedGameSession(int id, String playerId){
        /*
        This function find needed game if it exists.
         */
        return games.stream().filter(o -> o.getId().equals(id) && o.getPlayerId().equals(playerId)).findFirst();
    }


    //Functions below are just for reading or writing in files.

    public static void readFromFile(String filename){
        try {
            data = new ArrayList<>(Files.readAllLines(Path.of(filename)));
        } catch (IOException e){
            throw new RuntimeException("File is missing");
        }
    }

    public static void createAndWriteToFile(String filename){
        try {
            File myObj = new File(filename);
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter(filename);
            for (String line : faultyLines) {
                myWriter.write(line + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Smth went wrong... :(");
            e.printStackTrace();
        }
    }
}
