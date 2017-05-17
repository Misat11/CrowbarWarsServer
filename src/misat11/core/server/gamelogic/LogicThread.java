/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.gamelogic;

import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import misat11.core.server.Main;

/**
 *
 * @author misat11
 */
public class LogicThread implements Runnable {

    private Main core;
    private final float tpf = 0.1f;
    private AppStateManager stateManager;
    
    public LogicThread(Main core){
        this.core = core;
        stateManager = new AppStateManager(core);
    }
    
    public void attachAppState(AppState state){
        stateManager.attach(state);
    }
    
    public void detachAppState(AppState state){
        stateManager.detach(state);
    }
    
    @Override
    public void run() {
        stateManager.update(tpf);
    }
    
}
