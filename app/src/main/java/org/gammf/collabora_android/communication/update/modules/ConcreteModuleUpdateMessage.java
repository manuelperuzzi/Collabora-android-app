package org.gammf.collabora_android.communication.update.modules;

import org.gammf.collabora_android.communication.common.MessageType;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.modules.Module;

/**
 * @author ManuelPeruzzi
 * A concrete message representing an update in a module.
 */
public class ConcreteModuleUpdateMessage implements ModuleUpdateMessage {

    private final String username;
    private final Module module;
    private final UpdateMessageType updateType;

    public ConcreteModuleUpdateMessage(final String username, final Module module, final UpdateMessageType updateType) {
        this.username = username;
        this.module = module;
        this.updateType = updateType;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.UPDATE;
    }

    @Override
    public UpdateMessageType getUpdateType() {
        return updateType;
    }

    @Override
    public UpdateMessageTarget getTarget() {
        return UpdateMessageTarget.MODULE;
    }

    @Override
    public Module getModule() {
        return module;
    }

}
