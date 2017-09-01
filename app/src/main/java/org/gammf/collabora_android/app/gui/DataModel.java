package org.gammf.collabora_android.app.gui;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class DataModel {

    private int icon;
    private String id, name;
    private boolean isModule;

    public DataModel (int icon, String id, String name, boolean isModule) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.isModule = isModule;
    }
    public int getIcon(){
        return this.icon;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIfIsModule(){
        return this.isModule;
    }
}
