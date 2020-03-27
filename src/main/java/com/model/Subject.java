package com.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subjects")
@NamedQueries({
//     @NamedQuery(name = "Room.findById", query = "SELECT r,te.email FROM Room r  "
//             + "LEFT JOIN Tenant te ON te.room = r.id"
//             + "WHERE r.id = :id")
        @NamedQuery(name = "Subject.findById", query = "SELECT r FROM Subject r WHERE r.subjectId = :id"),
        @NamedQuery(name = "Subject.findByIdWithAll", query ="SELECT s FROM Subject s JOIN FETCH s.timeline k JOIN FETCH s.subjectGroup d WHERE s.subjectId = :id"),

})
public class Subject implements Serializable {

    private static final long serialVersionUID = 2681531852204068105L;
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "SUBJECT_ID")
    private String subjectId;

    @Column(name = "NAME")
    //@NotNull
    private String name;

    @OneToMany(mappedBy="subjectTimeLine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeLine> timeline;

    @OneToMany(mappedBy="subjectEvent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Event> events;

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "GROUP_ID")
    private Group subjectGroup;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeLine> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimeLine> timeline) {
        this.timeline = timeline;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Group getSubjectGroup() {
        return subjectGroup;
    }

    public void setSubjectGroup(Group subjectGroup) {
        this.subjectGroup = subjectGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectId, subject.subjectId) &&
                Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId, name);
    }
}