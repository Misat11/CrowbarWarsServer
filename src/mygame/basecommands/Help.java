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
import mygame.TextMessage;

/**
 *
 * @author misat11
 */
public class Help implements Command {

    private Main main;
    
    public Help (Main main) {
        this.main = main;
    }
    
    @Override
    public void call(HostedConnection source, String m) {
        source.send(new TextMessage("------ HELP ------"));
        for (String key : main.commands.keySet()) {
            source.send(new TextMessage("/" + key + " - " + main.commands.get(key).getDescription()));
        }
        source.send(new TextMessage("------------------"));
    }
    
    @Override
    public void callFromServer(String m) {
        System.out.println("------ HELP ------");
        for (String key : main.commands.keySet()) {
            System.out.println("/" + key + " - " + main.commands.get(key).getDescription());
        }
        System.out.println("------------------");
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
