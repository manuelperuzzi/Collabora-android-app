package org.gammf.collabora_android.utils.model;

/**
 * Simple exception thrown when an object instantiation does not contain all the mandatory parameters.
 */
public class MandatoryFieldMissingException extends Exception {

    private final String field;
    private final String object;

    public MandatoryFieldMissingException(final String field, final String object) {
        this.field = field;
        this.object = object;
    }

    @Override
    public String toString() {
        return "Field " + field + " is mandatory for the creation of a " + object + " object.\n" +
                super.toString();
    }

}
