package org.gammf.collabora_android.notes;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNote implements Note {
    private final String userID;
    private String title;
    private String content;

    SimpleNote(final String userID, final String title, final String content) {
        this.userID = userID;
        this.title = title;
        this.content = content;
    }


    @Override
    public String getUserID() {
        return this.userID;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    @Override
    public void modifyTitle(String newTitle) {
        this.title = newTitle;
    }

    @Override
    public void modifyContent(String newContent) {
        this.content = newContent;
    }
}
