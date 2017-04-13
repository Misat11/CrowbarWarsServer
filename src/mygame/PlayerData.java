/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author misat11
 */
@Serializable
public class PlayerData {
    private int id;
    private String playername;
    private Vector3f location;
    private Quaternion rotation;
    
    private String modelAsset;
    private int health = 100;
    
    public PlayerData() {}
    
    public PlayerData(int i, String name, Vector3f loc, Quaternion rot, String model, int h){
        this.id = i;
        this.playername = name;
        this.location = loc;
        this.rotation = rot;
        this.modelAsset = model;
        this.health = h;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getName(){
        return this.playername;
    }
    
    public Vector3f getLocation(){
        return this.location;
    }
    
    public void setLocation(Vector3f l){
        this.location = l;
    }
    
    public Quaternion getRotation(){
        return this.rotation;
    }
    
    public void setRotation(Quaternion q){
        this.rotation = q;
    }
    
    public String getModelAsset(){
        return this.modelAsset;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public void setHealth(int h){
        this.health = h;
    }
}
