/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.JoinLeaveMessage;
import misat11.core.server.messages.TextMessage;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

/**
 *
 * @author misat11
 */
public class ClientConnectionListener implements ConnectionListener {

    private ServerInfoMessage serverInfoMessage;
    private ServerDataManager dataManager;

    public ClientConnectionListener() {
    }

    public ClientConnectionListener(ServerInfoMessage serverInfoMessage, ServerDataManager dataManager) {
        this.serverInfoMessage = serverInfoMessage;
        this.dataManager = dataManager;
    }

    @Override
    public void connectionAdded(Server s, HostedConnection source) {
        System.out.println("[REQUEST] New Client " + source.getId() + " [" + source.getAddress() + "] want to connect. Sending ServerInfoMessage");
        source.send(serverInfoMessage);
        s.broadcast(new JoinLeaveMessage(source.getId(), true));
    }

    @Override
    public void connectionRemoved(Server s, HostedConnection source) {
        System.out.println("[LEAVE PLAYER] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") now disconnected from server.");
        s.broadcast(new TextMessage(dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") now disconnected from server."));
        dataManager.removePlayer(source.getId());
        s.broadcast(new JoinLeaveMessage(source.getId(), false));
    }
}
