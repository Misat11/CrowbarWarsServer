/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import misat11.core.server.messages.ServerInfoMessage;
import misat11.core.server.messages.TextMessage;
import com.jme3.network.serializing.Serializer;
import misat11.core.server.messages.CameraLookMessage;
import misat11.core.server.messages.ChangeEntityLocationMessage;
import misat11.core.server.messages.ChangeObjectLocationMessage;
import misat11.core.server.messages.ClientHasMessage;
import misat11.core.server.messages.DespawnEntityMessage;
import misat11.core.server.messages.DespawnObjectMessage;
import misat11.core.server.messages.HealthBarUpdateMessage;
import misat11.core.server.messages.ModelInfo;
import misat11.core.server.messages.MoveMessage;
import misat11.core.server.messages.PlayerSettingsMessage;
import misat11.core.server.messages.ServerWantClientHasMessage;
import misat11.core.server.messages.SpawnEntityMessage;
import misat11.core.server.messages.SpawnObjectMessage;
import misat11.core.server.messages.guis.AbstractGuiElement;
import misat11.core.server.messages.guis.CloseGuiMessage;
import misat11.core.server.messages.guis.QuadElement;
import misat11.core.server.messages.guis.Gui;
import misat11.core.server.messages.guis.OpenGuiMessage;
import misat11.core.server.messages.guis.TextElement;
import misat11.core.server.messages.guis.TextWCounterElement;

/**
 *
 * @author misat11
 */
public class Utils {

    public static final int PORT = 4444;
    public static final int PROTOCOL = 5;
    public static final String GAMEHASHCODE = "crbar_multi";
    
    public static void initSerializer() {
        Serializer.registerClass(TextMessage.class);
        Serializer.registerClass(ServerInfoMessage.class);
        Serializer.registerClass(MoveMessage.class);
        Serializer.registerClass(PlayerSettingsMessage.class);
        Serializer.registerClass(ChangeEntityLocationMessage.class);
        Serializer.registerClass(ChangeObjectLocationMessage.class);
        Serializer.registerClass(SpawnObjectMessage.class);
        Serializer.registerClass(SpawnEntityMessage.class);
        Serializer.registerClass(DespawnObjectMessage.class);
        Serializer.registerClass(DespawnEntityMessage.class);
        Serializer.registerClass(ClientHasMessage.class);
        Serializer.registerClass(CameraLookMessage.class);
        Serializer.registerClass(ServerWantClientHasMessage.class);
        Serializer.registerClass(ModelInfo.class);
        Serializer.registerClass(Gui.class);
        Serializer.registerClass(AbstractGuiElement.class);
        Serializer.registerClass(OpenGuiMessage.class);
        Serializer.registerClass(CloseGuiMessage.class);
        Serializer.registerClass(TextElement.class);        
        Serializer.registerClass(TextWCounterElement.class);
        Serializer.registerClass(QuadElement.class);
        Serializer.registerClass(HealthBarUpdateMessage.class);
    }
    
    public static Gui getRespawnGui(int time_in_seconds) {
        Gui gui = new Gui(100);
        gui.addElement(new TextWCounterElement("You will respawned in {counter}!", 40, 40, 20, 15, 1, time_in_seconds));
        gui.addElement(new QuadElement(0, 0, 100, 20, 1));
        gui.addElement(new QuadElement(0, 80, 100, 20, 1));
        return gui;
    }
}
