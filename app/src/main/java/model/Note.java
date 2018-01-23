package model;

/**
 * Created by pritshah on 8/1/17.
 */

public class Note {

    private String note;
    private String title;

    public Note() {

    }

    public Note(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
