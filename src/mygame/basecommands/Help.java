/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.basecommands;

import com.jme3.network.HostedConnection;
import mygame.Command;
import mygame.Main;
import mygame.PermissionLevel;
import mygame.PlayerPermissions;
import mygame.ServerDataManager;
import misat11.core.server.messages.TextMessage;

/**
 *
 * @author misat11
 */
public class Help implements Command {

    private Main main;
    private ServerDataManager dataManager;
    private PlayerPermissions playerPermissions;

    public Help(Main main, ServerDataManager dataManager, PlayerPermissions playerPermissions) {
        this.main = main;
        this.dataManager = dataManager;
        this.playerPermissions = playerPermissions;
    }

    @Override
    public void call(HostedConnection source, String m) {
        source.send(new TextMessage("------ HELP ------"));
        for (String key : main.commands.keySet()) {
            if (main.commands.get(key).getPermissionlevel() != PermissionLevel.DEFAULT) {
                if (playerPermissions.hasPlayer(dataManager.getPlayerData(source.getId()).getName(), main.commands.get(key).getPermissionlevel())) {
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
