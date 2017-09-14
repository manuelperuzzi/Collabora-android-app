package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.modules.ConcreteModule;
import org.gammf.collabora_android.modules.Module;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class providing methods to convert from a {@link Module} object to a json object and vice versa.
 */
public class ModulesUtils {

    /**
     * Provides a json with all the {@link Module} information.
     * @param module the {@link Module} to be converted.
     * @return a json object with all the {@link Module} information.
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
     * Creates a {@link Module} object from a json object.
     * @param json the input json object.
     * @return a {@link Module} built from the json object.
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
