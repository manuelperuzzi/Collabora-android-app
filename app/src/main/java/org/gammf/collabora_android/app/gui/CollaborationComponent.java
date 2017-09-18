package org.gammf.collabora_android.app.gui;

/**
 * Simple interface used to represent a collaboration component to be visualized on GUI.
 */

public interface CollaborationComponent {
    /**
     * Gets the component ID.
     * @return the component ID.
     */
    String getId();

    /**
     * Gets the component content.
     * @return the component content.
     */
    String getContent();

    /**
     * Gets the component type.
     * @return the component type.
     */
    CollaborationComponentType getType();

    /**
     * Gets the component icon.
     * @return the component icon.
     */
    Integer getIcon();

    /**
     * Gets an additional information about the component
     * @return the additional information
     */
    String getAdditionalInfo();
}
