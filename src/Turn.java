import java.util.ArrayList;

public class Turn {
    int timeStamp;
    String move;
    ArrayList<Card> dealerCards;
    ArrayList<Card> playerCards;
    Turn(Integer timeStamp, String move){
        this.timeStamp = timeStamp;
        this.move = move;
    }

    public void addCardsToPlayer(ArrayList<Card> cards){
        playerCards = cards;
    }
    public void addCardsToDealer(ArrayList<Card> cards){
        dealerCards = cards;
    }
    public int getTimeStamp() {
        return timeStamp;
    }
    public int getCurrentDealerHandValue(){
        return dealerCards.stream().mapToInt(c -> c.cardsValue).sum();
    }
    public int getCurrentPlayerHandValue(){
        return playerCards.stream().mapToInt(c -> c.cardsValue).sum();
    }

    @Override
    public String toString() {
        return "Turn{" +
                "timeStamp=" + timeStamp +
                ", Move:'" + move + '\'' +
                ",\n Dealer cards:" + dealerCards +
                ",\n Player ards:" + playerCards +
                "}\n\n";
    }
}
