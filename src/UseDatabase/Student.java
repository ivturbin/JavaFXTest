package UseDatabase;

import java.sql.Date;
import java.time.LocalDate;

public class Student {
    private int id;
    private String name;
    private String secondName;
    private String lastName;
    private LocalDate birthDate;
    private boolean isEnrolled;
    private String Enrolled;
    private int groupId;
    Student(int id, String name, String secondName, String lastName, LocalDate birthDate, boolean isEnrolled, int groupId){
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.isEnrolled = isEnrolled;
        this.groupId = groupId;
        this.Enrolled = isEnrolled ? "Да" : "Нет";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public String getEnrolled() {
        return Enrolled;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
        this.Enrolled = enrolled ? "Да" : "Нет";
    }
}
