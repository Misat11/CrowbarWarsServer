/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.basecommands;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import misat11.core.server.Command;
import misat11.core.server.PermissionLevel;
import misat11.core.server.PlayersManager;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class Kick implements Command {

    private PlayersManager playersManager;
    private Server server;

    public Kick(PlayersManager playersManager, Server server) {
        this.playersManager = playersManager;
        this.server = server;
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.OP;
    }

    @Override
    public void call(HostedConnection source, String m) {
        String string = m.replaceFirst("/kick ", "");
        int id = playersManager.getPlayerId(string);
        if (id != -1) {
            server.getConnection(id).close("You was kicked");
            source.send(new TextMessage("You kicked " + string + " from server."));
            System.out.println("[KICK] Client " + string + " was kicked by " + playersManager.getPlayerName(source.getId()) + ".");
        } else {
            source.send(new TextMessage("No player with name " + string + "."));
        }
    }

    @Override
    public void callFromServer(String m) {
        String string = m.replaceFirst("kick ", "");
        int id = playersManager.getPlayerId(string);
        if (id != -1) {
            server.getConnection(id).close("You was kicked");
            System.out.println("Succesfully kicked player " + string + ".");
            System.out.println("[KICK] Client " + string + " was kicked by server.");
        } else {
            System.out.println("No player with name " + string + ".");
        }
    }

    @Override
    public String getDescription() {
        return "Kick player from server";
    }

}
