package com.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@NamedQueries({
        /*@NamedQuery(name = "Task.findByUserAndTask", query = "SELECT c FROM Task c WHERE c.user = :user AND c.task = :conversation"),*/
        @NamedQuery(name = "UserTask.findByUserAndSubject", query = "SELECT c FROM UserTask c where c.task.chapter.subject.subjectId =: subjectId"),


})
public class UserTask implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private RoleStudent student;

    @Id
    @ManyToOne
    @JoinColumn
    private Task task;

    @Column(name = "GRADE")
    private float grade;

    @Column(name = "OBSERVATIONS")
    private String observations;


    public UserTask(){}

    public RoleStudent getStudent() { return student; }

    public void setStudent(RoleStudent student) { this.student = student; }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTask that = (UserTask) o;
        return Float.compare(that.grade, grade) == 0 &&
                Objects.equals(student, that.student) &&
                Objects.equals(task, that.task) &&
                Objects.equals(observations, that.observations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, task, grade, observations);
    }
}