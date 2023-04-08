import java.util.*;

public class GameSession {
    public ArrayList<Integer> faultyLinesTimeStamp = new ArrayList<>();
    public int id;
    public String playerId;
    public Turn previousTurn;
    public Turn currentTurn;
    public ArrayList<Turn> turns = new ArrayList<>();

    GameSession(int id, String playerId){
        this.id = id;
        this.playerId = playerId;
    }
    public void addTurn(Turn t){
        turns.add(t);
    }
    public Integer getId() {
        return id;
    }
    public String getPlayerId() {
        return playerId;
    }
    public void sortTurns(){
        turns = new ArrayList<>(turns.stream().sorted(Comparator.comparing(Turn::getTimeStamp)).toList());
    }

    public void checkGameLogic() throws RuntimeException{
        /*
        Main function for checking the game for faulty turns, that call all needed functions
         */
        for (Turn turn : turns) {
            currentTurn = turn;
            checkForNullValueCards(turn);
            if (!faultyLinesTimeStamp.contains(turn.timeStamp)){
                checkIfDealersHandIsEmpty(turn);
                checkIfPlayerWentOver(turn);
                checkIfDealerWentOver(turn);
                checkIfCardsDoNotRepeat(turn);
                checkDealerMustHit(turn);
                checkIfTurnSequenceIsRight();
            }
            previousTurn = turn;
        }
    }

    public void checkForNullValueCards(Turn turn){
        for (Card card : turn.dealerCards) {
            if (card.cardsValue == null){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
        for (Card card : turn.playerCards) {
            if (card.cardsValue == null){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
    }
    private void checkIfDealersHandIsEmpty(Turn turn) {
        int dealerSum = turn.getCurrentDealerHandValue();
        if (!turn.move.equals("P Left")){
            if (dealerSum < 2){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
    }


    private void checkIfPlayerWentOver(Turn turn) {
        int playerSum = turn.getCurrentPlayerHandValue();

        if (turn.move.equals("P Hit") || turn.move.equals("P Stand")){
            if (playerSum > 21){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
    }

    public void checkIfDealerWentOver(Turn turn){
        int dealerSum = turn.getCurrentDealerHandValue();
        int playerSum = turn.getCurrentPlayerHandValue();

        if (turn.move.equals("D Hit") && dealerSum > 16){
            faultyLinesTimeStamp.add(turn.timeStamp);
        }
        if (turn.move.equals("P Lose") && playerSum < 22 && dealerSum > 21){
            faultyLinesTimeStamp.add(turn.timeStamp);
        }
    }

    public void checkDealerMustHit(Turn turn){
        if (turn.move.equals("P Win") || turn.move.equals("P Lose")){
            int dealerSum = turn.getCurrentDealerHandValue();
            int playerSum = turn.getCurrentPlayerHandValue();

            if (playerSum < 22 && dealerSum < 17){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
            if (playerSum < 22 && dealerSum > 16 && dealerSum < 22 && dealerSum > playerSum){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
    }

    public void checkIfCardsDoNotRepeat(Turn turn){
        for (Card playerCard : turn.playerCards) {
            if (turn.dealerCards.contains(playerCard)){
                faultyLinesTimeStamp.add(turn.timeStamp);
            }
        }
    }

    public void checkIfTurnSequenceIsRight(){
        if (currentTurn.move.equals("D Show") && !previousTurn.move.equals("P Stand")){
            faultyLinesTimeStamp.add(previousTurn.timeStamp);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameSession that)) return false;
        return id == that.id && playerId.equals(that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerId);
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "id=" + id +
                ", playerId='" + playerId + '\'' +
                ", turnList=\n" + turns +
                "}\n";
    }

    public String presentFaultyLine() {
        /*
        This function combine back the faulty line
         */
        if (faultyLinesTimeStamp.isEmpty()){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        faultyLinesTimeStamp.sort(null);

        Turn turn = turns.stream().filter(t -> t.timeStamp == faultyLinesTimeStamp.get(0)).findFirst().get();

        stringBuilder.append(turn.timeStamp).append(",").append(id).append(",")
                .append(playerId).append(",").append(turn.move).append(",");

        for (Card dealerCard : turn.dealerCards) {
            stringBuilder.append(dealerCard.cardsName).append("-");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append(",");

        for (Card playerCard : turn.playerCards) {
            stringBuilder.append(playerCard.cardsName).append("-");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        return stringBuilder.toString();
    }
}
