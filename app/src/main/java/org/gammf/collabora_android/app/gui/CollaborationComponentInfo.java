package org.gammf.collabora_android.app.gui;

import org.gammf.collabora_android.app.R;

/**
 * @author Alfredo Maffi
 * Simple data structure used to store information about collaboration components to be visualized on GUI.
 */
public class CollaborationComponentInfo implements CollaborationComponent{

    private final String id;
    private final String content;
    private final CollaborationComponentType componentType;

    public CollaborationComponentInfo(final String id,final String content, final CollaborationComponentType componentType) {
        this.id = id;
        this.content = content;
        this.componentType =  componentType;
    }

    @Override
    public String getId(){
        return this.id;
    }

    @Override
    public String getContent(){
        return this.content;
    }

    @Override
    public CollaborationComponentType getType(){
        return this.componentType;
    }

    @Override
    public Integer getIcon() {
        switch (this.componentType) {
            case NOTE: return R.drawable.note_icon;
            case MODULE: return R.drawable.module32;
            case MEMBER: return R.drawable.user;
            default: return -1;
        }
    }


}
