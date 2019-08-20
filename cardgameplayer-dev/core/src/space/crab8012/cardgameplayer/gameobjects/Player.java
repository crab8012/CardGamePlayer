package space.crab8012.cardgameplayer.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable { //Done so that we can send this over the network
    private String name = "";
    private String icon = "";

    ArrayList<Card> hand = new ArrayList();

    int score = 0;

    public Player(String name, String icon){
        this.name = name;
        this.icon = icon;
    }

    public void setScore(int newScore){
        score = newScore;
    }
    public void addScore(int add){
        score += add;
    }
    public void removeScore(int remove){
        score -= remove;
        if(score < 0){
            score = 0;
        }
    }
    public int getScore(){
        return score;
    }
    public String getName(){
        return name;
    }
    public String getIcon(){
        return icon;
    }

    public void addCardToHand(Card card) throws Exception{
        if(hand.contains(card)){
            throw new Exception("Player Already has card in hand");
        }else{
            hand.add(card);
        }
    }

    public Card removeCardFromHand(Card card) throws Exception{
        if(!hand.contains(card)){
            throw new Exception("Player does not have card");
        }else{
            Card removedCard = hand.get(hand.indexOf(card));
            hand.remove(card);
            hand.trimToSize();
            return removedCard;
        }
    }

    public int getSizeOfHand(){
        return hand.size();
    }

    public ArrayList<Card> getHand(){
        return hand;
    }
}
