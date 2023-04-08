import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Card {
    String cardsName;
    Integer cardsValue;
    static final HashMap<String, Integer> valuesForCards = new HashMap<>() {{
        put("2", 2); put("3", 3); put("4", 4); put("5", 5);
        put("6", 6); put("7", 7); put("8", 8); put("9", 9);
        put("10", 10); put("J", 10); put("Q", 10); put("K", 10);
        put("A", 11); put("?", 0);
    }};

    Card(String cardsName, Integer cardsValue){
        this.cardsName = cardsName;
        this.cardsValue = cardsValue;
    }

    @Override
    public String toString() {
        return "Card:(" + cardsName +
                "," + cardsValue + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return cardsName.equalsIgnoreCase(card.cardsName) && cardsValue.equals(card.cardsValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardsName, cardsValue);
    }

    public static ArrayList<Card> generateCardsFromHand(String stringHand){

        ArrayList<Card> cards = new ArrayList<Card>();

        for (String str : stringHand.split("-")) {

            Integer value;

            if (str.length() == 3 && str.startsWith("10")){
                value = valuesForCards.get("10");
            } else if (str.length() < 3){
                value = valuesForCards.get(str.substring(0,1).toUpperCase());
            } else {
                value = null;
            }
            Card card = new Card(str, value);

            cards.add(card);
        }
        return cards;
    }

}
