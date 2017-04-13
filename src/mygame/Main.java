package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.basecommands.Help;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public Server server;
    public ServerDataManager dataManager;
    public ServerInfoMessage serverInfoMessage;
    public HashMap<String, Command> commands;

    public static void main(String[] args) {
        Utils.initSerializer();

        Main app = new Main();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        (new InputThread(this)).start();
        commands = new HashMap<String, Command>();
        dataManager = new ServerDataManager();
        serverInfoMessage = new ServerInfoMessage("Scenes/Region0/main.j3o", "Test Server", Utils.PROTOCOL, Utils.GAMEHASHCODE);
        this.registerCommand("help", new Help(this));
        initServer(Utils.PORT);
    }

    private void initServer(int port) {
        try {
            server = Network.createServer(port);
            server.addMessageListener(new ServerListener(this, server, dataManager));
            server.addConnectionListener(new ClientConnectionListener(serverInfoMessage, dataManager));
            server.start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {

        server.broadcast(new PlayerListMessage(dataManager.getPlayerList()));
        dataManager.getPlayerList().size();

    }

    public boolean registerCommand(String prefix, Command command) {
        if (commands.containsKey(prefix)) {
            return false;
        } else {
            commands.put(prefix, command);
            return true;
        }
    }
}
