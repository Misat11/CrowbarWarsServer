/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Server;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashMap;
import misat11.core.server.gamelogic.LogicThread;
import misat11.core.server.gamelogic.states.EntityAppState;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;
import misat11.core.server.objects.WorldObject;
import misat11.core.server.objects.Entity;

/**
 *
 * @author misat11
 */
public class ServerDataStorage {
    
    private Server server;
    private BulletAppState bulletAppState;
    private LogicThread logicThread;
    
    private HashMap<Integer, WorldObject> objects = new HashMap<Integer, WorldObject>();
    private int lastobjectid = 0;
    
    private HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
    private HashMap<Integer, EntityAppState> entities_app_state = new HashMap<Integer, EntityAppState>();
    private HashMap<Integer, Integer> playerEntities = new HashMap<Integer, Integer>();
    private HashMap<Integer, CharacterControl> controls = new HashMap<Integer, CharacterControl>();
    private HashMap<Integer, Float> airTimes = new HashMap<Integer, Float>();
    private int lastentityid = 0;
    
    public ServerDataStorage(Server server, BulletAppState bulletAppState, LogicThread logicThread) {
        this.server = server;
        this.bulletAppState = bulletAppState;
        this.logicThread = logicThread;
    }
    
    public int spawnObject(WorldObject obj) {
        if (objects.containsValue(obj)) {
            return -1;
        }
        int id = lastobjectid++;
        objects.put(id, obj);
        spawnObj(id);
        return id;
    }
    
    public void despawnObject(int id) {
        if (objects.containsKey(id)) {
            despawnObj(id);
            objects.remove(id);
        }
    }
    
    public void respawnObject(int id) {
        if (objects.containsKey(id)) {
            respawnObj(id);
        }
    }
    
    public void teleportObject(int id, Vector3f newloc) {
        if (objects.containsKey(id)) {
            teleportObj(id, newloc, objects.get(id).getRotation());
        }
    }
    
    public void teleportObject(int id, Vector3f newloc, Quaternion newrot) {
        if (objects.containsKey(id)) {
            teleportObj(id, newloc, newrot);
        }
    }
    
    public void syncObjectData() {
        ArrayList<Integer> list = getObjectIdList();
        for (int id : list) {
            syncObjectData(id);
        }
    }
    
    private void syncObjectData(int id) {
        WorldObject obj = objects.get(id);
        if (objects.containsKey(id)) {
            RigidBodyControl control = obj.getSpatial().getControl(RigidBodyControl.class);
            obj.setLocation(control.getPhysicsLocation());
            obj.setRotation(control.getPhysicsRotation());
            server.broadcast(constructObjectLocationMessage(id));
        }
    }
    
    private void spawnObj(int id) {
        WorldObject obj = objects.get(id);
        if (obj.isSpawned() == false) {
            Spatial spatial = obj.getSpatial();
            RigidBodyControl control = new RigidBodyControl(obj.getMass());
            control.setPhysicsLocation(obj.getSpawn_location());
            control.setPhysicsRotation(obj.getSpawn_rotation());
            spatial.addControl(control);
            bulletAppState.getPhysicsSpace().addAll(spatial);
            obj.setSpawned(true);
            server.broadcast(constructObjectSpawnMessage(id));
        }
    }
    
    private void despawnObj(int id) {
        WorldObject obj = objects.get(id);
        if (obj.isSpawned() == true) {
            Spatial spatial = obj.getSpatial();
            bulletAppState.getPhysicsSpace().removeAll(spatial);
            obj.setSpawned(false);
            server.broadcast(constructObjectDespawnMessage(id));
        }
    }
    
    private void teleportObj(int id, Vector3f loc, Quaternion rot) {
        WorldObject obj = objects.get(id);
        if (obj.isSpawned() == true) {
            RigidBodyControl control = obj.getSpatial().getControl(RigidBodyControl.class);
            control.setPhysicsLocation(loc);
            control.setPhysicsRotation(rot);
            obj.setLocation(loc);
            obj.setRotation(rot);
        }
    }
    
    private void respawnObj(int id) {
        despawnObj(id);
        spawnObj(id);
    }
    
    public void syncEntityData() {
        ArrayList<Integer> list = getEntityIdList();
        for (int id : list) {
            syncEntityData(id);
        }
    }
    
    private void syncEntityData(int id) {
        Entity ent = entities.get(id);
        if (entities.containsKey(id)) {
            CharacterControl control = ent.getSpatial().getControl(CharacterControl.class);
            ent.setLocation(control.getPhysicsLocation());
            server.broadcast(constructEntityLocationMessage(id));
        }
    }
    
    public int spawnEntity(Entity ent) {
        if (entities.containsValue(ent)) {
            return -1;
        }
        int id = lastentityid++;
        entities.put(id, ent);
        entities_app_state.put(id, new EntityAppState(id, ent, this));
        logicThread.attachAppState(entities_app_state.get(id));
        spawnEnt(id);
        return id;
    }
    
    public void despawnEntity(int id) {
        if (entities.containsKey(id)) {
            logicThread.detachAppState(entities_app_state.get(id));
            despawnEnt(id);
            entities_app_state.remove(id);
            entities.remove(id);
        }
    }
    
    public void respawnEntity(int id) {
        if (entities.containsKey(id)) {
            respawnEnt(id);
        }
    }
    
    public void teleportEntity(int id, Vector3f newloc) {
        if (entities.containsKey(id)) {
            teleportEnt(id, newloc);
        }
    }
    
    private void spawnEnt(int id) {
        Entity ent = entities.get(id);
        if (ent.isSpawned() == false) {
            Spatial spatial = ent.getSpatial();
            CapsuleCollisionShape collisionShape = new CapsuleCollisionShape(0.6f, 2f);
            CharacterControl control = new CharacterControl(collisionShape, 0.09f);
            controls.put(id, control);
            spatial.addControl(control);
            bulletAppState.getPhysicsSpace().add(control);
            ent.setSpawned(true);
            control.setGravity(ent.getMass());
            control.setJumpSpeed(15f);
            control.warp(ent.getSpawnlocation());
            server.broadcast(constructEntitySpawnMessage(id));
        }
    }
    
    private void despawnEnt(int id) {
        Entity ent = entities.get(id);
        if (ent.isSpawned() == true) {
            ent.setSpawned(false);
            bulletAppState.getPhysicsSpace().remove(controls.get(id));
            ent.getSpatial().removeControl(controls.get(id));
            controls.remove(id);
            server.broadcast(constructEntityDespawnMessage(id));
        }
        
    }
    
    private void teleportEnt(int id, Vector3f loc) {
        Entity ent = entities.get(id);
        if (ent.isSpawned() == true) {
            controls.get(id).warp(loc);
        }
    }
    
    private void respawnEnt(int id) {
        despawnEnt(id);
        spawnEnt(id);
    }
    
    public ArrayList<Integer> getEntityIdList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer key : entities.keySet()) {
            list.add(key);
        }
        return list;
    }
    
    public ArrayList<Integer> getObjectIdList() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Integer key : objects.keySet()) {
            list.add(key);
        }
        return list;
    }
    
    public float getAirTime(int id) {
        return airTimes.get(id);
    }
    
    public boolean containsAirTime(int id) {
        return airTimes.containsKey(id);
    }
    
    public void setAirTime(int id, float airTime) {
        airTimes.put(id, airTime);
    }
    
    public CharacterControl getControl(int id) {
        return controls.get(id);
    }
    
    public boolean containsControl(int id) {
        return controls.containsKey(id);
    }
    
    public void checkClientHasMessage(int id, ClientHasMessage msg) {
        ArrayList<Integer> objects_list = getObjectIdList();
        ArrayList<Integer> entities_list = getEntityIdList();
        for (int obj : objects_list) {
            if (!msg.getObject_ids().contains(obj)) {
                server.getConnection(id).send(constructObjectSpawnMessage(obj));
            }
        }
        for (int ent : entities_list) {
            if (!msg.getEntities_ids().contains(ent)) {
                server.getConnection(id).send(constructEntitySpawnMessage(ent));
            }
        }
        for (int obj : msg.getObject_ids()) {
            if (!objects_list.contains(obj)) {
                server.getConnection(id).send(constructObjectDespawnMessage(obj));
            }
        }
        for (int ent : msg.getEntities_ids()) {
            if (!entities_list.contains(id)) {
                server.getConnection(id).send(constructEntityDespawnMessage(ent));
            }
        }
    }
    
    public SpawnObjectMessage constructObjectSpawnMessage(int id) {
        if (objects.get(id).isWspatial()) {
            return new SpawnObjectMessage(id, objects.get(id).getSended_spatial(), objects.get(id).getLocation(), objects.get(id).getRotation(), objects.get(id).getMass());
        } else {
            return new SpawnObjectMessage(id, objects.get(id).getAssetUrl(), objects.get(id).getLocation(), objects.get(id).getRotation(), objects.get(id).getMass());
        }
    }
    
    public SpawnEntityMessage constructEntitySpawnMessage(int id) {
        return new SpawnEntityMessage(id, entities.get(id).getAssetUrl(), entities.get(id).getLocation(), entities.get(id).getMass(), entities.get(id).getHeadText());
    }
    
    public DespawnObjectMessage constructObjectDespawnMessage(int id) {
        return new DespawnObjectMessage(id);
    }
    
    public DespawnEntityMessage constructEntityDespawnMessage(int id) {
        return new DespawnEntityMessage(id);
    }
    
    public ChangeEntityLocationMessage constructEntityLocationMessage(int id) {
        return new ChangeEntityLocationMessage(id, entities.get(id).getLocation(), entities.get(id).getSpatial().getControl(CharacterControl.class).getViewDirection(), entities.get(id).getActualAnim());
    }
    
    public ChangeObjectLocationMessage constructObjectLocationMessage(int id) {
        return new ChangeObjectLocationMessage(id, objects.get(id).getLocation(), objects.get(id).getRotation());
    }
    
    public Entity getEntity(int id){
        return entities.get(id);
    }
    
    public boolean containsEntity(int id){
        return entities.containsKey(id);
    }
    
    public Object getObject(int id){
        return objects.get(id);
    }
    
    public boolean containsObject(int id){
        return objects.containsKey(id);
    }
    
    public void moveEntity(int id, boolean left, boolean right, boolean up, boolean down, boolean jump, Vector3f camDir, Vector3f camLeft){
        if(entities_app_state.containsKey(id)){
            EntityAppState appState = entities_app_state.get(id);
            appState.setLeft(left);
            appState.setRight(right);
            appState.setUp(up);
            appState.setDown(down);
            appState.setJump(jump);
            appState.setCamDir(camDir);
            appState.setCamLeft(camLeft);
        }
    }
}
