package space.crab8012.cardgameplayer.gameobjects;

public class Card {
    public enum Suits{
        HEARTS, SPADES, CLUBS, DIAMONDS
    }

    Suits suit;
    int value; //1 to 13(Ace, 2-10, J, Q, K).

    public Card(Suits suit, int value){
        this.suit = suit;
        this.value = value;
    }
    public Suits getSuit(){
        return suit;
    }
    public int getValue(){
        return value;
    }
    public String getCardFace(){
        return suit.name() + value;
    }
}
