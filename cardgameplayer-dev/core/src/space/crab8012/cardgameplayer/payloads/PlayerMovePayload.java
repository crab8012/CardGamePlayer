package space.crab8012.cardgameplayer.payloads;

import space.crab8012.cardgameplayer.gameobjects.Enums;
import space.crab8012.cardgameplayer.gameobjects.Player;

public class PlayerMovePayload extends Payload {
    Player player;
    Enums.RPSMOVES move;

    public PlayerMovePayload(Player p, Enums.RPSMOVES move){
        this.player = p;
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enums.RPSMOVES getMove() {
        return move;
    }

    public void setMove(Enums.RPSMOVES move) {
        this.move = move;
    }
}