package org.gammf.collabora_android.utils;


public class AuthenticationUtils {

    private static final String SERVERIP = "http://192.168.1.1";

    private static final String SERVERPORT = ":8080";

    public static final String GET = SERVERIP+SERVERPORT+"/login";

    public static final String POST = SERVERIP+SERVERPORT+"/signin";

}
