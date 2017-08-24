package org.gammf.collabora_android.app.gui;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class DataModel {

    public int icon;
    public String name;
    public boolean isModule;

    // Constructor for notes.
    public DataModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
        this.isModule = false;
    }
    //Constructor for modules.
    public DataModel (int icon, String name, boolean isModule){
        this.icon = icon;
        this.name = name;
        this.isModule = isModule;
    }
    public int getIcon(){
        return this.icon;
    }

    public String getName(){
        return this.name;
    }

    public boolean getIfIsModule(){
        return this.isModule;
    }
}
