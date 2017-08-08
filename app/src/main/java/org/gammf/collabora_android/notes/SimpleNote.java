package org.gammf.collabora_android.notes;

import java.util.Date;
import java.util.List;

/**
 * Created by Alfredo on 05/08/2017.
 */

public class SimpleNote implements Note {
    private String noteID;
    private String username;
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
    private Date expirationDate;
    private String state;
    private String stateResponsible;
    private final List<String> previousNotes;

    SimpleNote(final String noteID,
               final String username,
               final String title,
               final String content,
               final Double latitude,
               final Double longitude,
               final Date expirationDate,
               final String state,
               final String stateResponsible,
               final List<String> previousNotes) {
        this.noteID = noteID;
        this.username = username;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.expirationDate = expirationDate;
        this.state = state;
        this.stateResponsible = stateResponsible;
        this.previousNotes = previousNotes;
    }


    @Override
    public String getNoteID() {
        return this.noteID;
    }

    @Override
    public String getUsername() {
        return this.username;
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
    public Double getLatitude() {
        return this.latitude;
    }

    @Override
    public Double getLongitude() {
        return this.longitude;
    }

    @Override
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    @Override
    public String getState() {
        return this.state;
    }

    @Override
    public String getStateResponsible() {
        return this.stateResponsible;
    }

    @Override
    public List<String> getPreviousNotes() {
        return this.previousNotes;
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
