/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import com.jme3.math.Vector3f;
import com.jme3.network.Server;
import java.util.HashMap;
import misat11.core.server.messages.CameraLookMessage;
import misat11.core.server.messages.MoveMessage;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.objects.Entity;

/**
 *
 * @author misat11
 */
public class PlayersManager {

    private ServerDataStorage serverDataStorage;
    private Server server;
    private HashMap<Integer, PlayerData> players = new HashMap<Integer, PlayerData>();
    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private HashMap<Integer, Integer> entities_id = new HashMap<Integer, Integer>();
    private HashMap<String, Integer> playernames_db = new HashMap<String, Integer>();
    private Vector3f spawn_loc = new Vector3f(0f, 60f, 0f);

    public PlayersManager(Server server, ServerDataStorage serverDataStorage) {
        this.serverDataStorage = serverDataStorage;
        this.server = server;
    }

    public void addPlayer(int conn_id, PlayerSettingsMessage msg) {
        PlayerData data = new PlayerData(msg.getName(), "Models/womanmodel.j3o");
        addPlayer(conn_id, data);
    }

    public void addPlayer(int conn_id, PlayerData data) {
        playernames_db.put(data.getUsername(), conn_id);
        players.put(conn_id, data);
        Entity entity = new Entity(data.getAssetUrl(), spawn_loc, 40f, data.getUsername());
        entities.put(conn_id, entity);
        spawnPlayer(conn_id);
    }

    public void removePlayer(int conn_id) {
        despawnPlayer(conn_id);
        if (entities_id.containsKey(conn_id)) {
            entities_id.remove(conn_id);
        }
        if (entities.containsKey(conn_id)) {
            entities.remove(conn_id);
        }
        if (playernames_db.containsKey(players.get(conn_id).getUsername())) {
            playernames_db.remove(players.get(conn_id).getUsername());
        }
        if (players.containsKey(conn_id)) {
            players.remove(conn_id);
        }
    }

    public void spawnPlayer(int conn_id) {
        if (entities.containsKey(conn_id) && players.containsKey(conn_id)) {
            if (entities.get(conn_id).isSpawned() == false) {
                entities_id.put(conn_id, serverDataStorage.spawnEntity(entities.get(conn_id)));
            }
        }
    }

    public void despawnPlayer(int conn_id) {
        if (entities.containsKey(conn_id) && players.containsKey(conn_id) && entities_id.containsKey(conn_id)) {
            if (entities.get(conn_id).isSpawned() == true) {
                serverDataStorage.despawnEntity(entities_id.get(conn_id));
                entities_id.remove(conn_id);
            }
        }
    }

    public void respawnPlayer(int conn_id) {
        if (entities.containsKey(conn_id) && players.containsKey(conn_id) && entities_id.containsKey(conn_id)) {
            if (entities.get(conn_id).isSpawned() == true) {
                serverDataStorage.respawnEntity(entities_id.get(conn_id));
            }
        }
    }

    public void teleportPlayer(int conn_id, Vector3f loc) {
        if (entities.containsKey(conn_id) && players.containsKey(conn_id) && entities_id.containsKey(conn_id)) {
            if (entities.get(conn_id).isSpawned() == true) {
                serverDataStorage.teleportEntity(conn_id, loc);
            }
        }
    }

    public int getPlayerId(String name) {
        return playernames_db.get(name);
    }

    public String getPlayerName(int id) {
        return players.get(id).getUsername();
    }

    public Vector3f getSpawn_loc() {
        return spawn_loc;
    }

    public void setSpawn_loc(Vector3f spawn_loc) {
        this.spawn_loc = spawn_loc;
    }

    public boolean isTherePlayerWithName(String name) {
        return playernames_db.containsKey(name);
    }

    public boolean isTherePlayerWithId(int id) {
        return players.containsKey(id);
    }

    public int getEntityId(int id) {
        return entities_id.get(id);
    }

    public void sendCameraMessages() {
        for (int id : players.keySet()) {
            if (entities.containsKey(id)) {
                server.getConnection(id).send(new CameraLookMessage(entities.get(id).getLocation()));
            }
        }
    }

    public void move(int id, MoveMessage msg) {
        if (msg.getId() == id && msg.isMoveValid() && entities.containsKey(id)) {
            serverDataStorage.moveEntity(entities_id.get(id), msg.isLeft(), msg.isRight(), msg.isUp(), msg.isDown(), msg.isJump(), msg.getDirCam(), msg.getLeftCam());
        }
    }

}
