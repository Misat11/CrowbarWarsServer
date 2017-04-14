/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

/**
 *
 * @author misat11
 */
public class ServerListener implements MessageListener<HostedConnection> {

    private Server server;
    private ServerDataManager dataManager;
    private Main main;

    public ServerListener(Main main, Server server, ServerDataManager dataManager) {
        this.server = server;
        this.dataManager = dataManager;
        this.main = main;
    }

    @Override
    public void messageReceived(HostedConnection source, Message m) {
        if (m instanceof PlayerDataMessage) {
            PlayerDataMessage p = (PlayerDataMessage) m;
            PlayerData pd = new PlayerData();
            pd.setId(source.getId());
            pd.setHealth(p.getHealth());
            pd.setLocation(p.getLocation());
            pd.setName(p.getName());
            pd.setModelAsset(p.getModelAsset());
            pd.setRotation(p.getRotation());
            pd.setWalkDirection(p.getWalkDirection());
            if (dataManager.isTherePlayerWithId(source.getId()) == false) {
                dataManager.addOrRefreshplayer(source.getId(), pd);
                System.out.println("[NEW PLAYER] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") completed connection succesfully.");
                server.broadcast(new TextMessage(dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") now connected to server."));
            } else {
                dataManager.addOrRefreshplayer(source.getId(), pd);
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
                        if (main.permissions.hasPlayer(dataManager.getPlayerData(source.getId()).getName(), perms)) {
                            System.out.println("[REQUEST] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ")");
                            main.commands.get(args[0]).call(source, orgmsg);
                        } else {
                            System.out.println("[REQUEST] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ") without permissions.");
                            source.send(new TextMessage("You haven't permissions to use command /"+args[0]));
                        }
                    } else {
                        System.out.println("[REQUEST] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") call command " + args[0] + " (" + orgmsg + ")");
                        main.commands.get(args[0]).call(source, orgmsg);
                    }
                } else {
                    System.out.println("[REQUEST] " + dataManager.getPlayerData(source.getId()).getName() + " (Client " + source.getId() + ") calling not exist command " + orgmsg);
                    source.send(new TextMessage("Command /" + args[0] + " not found. Type \"/help\" for help."));
                }
            } else {
                String chatmsg = "Client " + dataManager.getPlayerData(source.getId()).getName() + ": " + message.getMessage();
                System.out.println("[CHAT] " + chatmsg);
                server.broadcast(new TextMessage(chatmsg));
            }
        }
    }

}
