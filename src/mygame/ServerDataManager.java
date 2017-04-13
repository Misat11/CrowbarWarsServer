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
public class ServerDataManager {
    
    private HashMap<Integer, PlayerData> entities = new HashMap<Integer, PlayerData>();
    
    public ServerDataManager() {
    
    }
    
    public void addOrRefreshplayer(int id, PlayerData data) {
        entities.put(id, data);
    }
    
    public void removePlayer(int id){
        if(entities.containsKey(id)){
            entities.remove(id);
        }
    }
    
    public boolean isTherePlayerWithId(int id) {
        return entities.containsKey(id);
    }
    
    public HashMap getPlayerList(){
        return entities;
    }
    
    public PlayerData getPlayerData(int id){
        return entities.get(id);
    }
}
