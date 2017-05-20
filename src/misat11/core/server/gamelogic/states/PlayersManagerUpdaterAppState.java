/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.gamelogic.states;

import com.jme3.app.state.AbstractAppState;
import misat11.core.server.PlayersManager;

/**
 *
 * @author misat11
 */
public class PlayersManagerUpdaterAppState extends AbstractAppState {

    private PlayersManager playersManager;

    public PlayersManagerUpdaterAppState(PlayersManager playersManager) {
        this.playersManager = playersManager;
    }
    
    @Override
    public void update(float tpf){
        playersManager.update();
    }
}
