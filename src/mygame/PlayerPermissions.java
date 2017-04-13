/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.HashMap;

/**
 *
 * @author misat11
 */
public class PlayerPermissions {
    
    public HashMap<String, PermissionLevel> players;
    
    public PlayerPermissions() {}
    
    public void addPlayer(String playername, PermissionLevel permissions){
        players.put(playername, permissions);
    }
    
    public void removePlayer(String playername){
        if(players.containsKey(playername)){
            players.remove(playername);
        }
    }
}
