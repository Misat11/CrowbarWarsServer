package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.basecommands.Deop;
import mygame.basecommands.Help;
import mygame.basecommands.Kick;
import mygame.basecommands.Op;
import mygame.basecommands.Say;
import mygame.basecommands.Stop;

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
    public PlayerPermissions permissions;

    public static void main(String[] args) {
        Utils.initSerializer();

        Main app = new Main();
        app.start(JmeContext.Type.Headless);
    }

    @Override
    public void simpleInitApp() {
        
        // CONSOLE INPUT THREAD START
        
        (new InputThread(this)).start();
        
        // VALUES INIT
        
        commands = new HashMap<String, Command>();
        permissions = new PlayerPermissions();
        dataManager = new ServerDataManager();
        serverInfoMessage = new ServerInfoMessage("Scenes/Region0/main.j3o", "Test Server", Utils.PROTOCOL, Utils.GAMEHASHCODE);
        
        // SERVER INIT
        
        initServer(Utils.PORT);
        
        // COMMANDS INIT
        
        registerCommand("help", new Help(this, dataManager, permissions));
        registerCommand("say", new Say(server));
        registerCommand("stop", new Stop(server, this));
        registerCommand("op", new Op(permissions));
        registerCommand("deop", new Deop(permissions));
        registerCommand("kick", new Kick(dataManager, server));
    }

    private void initServer(int port) {
        try {
            System.out.println("[INFO] Starting server ["+serverInfoMessage.getServerName()+"] on port "+port+" (Protocol: "+Utils.PROTOCOL+", GameHashCode: "+Utils.GAMEHASHCODE+")");
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
