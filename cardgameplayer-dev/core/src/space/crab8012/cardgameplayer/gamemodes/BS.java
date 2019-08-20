package space.crab8012.cardgameplayer.gamemodes;

import space.crab8012.cardgameplayer.gameobjects.Card;
import space.crab8012.cardgameplayer.gameobjects.GameMode;
import space.crab8012.cardgameplayer.gameobjects.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class BS extends GameMode implements Serializable {

    public BS() {
        super(WinCondition.NOCARDINHAND, 4);
    }

    public Player getFirstPlayer(ArrayList<Player> players) throws Exception{
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

    @Override
    public Player checkForWinner(ArrayList<Player> players) {
        Player winningPlayer = null;

        for (Player player : players) {
            if(player.getHand().size() == 0){
                winningPlayer = player;
            }
        }

        return winningPlayer;
    }
}
