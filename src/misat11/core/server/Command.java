/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import com.jme3.network.HostedConnection;

/**
 *
 * @author misat11
 */
public interface Command {
    public PermissionLevel getPermissionlevel();
    
    public void call(HostedConnection source, String m);
    
    public void callFromServer(String m);
    
    public String getDescription();
    
}
