/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.objects;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author misat11
 */
public class Entity {

    private Spatial spatial = new Node();
    private String assetUrl = "";
    private Vector3f location = null;
    private Vector3f spawnlocation = null;
    private float mass = 0f;
    private String headText = "";
    private boolean spawned = false;
    private String actualAnim = "Stand";
    private float health = 100f;

    public Entity(String assetUrl, Vector3f location, float mass, String headText) {
        this.assetUrl = assetUrl;
        this.location = location.clone();
        this.spawnlocation = location.clone();
        this.mass = mass;
        this.headText = headText;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public String getHeadText() {
        return headText;
    }

    public void setHeadText(String headText) {
        this.headText = headText;
    }

    public Vector3f getSpawnlocation() {
        return spawnlocation;
    }

    public void setSpawnlocation(Vector3f spawnlocation) {
        this.spawnlocation = spawnlocation;
    }

    public boolean isSpawned() {
        return spawned;
    }

    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }

    public Spatial getSpatial() {
        return spatial;
    }

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }

    public String getActualAnim() {
        return actualAnim;
    }

    public void setActualAnim(String actualAnim) {
        this.actualAnim = actualAnim;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }
    
    public void deductHealth(float health) {
        this.health = this.health - health;
    }
    
    public void addHealth(float health) {
        this.health = this.health + health;
    }
}
