/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import misat11.core.server.messages.TextMessage;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.MoveMessage;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.messages.ServerWantClientHasMessage;

/**
 *
 * @author misat11
 */
public class ServerListener implements MessageListener<HostedConnection> {

    private Server server;
    private PlayersManager playersManager;
    private Main main;
    private ServerDataStorage serverDataStorage;

    public ServerListener(Main main, Server server, PlayersManager playersManager, ServerDataStorage serverDataStorage) {
        this.server = server;
        this.playersManager = playersManager;
        this.main = main;
        this.serverDataStorage = serverDataStorage;
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        if (m instanceof PlayerSettingsMessage) {
            PlayerSettingsMessage p = (PlayerSettingsMessage) m;
            if (playersManager.isTherePlayerWithName(p.getName()) && playersManager.getPlayerId(p.getName()) != source.getId()) {
                System.out.println("[KICK] Player " + p.getName() + "(Client " + source.getId() + ") was kicked because he doesn't have original name.");
                source.close("You haven't original name!");
            } else {
                playersManager.addPlayer(source.getId(), p);
                serverDataStorage.checkClientHasMessage(source.getId(), new ClientHasMessage());
                System.out.println("[NEW PLAYER] " + p.getName() + " (Client " + source.getId() + ") completed connection succesfully.");
                server.broadcast(new TextMessage(p.getName() + " (Client " + source.getId() + ") now connected to server."));
                source.send(new ServerWantClientHasMessage());
            }
        }
        if (m instanceof TextMessage) {
            TextMessage message = (TextMessage) m;
            String orgmsg = message.getMessage();
            if (orgmsg.startsWith("/")) {
                String[] args = orgmsg.replaceFirst("/", "").split(" ");
                if (main.commands.containsKey(args[0])) {
                    PermissionLevel perms = main.commands.get(args[0]).getPermissionlevel();
                    if (perms != PermissionLevel.DEFAULT) {
                        if (main.permissions.hasPlayer(playersManager.getPlayerName(source.getId()), perms)) {
                            System.out.println("[REQUEST] " + playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ")");
                            main.commands.get(args[0]).call(source, orgmsg);
                        } else {
                            System.out.println("[REQUEST] " + playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ") without permissions.");
                            source.send(new TextMessage("You haven't permissions to use command /" + args[0]));
                        }
                    } else {
                        System.out.println("[REQUEST] " + playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ")");
                        main.commands.get(args[0]).call(source, orgmsg);
                    }
                } else {
                    System.out.println("[REQUEST] " + playersManager.getPlayerName(source.getId()) + " (Client " + source.getId() + ") calling not exist command " + orgmsg);
                    source.send(new TextMessage("Command /" + args[0] + " not found. Type \"/help\" for help."));
                }
            } else {
                String chatmsg = "Client " + playersManager.getPlayerName(source.getId()) + ": " + message.getMessage();
                System.out.println("[CHAT] " + chatmsg);
                server.broadcast(new TextMessage(chatmsg));
            }
        }
        if (m instanceof MoveMessage) {
            MoveMessage mm = (MoveMessage) m;
            playersManager.move(source.getId(), mm);
        }
        if (m instanceof ClientHasMessage) {
            serverDataStorage.checkClientHasMessage(source.getId(), (ClientHasMessage) m);
        }
    }

}
