/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.basecommands;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import mygame.Command;
import mygame.Main;
import mygame.PermissionLevel;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class Stop implements Command {

    private Server server;
    private Main main;

    public Stop(Server server, Main main) {
        this.server = server;
        this.main = main;
    }

    @Override
    public void call(HostedConnection source, String m) {

    }

    @Override
    public void callFromServer(String m) {
        server.broadcast(new TextMessage("Server is shuting down."));
        server.close();
        main.stop();
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "Stop server";
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.SERVER;
    }

}
