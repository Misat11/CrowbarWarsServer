/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.basecommands;

import com.jme3.network.HostedConnection;
import misat11.core.server.Command;
import misat11.core.server.PermissionLevel;
import misat11.core.server.PlayerPermissions;

/**
 *
 * @author misat11
 */
public class Deop implements Command {

    private PlayerPermissions playerPermissions;

    public Deop(PlayerPermissions playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    @Override
    public void call(HostedConnection source, String m) {

    }

    @Override
    public void callFromServer(String m) {
        playerPermissions.removePlayer(m.replaceFirst("deop ", ""));
    }

    @Override
    public String getDescription() {
        return "Remove permissions from user";
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.SERVER;
    }

}
