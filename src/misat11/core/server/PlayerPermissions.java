/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import java.util.HashMap;

/**
 *
 * @author misat11
 */
public class PlayerPermissions {

    public HashMap<String, PermissionLevel> players;

    public PlayerPermissions() {
        players = new HashMap<String, PermissionLevel>();
    }

    public boolean hasPlayer(String playername, PermissionLevel permissions) {
        if (players.containsKey(playername)) {
            return players.get(playername) == permissions;
        } else {
            return false;
        }
    }

    public void addPlayer(String playername, PermissionLevel permissions) {
        players.put(playername, permissions);
    }

    public void removePlayer(String playername) {
        if (players.containsKey(playername)) {
            players.remove(playername);
        }
    }
}
