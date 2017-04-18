/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import misat11.core.server.messages.PlayerListMessage;
import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.JoinLeaveMessage;
import misat11.core.server.messages.TextMessage;
import misat11.core.server.messages.PlayerDataMessage;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author misat11
 */
public class Utils {
    public static final int PORT = 4444;
    public static final int PROTOCOL = 2;
    public static final String GAMEHASHCODE = "crbar_multi";
    
    public static void initSerializer(){
        Serializer.registerClass(TextMessage.class);
        Serializer.registerClass(PlayerData.class);
        Serializer.registerClass(PlayerDataMessage.class);
        Serializer.registerClass(ServerInfoMessage.class);
        Serializer.registerClass(PlayerListMessage.class);
        Serializer.registerClass(JoinLeaveMessage.class);
    }
}
