package com.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQueries({
        /*@NamedQuery(name = "Task.findByUserAndTask", query = "SELECT c FROM Task c WHERE c.user = :user AND c.task = :conversation"),*/


})
public class UserSubject implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    private RoleStudent student;

    @Id
    @ManyToOne
    @JoinColumn
    private Subject subject;

    @Column(name = "GRADE")
    private float grade;

    @Column(name = "OBSERVATIONS")
    private String observations;


    public UserSubject(){}

    public RoleStudent getStudent() { return student; }

    public void setStudent(RoleStudent student) { this.student = student; }

    public Subject getSubject() { return subject; }

    public void setSubject(Subject subject) { this.subject = subject; }

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
        UserSubject that = (UserSubject) o;
        return Float.compare(that.grade, grade) == 0 &&
                Objects.equals(student, that.student) &&
                Objects.equals(subject, that.subject) &&
                Objects.equals(observations, that.observations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, subject, grade, observations);
    }
}