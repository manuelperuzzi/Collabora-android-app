package org.gammf.collabora_android.app.gui;

/**
 * Created by Mattia on 25/08/2017.
 */

public class CollaborationDataModelDrawer {

    private String collaborationId;
    private String collaborationName;

    public CollaborationDataModelDrawer(String id, String name){
        this.collaborationId = id;
        this.collaborationName = name;
    }

    public String getCollaborationId(){
        return this.collaborationId;
    }

    public String getCollaborationName(){
        return this.collaborationName;
    }
    /*
    public void setCollaborationId(String id){
        this.collaborationId = id;
    }

    public void setCollaborationName(String name){
        this.collaborationName = name;
    }
    */

}
