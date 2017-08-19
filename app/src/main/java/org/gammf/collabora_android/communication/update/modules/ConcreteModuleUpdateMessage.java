package org.gammf.collabora_android.communication.update.modules;

import org.gammf.collabora_android.communication.update.general.AbstractUpdateMessage;
import org.gammf.collabora_android.communication.update.general.UpdateMessageTarget;
import org.gammf.collabora_android.communication.update.general.UpdateMessageType;
import org.gammf.collabora_android.modules.Module;

/**
 * @author ManuelPeruzzi
 * A concrete message representing an update in a module.
 */
public class ConcreteModuleUpdateMessage extends AbstractUpdateMessage implements ModuleUpdateMessage {

    private final Module module;

    public ConcreteModuleUpdateMessage(final String username, final Module module,
                                       final UpdateMessageType updateType, final String collaborationId) {
        super(username, updateType, collaborationId);
        this.module = module;
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
