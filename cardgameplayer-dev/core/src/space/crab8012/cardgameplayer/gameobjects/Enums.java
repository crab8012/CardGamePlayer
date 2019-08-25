package space.crab8012.cardgameplayer.gameobjects;

public class Enums {
    public enum RPSMOVES {
        ROCK, PAPER, SCISSORS
    }

    /*
     * An ENUM that mirrors an enum to be added to the client.
     * It is intended to evolve as the client-server pair evolve.
     */
    // GETPLAYER - Request to get a player object.
    // SENDPLAYER - Response to a GETPLAYER request. Generally a Player object.
    // UPDATEGAMESTATE - Response-less command. Sent from Server to Client. Contains GameState.
    // GETMOVE - Tell the client to send over the player's move.
    // SENDMOVE - Send the server the player's move.
    // SENDWINNER - Send the client the winning player.
    public enum COMMANDS {
        GETPLAYER, UPDATEGAMESTATE, SENDPLAYER, GETMOVE, SENDWINNER, QUIT
    }
}
