package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 08/08/2017.
 */

public interface State {
    String getCurrentState();
    String getCurrentResponsible();
    void setState(String newState);
    void setResponsible(String newResponsibleUsername);
}
