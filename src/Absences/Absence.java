package Absences;

public class Absence {
    private int absenceId;
    private String absenceDate;
    private String absenceStatus;

    public Absence(int absenceId, String absenceDate, String absenceStatus) {
        this.absenceId = absenceId;
        this.absenceDate = absenceDate;
        this.absenceStatus = absenceStatus;
    }

    public int getAbsenceId() {
        return absenceId;
    }

    public String getAbsenceDate() {
        return absenceDate;
    }

    public String getAbsenceStatus() {
        return absenceStatus;
    }

    public void setAbsenceId(int absenceId) {
        this.absenceId = absenceId;
    }

    public void setAbsenceDate(String absenceDate) {
        this.absenceDate = absenceDate;
    }

    public void setAbsenceStatus(String absenceStatus) {
        this.absenceStatus = absenceStatus;
    }
}
