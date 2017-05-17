/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.objects;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author misat11
 */
public class WorldObject {

    private String assetUrl;
    private Spatial spatial = new Node();
    private Spatial sended_spatial;
    private boolean wspatial = false;
    private Vector3f location;
    private Quaternion rotation;
    private Vector3f spawn_location;
    private Quaternion spawn_rotation;
    private float mass = 0f;
    private boolean spawned = false;

    public WorldObject(String assetUrl, Vector3f location, Quaternion rotation, float mass) {
        this.assetUrl = assetUrl;
        this.location = location;
        this.rotation = rotation;
        this.spawn_location = location;
        this.spawn_rotation = rotation;
        this.mass = mass;
    }
    
    public WorldObject(Spatial spatial, Vector3f location, Quaternion rotation, float mass){
        this.sended_spatial = spatial;
        this.wspatial = true;
        this.location = location;
        this.rotation = rotation;
        this.spawn_location = location;
        this.spawn_rotation = rotation;
        this.mass = mass;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public boolean isWspatial() {
        return wspatial;
    }

    public void setWspatial(boolean wspatial) {
        this.wspatial = wspatial;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public void setRotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Vector3f getSpawn_location() {
        return spawn_location;
    }

    public void setSpawn_location(Vector3f spawn_location) {
        this.spawn_location = spawn_location;
    }

    public Quaternion getSpawn_rotation() {
        return spawn_rotation;
    }

    public void setSpawn_rotation(Quaternion spawn_rotation) {
        this.spawn_rotation = spawn_rotation;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public Spatial getSended_spatial() {
        return sended_spatial;
    }

    public void setSended_spatial(Spatial sended_spatial) {
        this.sended_spatial = sended_spatial;
    }
    
    
}
