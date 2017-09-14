package org.gammf.collabora_android.notes;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Manuel Peruzzi
 * Simple class representing a note contained in a module.
 */
public class SimpleModuleNote implements ModuleNote {

    private final Note note;
    private final String moduleId;

    /**
     * Class constructor.
     * @param note the single note.
     * @param moduleId the identifier of the module that contains the note.
     */
    public SimpleModuleNote(final Note note, final String moduleId) {
        this.note = note;
        this.moduleId = moduleId;
    }

    @Override
    public String getNoteID() {
        return note.getNoteID();
    }

    @Override
    public String getContent() {
        return note.getContent();
    }

    @Override
    public Location getLocation() {
        return note.getLocation();
    }

    @Override
    public DateTime getExpirationDate() {
        return note.getExpirationDate();
    }

    @Override
    public State getState() {
        return note.getState();
    }

    @Override
    public List<String> getPreviousNotes() {
        return note.getPreviousNotes();
    }

    @Override
    public void setContent(String newContent) {
        note.setContent(newContent);
    }

    @Override
    public void setLocation(Location location) {
        this.note.setLocation(location);
    }

    @Override
    public void setExpirationDate(DateTime expirationDate) {
        this.note.setExpirationDate(expirationDate);
    }

    @Override
    public void setState(State state) {
        this.note.setState(state);
    }

    @Override
    public void setPreviousNotes(List<String> previousNotes) {
        this.note.setPreviousNotes(previousNotes);
    }

    @Override
    public Note getNote() {
        return note;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleModuleNote that = (SimpleModuleNote) o;

        return note != null ? note.equals(that.note) : that.note == null;

    }

    @Override
    public int hashCode() {
        return note != null ? note.hashCode() : 0;
    }
}
