/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.gamelogic.states;

import com.jme3.app.state.AbstractAppState;
import misat11.core.server.ServerDataStorage;

/**
 *
 * @author misat11
 */
public class EntitiesUpdaterAppState extends AbstractAppState {

    private ServerDataStorage serverDataStorage;

    public EntitiesUpdaterAppState(ServerDataStorage serverDataStorage) {
        this.serverDataStorage = serverDataStorage;
    }
    
    @Override
    public void update(float tpf){
        serverDataStorage.syncEntityData();
    }
    
}
