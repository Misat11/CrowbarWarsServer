/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.basecommands;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import mygame.Command;
import mygame.PermissionLevel;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class Say implements Command {

    private Server server;

    public Say(Server server) {
        this.server = server;
    }

    @Override
    public void call(HostedConnection source, String m) {

    }

    @Override
    public void callFromServer(String m) {
        String nm = m.replaceFirst("say", "");
        if (nm.equals("") == false) {
            server.broadcast(new TextMessage("Server said: " + nm));
            System.out.println("[CHAT] Server said:" + nm);
        }
    }

    @Override
    public String getDescription() {
        return "You can say from server to chat.";
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.SERVER;
    }

}
