package org.gammf.collabora_android.communication.update.modules;

import org.gammf.collabora_android.communication.update.general.UpdateMessage;
import org.gammf.collabora_android.modules.Module;

/**
 * A simple interface that represents a message containing an update operation carried out on a {@link Module}.
 */
public interface ModuleUpdateMessage extends UpdateMessage {

    /**
     * @return the updated {@link Module}.
     */
    Module getModule();

}
