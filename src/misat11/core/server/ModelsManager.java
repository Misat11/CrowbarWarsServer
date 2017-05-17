/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server;

import java.util.HashMap;
import misat11.core.server.messages.ModelInfo;

/**
 *
 * @author misat11
 */
public class ModelsManager {

    private HashMap<String, ModelInfo> models = new HashMap<String, ModelInfo>();

    public ModelsManager() {
        //INIT DEFAULT MODELS
        models.put("Misat11/WomanModel1", new ModelInfo("WomanModel1", "Misat11", "https://github.com/Misat11/CrowbarWars/blob/master/assets/Models/womanmodel.j3o?raw=true", 1));
        models.put("Misat11/TestMap", new ModelInfo("TestMap", "Misat11", "https://github.com/Misat11/CrowbarWars/blob/master/assets/Scenes/Region0/main.j3o?raw=true", 1));
    }

    public boolean containsModel(String name) {
        return models.containsKey(name);
    }

    public ModelInfo getModel(String name) {
        return models.get(name);
    }

    public void addModel(String name, ModelInfo model) {
        models.put(name, model);
    }

    public HashMap<String, ModelInfo> getModelList() {
        return models;
    }

}
