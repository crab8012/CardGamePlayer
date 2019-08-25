package space.crab8012.cardgameplayer.payloads;

import space.crab8012.cardgameplayer.gameobjects.Player;

public class PlayerObjectPayload extends Payload {
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    Player player;

    public PlayerObjectPayload(Player p){
        this.player = p;
    }
}
