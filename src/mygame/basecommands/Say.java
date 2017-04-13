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
import mygame.TextMessage;

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
        TextMessage tm = new TextMessage("Server said: " + m);
        server.broadcast(tm);
        System.out.println("[CHAT] " + tm.toString());
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
