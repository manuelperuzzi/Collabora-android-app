package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utily class providing methods to convert from module class to json message and vice versa.
 */
public class ModulesUtils {

    /**
     * Provides a json with all the module information.
     * @param module the module.
     * @return a json message with all the module information.
     */
    public static JSONObject moduleToJson(final Module module) {
        final JSONObject json = new JSONObject();
        try {
            json.put("id", module.getId());
            json.put("description", module.getDescription());
            json.put("state", module.getStateDefinition());
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return json;
    }

    /**
     * Creates a module class from a json message.
     * @param json the input json message.
     * @return a module built from the json message.
     */
    public static Module jsonToModule(final JSONObject json) {
        Module module = null;
        try {
            final String id = json.getString("id");
            final String description = json.getString("description");
            final String state = json.getString("state");
            module = new ConcreteModule(id, description, state);
        } catch (final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return module;
    }
}
