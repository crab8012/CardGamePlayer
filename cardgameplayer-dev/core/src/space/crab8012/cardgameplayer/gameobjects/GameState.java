package space.crab8012.cardgameplayer.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class GameState implements Serializable {

    GameMode game;
    ArrayList<Card> deck = new ArrayList();
    ArrayList<Player> players = new ArrayList();
    ArrayList<Card> pile = new ArrayList();
    int maxGameSize;
    int numConnectedPlayers = 0;
    int cardsPlayed = 0;
    Player currentPlayer, lastPlayer;

    public GameState(GameMode game){
        this.game = game;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void addPlayer(Player player){
        players.add(player);
        numConnectedPlayers++;
    }
    public void removePlayer(Player player){
        if(players.remove(player)){
            players.trimToSize();
            numConnectedPlayers--;
        }
    }
    public int getMaxGameSize() {
        return maxGameSize;
    }
    public int getNumConnectedPlayers() {
        return numConnectedPlayers;
    }

    public void createDeck(){
        for(int suit = 1; suit < 6; suit++){
            for(int value = 1; value < 14; value++){
                if(suit == 1)
                    deck.add(new Card(Card.Suits.HEARTS, value));
                if(suit == 2)
                    deck.add(new Card(Card.Suits.SPADES, value));
                if(suit == 3)
                    deck.add(new Card(Card.Suits.CLUBS, value));
                if(suit == 4)
                    deck.add(new Card(Card.Suits.DIAMONDS, value));
            }
        }

        System.out.println(deck.size());
    }

    //Game Loop
    //Wait for game start
    //Get all players Connected
    //Create the Deck
    //Deal the Cards to All Players
    //Get the first player
    //Wait for command from first player
    //Continue to see if player calls BS
    //Handle BS Call
    //              Give a player the entire pile
    //Check to see if a player has won, on their turn

    //--------BullShit Game Implementation
    public void dealAllCards(){
        Random rdm = new Random();
        int playerNum = 0;
        for(int i = 0; i < 52; i++){
            int cardIndex = rdm.nextInt(deck.size());
            try {
                players.get(playerNum).addCardToHand(deck.get(cardIndex));
                deck.remove(cardIndex);
                deck.trimToSize();
            }catch(Exception e){
                System.out.println(e.getMessage());
            }

            playerNum++;
            if(playerNum >= maxGameSize){
                playerNum = 0;
            }
        }
    }

    public Player getFirstPlayer(ArrayList<Player> players) throws Exception{ // goes through the list of players to find the one with the Ace of Spades
        Player firstPlayer = null;
        for(Player p :players){
            if(p.getHand().contains(new Card(Card.Suits.SPADES, 1))){
                firstPlayer = p;
            }
        }

        if(firstPlayer == null){
            throw new Exception("No players satisfy the First Player Requirement");
        }

        return firstPlayer;
    }

    public void playCard(Player player, Card card) throws Exception{
        if(pile.contains(card)){
            throw new Exception("Card already in pile");
        }else{
            pile.add(card);
            player.removeCardFromHand(card);
            player.getHand().trimToSize();
        }
    }

    public void setNumCardsPlayed(int cardsPlayed){
        this.cardsPlayed = cardsPlayed;
    }

    public int getNumCardsPlayed(){
        return this.cardsPlayed;
    }

    public void bsCalled(){
        int lastValue = -1;
        for(int i = 0; i < cardsPlayed; i++){
            Card c = pile.get(pile.size()-i);
            if(lastValue == -1){
                lastValue = c.getValue();
            }else{
                if(lastValue == c.getValue()){
                    lastValue = c.getValue();
                }else{
                    givePlayerPile(new Player("n", "i"));
                }
            }
        }
    }

    public void givePlayerPile(Player p){
        for(Card c : pile){
            try {
                p.addCardToHand(c);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        pile.removeAll(pile);
        pile.trimToSize();
    }

    public void hasPlayerWon(){
        if(currentPlayer.hand.size() == 0){
            gameOver();
        }
    }

    public void gameOver(){
        System.out.println("Player " + currentPlayer.getName() + " has won the game!");
    }

}
