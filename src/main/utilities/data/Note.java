package main.utilities.data;

public class Note {
    private int noteId;
    private int subjectId;
    private String login;
    private double value;
    private String type;
    private String date;
    private String comment;


    public Note(int noteId, int subjectId, String login, double value, String type, String date, String comment) {
        this.noteId = noteId;
        this.subjectId = subjectId;
        this.login = login;
        this.value = value;
        this.type = type;
        this.date = date;
        this.comment = comment;
    }

    public int getNoteId() {
        return noteId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getLogin() {
        return login;
    }

    public double getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

