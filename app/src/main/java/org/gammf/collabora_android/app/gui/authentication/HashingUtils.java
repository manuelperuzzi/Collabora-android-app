package org.gammf.collabora_android.app.gui.authentication;

import com.loopj.android.http.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class that provides some hasing utils for hashing values.
 */
public final class HashingUtils {

    private static final String HASH_ALGORITHM = "SHA-256";

    private static final String CHARSET_NAME = "UTF-8";

    private HashingUtils() {}

    /**
     * Hash a string, using SHA-256 algorith.
     * @param value the string to be hashed
     * @return a {@link Base64} encoded string.
     */
    public static String hashString(final String value) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(value.getBytes(CHARSET_NAME));
            return Base64.encodeToString(digest.digest(), Base64.DEFAULT);
        } catch (final NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            // should never be catch any error. If the application enters here, nothing better to do than
            // stop it.
            System.exit(0);
            return "";
        }
    }
}
