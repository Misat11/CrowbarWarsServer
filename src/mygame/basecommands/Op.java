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
import mygame.PlayerPermissions;
import mygame.ServerDataManager;
import mygame.TextMessage;

/**
 *
 * @author misat11
 */
public class Op implements Command {

    private PlayerPermissions playerPermissions;

    public Op(PlayerPermissions playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    @Override
    public void call(HostedConnection source, String m) {

    }

    @Override
    public void callFromServer(String m) {
        playerPermissions.addPlayer(m.replaceFirst("op ", ""), PermissionLevel.OP);
    }

    @Override
    public String getDescription() {
        return "Add permissions to user";
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.SERVER;
    }

}
