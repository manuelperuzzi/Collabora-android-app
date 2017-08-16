package org.gammf.collabora_android.communication.update.modules;

import org.gammf.collabora_android.modules.Module;

/**
 * @author Manuel Peruzzi
 * A simple interface that represents a message containing an update in a module.
 */
public interface ModuleUpdateMessage {

    /**
     * @return the updated module.
     */
    Module getModule();

}
