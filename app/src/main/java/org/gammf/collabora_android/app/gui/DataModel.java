package org.gammf.collabora_android.app.gui;

/**
 * Created by anupamchugh on 10/12/15.
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
