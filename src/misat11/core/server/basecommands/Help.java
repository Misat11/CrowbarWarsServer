/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.basecommands;

import com.jme3.network.HostedConnection;
import misat11.core.server.Command;
import misat11.core.server.Main;
import misat11.core.server.PermissionLevel;
import misat11.core.server.PlayerPermissions;
import misat11.core.server.PlayersManager;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class Help implements Command {

    private Main main;
    private PlayersManager playersManager;
    private PlayerPermissions playerPermissions;

    public Help(Main main, PlayersManager playersManager, PlayerPermissions playerPermissions) {
        this.main = main;
        this.playersManager = playersManager;
        this.playerPermissions = playerPermissions;
    }

    @Override
    public void call(HostedConnection source, String m) {
        source.send(new TextMessage("------ HELP ------"));
        for (String key : main.commands.keySet()) {
            if (main.commands.get(key).getPermissionlevel() != PermissionLevel.DEFAULT) {
                if (playerPermissions.hasPlayer(playersManager.getPlayerName(source.getId()), main.commands.get(key).getPermissionlevel())) {
                    source.send(new TextMessage("/" + key + " - " + main.commands.get(key).getDescription()));
                }
            } else {
                source.send(new TextMessage("/" + key + " - " + main.commands.get(key).getDescription()));
            }
        }
        source.send(new TextMessage("------------------"));
    }

    @Override
    public void callFromServer(String m) {
        System.out.println("--- SERVER HELP ---");
        for (String key : main.commands.keySet()) {
            System.out.println("/" + key + " - " + main.commands.get(key).getDescription());
        }
        System.out.println("-------------------");
    }

    @Override
    public String getDescription() {
        return "Display help of all commands";
    }

    @Override
    public PermissionLevel getPermissionlevel() {
        return PermissionLevel.DEFAULT;
    }

}
