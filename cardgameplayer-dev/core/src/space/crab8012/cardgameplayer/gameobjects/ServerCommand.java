package space.crab8012.cardgameplayer.gameobjects;

import java.io.Serializable;
import java.util.ArrayList;

/*
 *  ServerCommand
 *
 *  This will eventually be moved into a separate library for making easier Java servers.
 *  This class should also remain relatively unchanged, due to it's simple nature.
 *  It is a class intended to transport large amounts of data that can easily be interpreted
 *  by both the client and the server.
 *
 * Both the client and the server should have a shared list of commands, as well as how to deal with them.
 */

public class ServerCommand implements Serializable {
    private String command;
    private ArrayList<Object> payload;

    public ServerCommand(String command, ArrayList<Object> payload){
        this.command = command;
        this.payload = payload;
    }

    public String getCommand() {
        return command;
    }

    public ArrayList<Object> getPayload() {
        return payload;
    }

    public void setCommand(String command){
        this.command = command;
    }
}
