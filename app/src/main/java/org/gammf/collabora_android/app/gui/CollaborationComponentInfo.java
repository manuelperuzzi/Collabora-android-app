package org.gammf.collabora_android.app.gui;

import org.gammf.collabora_android.app.R;

/**
 * Simple data structure used to store information about collaboration components to be visualized on GUI.
 */
public class CollaborationComponentInfo implements CollaborationComponent{

    private final String id;
    private final String content;
    private final CollaborationComponentType componentType;
    private final String additionalInfo;

    public CollaborationComponentInfo(final String id,final String content, final CollaborationComponentType componentType, String additionalInfo) {
        this.id = id;
        this.content = content;
        this.componentType =  componentType;
        this.additionalInfo = additionalInfo;
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

    @Override
    public String getAdditionalInfo() {
        return this.additionalInfo;
    }


}
