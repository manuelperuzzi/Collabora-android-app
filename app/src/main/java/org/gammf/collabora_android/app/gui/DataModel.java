package org.gammf.collabora_android.app.gui;

/**
 * Created by @MattiaOriani on 12/08/2017
 */
public class DataModel {

    public int icon;
    public String name;

    // Constructor.
    public DataModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getName(){
        return this.name;
    }
}
