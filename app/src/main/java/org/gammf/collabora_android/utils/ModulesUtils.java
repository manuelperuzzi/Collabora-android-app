package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Manuel Peruzzi
 * Utily class providing methods to convert from module class to json message and vice versa.
 */
public class ModulesUtils {

    /**
     * Provides a json with all the module information.
     * @param module the module.
     * @return a json message with all the module information.
     * @throws JSONException if the conversion went wrong.
     */
    public static JSONObject moduleToJson(final Module module) throws JSONException {
        final JSONObject json = new JSONObject();

        json.put("id", module.getId());
        json.put("description", module.getDescription());
        json.put("state", module.getStateDefinition());

        return json;
    }

    /**
     * Creates a module class from a json message.
     * @param json the input json message.
     * @return a module built from the json message.
     * @throws JSONException if the conversion went wrong.
     */
    public static Module jsonToModule(final JSONObject json) throws JSONException {
        final String id = json.getString("id");
        final String description = json.getString("description");
        final String state = json.getString("state");

        return new ConcreteModule(id, description, state);
    }

}
