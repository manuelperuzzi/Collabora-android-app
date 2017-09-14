package org.gammf.collabora_android.utils;

import org.gammf.collabora_android.app.utils.ExceptionManager;
import org.gammf.collabora_android.users.SimpleUser;
import org.gammf.collabora_android.users.User;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class providing methods to convert a {@link User} to a json message and vice versa.
 */
public class UserUtils {

    /**
     * Provides a json with all the {@link User} information.
     * @param user the {@link User}.
     * @return a json message with all the {@link User} information.
     */
    public static JSONObject userToJson(final User user) {
        final JSONObject json = new JSONObject();
        try {
            json.put("username", user.getUsername());
            json.put("email", user.getEmail());
            json.put("name", user.getName());
            json.put("surname", user.getSurname());
            json.put("birthday", user.getBirthday());
        } catch(final JSONException e) {
            ExceptionManager.getInstance().handle(e);
        }
        return json;
    }

    /**
     * Create a {@link User} class from a json message.
     * @param json the input json message.
     * @return a {@link User} built from the json message.
     */
    public static User jsonToUser(final JSONObject json) {
        User user = null;
        try {
            final SimpleUser.Builder userBuilder = new SimpleUser.Builder();
            userBuilder.username(json.getString("username"));
            userBuilder.email(json.getString("email"));
            userBuilder.name(json.getString("name"));
            userBuilder.surname(json.getString("surname"));
            userBuilder.birthday(new DateTime(json.get("birthday")));
            user = userBuilder.build();
        } catch (final Exception e) {
            ExceptionManager.getInstance().handle(e);
        }
        return user;
    }
}
