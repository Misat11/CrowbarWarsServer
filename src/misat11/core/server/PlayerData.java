/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

/**
 *
 * @author misat11
 */
public class PlayerData {

    private String username;
    private String assetUrl;
    
    public PlayerData(String username, String assetUrl) {
        this.username = username;
        this.assetUrl = assetUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }
    
}
