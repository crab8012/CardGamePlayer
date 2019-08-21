package space.crab8012.cardgameplayer.gameobjects;

import java.io.Serializable;

public class GameCall implements Serializable {
    public enum GameCalls{
        BSCALL
    }
    private GameCalls gameCall;

    public GameCalls getGameCall() {
        return gameCall;
    }

    public void setGameCall(GameCalls gameCall) {
        this.gameCall = gameCall;
    }
}
