package org.gammf.collabora_android.utils.communication;

/**
 * Class containing all the information needed to contact the server correctly, with both GET or POST request.
 */
public class AuthenticationUtils {

    private static final String SERVER_IP = "http://192.168.0.19";

    private static final String SERVER_PORT = ":9894";

    public static final String GET = SERVER_IP + SERVER_PORT + "/login";

    public static final String POST = SERVER_IP + SERVER_PORT +"/signin";

}
