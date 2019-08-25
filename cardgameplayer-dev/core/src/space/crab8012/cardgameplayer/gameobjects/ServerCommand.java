package space.crab8012.cardgameplayer.gameobjects;

import space.crab8012.cardgameplayer.payloads.Payload;

import java.io.Serializable;

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
    private String id;
    private String command;
    private Payload payload;

    public ServerCommand(String command, Payload payload, String id){
        this.command = command;
        this.payload = payload;
        this.id = id;
    }

    public ServerCommand(String command, String id){
        this.command = command;
        this.payload = null;
        this.id = id;
    }

    public ServerCommand(String command, Payload payload){
        this.command = command;
        this.payload = payload;
        this.id = null;
    }

    public ServerCommand(String command){
        this.command = command;
        this.payload = null;
        this.id = null;
    }

    public String getCommand() {
        return command;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
