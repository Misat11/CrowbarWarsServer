/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import misat11.core.server.messages.ServerInfoMessage;
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
    private PlayersManager playersManager;

    public ClientConnectionListener() {
    }

    public ClientConnectionListener(ServerInfoMessage serverInfoMessage, PlayersManager playersManager) {
        this.serverInfoMessage = serverInfoMessage;
        this.playersManager = playersManager;
    }

    @Override
    public void connectionAdded(Server s, HostedConnection source) {
        System.out.println("[REQUEST] New Client " + source.getId() + " [" + source.getAddress() + "] want to connect. Sending ServerInfoMessage");
        source.send(serverInfoMessage);
    }

    @Override
    public void connectionRemoved(Server s, HostedConnection source) {
        if (playersManager.isTherePlayerWithId(source.getId())) {
            System.out.println("[LEAVE PLAYER] " + playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") now disconnected from server.");
            s.broadcast(new TextMessage(playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") now disconnected from server."));
            playersManager.removePlayer(source.getId());
        }
    }
}
