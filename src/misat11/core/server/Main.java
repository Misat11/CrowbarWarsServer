package misat11.core.server;

import misat11.core.server.messages.ServerInfoMessage;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import misat11.core.server.basecommands.Deop;
import misat11.core.server.basecommands.Help;
import misat11.core.server.basecommands.Kick;
import misat11.core.server.basecommands.Op;
import misat11.core.server.basecommands.Say;
import misat11.core.server.basecommands.Stop;
import misat11.core.server.gamelogic.LogicThread;
import misat11.core.server.gamelogic.states.CamerasUpdaterAppState;
import misat11.core.server.gamelogic.states.EntitiesUpdaterAppState;
import misat11.core.server.gamelogic.states.ObjectsUpdaterAppState;

public class Main extends SimpleApplication {

    public Server server;
    public ServerInfoMessage serverInfoMessage;
    public HashMap<String, Command> commands;
    public PlayerPermissions permissions;
    private LogicThread logic;
    private ScheduledExecutorService exec;
    private BulletAppState bulletAppState;
    private ServerDataStorage serverDataStorage;
    private PlayersManager playersManager;
    private ModelsManager modelsManager;
    
    private EntitiesUpdaterAppState entitiesUpdaterAppState;
    private ObjectsUpdaterAppState objectsUpdaterAppState;
    private CamerasUpdaterAppState camerasUpdaterAppState;

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
        System.out.println("Loading, please wait...");
        try {
            server = Network.createServer(Utils.GAMEHASHCODE, Utils.PROTOCOL, Utils.PORT, Utils.PORT);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        commands = new HashMap<String, Command>();
        logic = new LogicThread(this);
        modelsManager = new ModelsManager();
        bulletAppState = new BulletAppState();
        serverDataStorage = new ServerDataStorage(server, bulletAppState, logic);
        playersManager = new PlayersManager(server, serverDataStorage);
        permissions = new PlayerPermissions();
        serverInfoMessage = new ServerInfoMessage("Misat11/TestMap", "Test Server", modelsManager.getModelList());

        // SERVER INIT
        System.out.println("[INFO] Starting server [" + serverInfoMessage.getServerName() + "] on port " + Utils.PORT + " (Protocol: " + Utils.PROTOCOL + ", GameHashCode: " + Utils.GAMEHASHCODE + ")");
        server.addMessageListener(new ServerListener(this, server, playersManager, serverDataStorage));
        server.addConnectionListener(new ClientConnectionListener(serverInfoMessage, playersManager));

        try {
            server.start();
        } catch (Exception e) {
            System.out.println("[ERROR] "+e.getLocalizedMessage());
            System.out.println("[ERROR] Server is not started. Maybe something running on this port. Closing...");
            destroy();
            System.exit(0);
        }

        // LOAD PHYSIC
        stateManager.attach(bulletAppState);

        // LOAD TERRAIN PHYSICS
        Spatial terrain = assetManager.loadModel("Scenes/Region0/main.j3o");
        RigidBodyControl terrainControl = new RigidBodyControl(0.0f);
        terrain.addControl(terrainControl);
        bulletAppState.getPhysicsSpace().addAll(terrain);

        // CREATE LOGIC THREAD
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(logic, 0, 100, TimeUnit.MILLISECONDS);

        entitiesUpdaterAppState = new EntitiesUpdaterAppState(serverDataStorage);
        objectsUpdaterAppState = new ObjectsUpdaterAppState(serverDataStorage);
        camerasUpdaterAppState = new CamerasUpdaterAppState(playersManager);
        
        logic.attachAppState(entitiesUpdaterAppState);
        logic.attachAppState(objectsUpdaterAppState);
        logic.attachAppState(camerasUpdaterAppState);

        // COMMANDS INIT
        registerCommand("help", new Help(this, playersManager, permissions));
        registerCommand("say", new Say(server));
        registerCommand("stop", new Stop(server, this));
        registerCommand("op", new Op(permissions));
        registerCommand("deop", new Deop(permissions));
        registerCommand("kick", new Kick(playersManager, server));

        System.out.println("Loading successfully, waiting for players");
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    public boolean registerCommand(String prefix, Command command) {
        if (commands.containsKey(prefix)) {
            return false;
        } else {
            commands.put(prefix, command);
            return true;
        }
    }

    public LogicThread getLogic() {
        return logic;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public ModelsManager getModelsManager() {
        return modelsManager;
    }
}
