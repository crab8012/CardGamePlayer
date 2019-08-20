package space.crab8012.cardgameplayer.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class GameMode implements Serializable {

    public enum WinCondition{
        ALLCARDINHAND, NOCARDINHAND, HIGHESTPOINTS
    }

    private WinCondition winCondition;
    private int maxPlayers;

    public GameMode(WinCondition winCondition){
        this.winCondition = winCondition;
        this.maxPlayers = 2;
    }
    public GameMode(WinCondition winCondition, int maxPlayers){
        this.winCondition = winCondition;
        this.maxPlayers = maxPlayers;
    }

    public WinCondition getWinCondition(){
        return winCondition;
    }
    public void setWinCondition(WinCondition winCondition){
        this.winCondition = winCondition;
    }
    public abstract Player checkForWinner(ArrayList<Player> players);
}
