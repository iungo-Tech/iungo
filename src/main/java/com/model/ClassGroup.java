package com.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import com.model.enums.Stage;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
@NamedQueries({
        @NamedQuery(name = "ClassGroup.findAll", query = "SELECT g FROM ClassGroup g"),
        @NamedQuery(name = "ClassGroup.findById", query = "SELECT g FROM ClassGroup g WHERE g.groupId = :id"),
        @NamedQuery(name = "ClassGroup.findByIdWithStudents", query = "SELECT g FROM ClassGroup g LEFT JOIN FETCH g.students s WHERE g.groupId = :id"),
        @NamedQuery(name = "ClassGroup.findLevelsByStage", query = "SELECT g.level FROM ClassGroup g WHERE g.stage =:stage"),
        @NamedQuery(name = "ClassGroup.findGroupsByStageAndLevel", query = "SELECT g.group FROM ClassGroup g WHERE g.stage =:stage AND g.level =:level"),
        @NamedQuery(name = "ClassGroup.findGroupByStageAndLevelAndGroupAndCourse", query = "SELECT g FROM ClassGroup g WHERE g.stage =:stage AND g.level =:level AND g.group =:group AND g.course =:course"),



//             + "LEFT JOIN Tenant te ON te.room = r.id"
//             + "WHERE r.id = :id")

})
public class ClassGroup implements Serializable {

    private static final long serialVersionUID = 2681531852204068105L;
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "GROUP_ID")
    private String groupId;

    @Column(name = "NAME")
    //@NotNull
    private String name;

    @Column(name = "STAGE")
    //@NotNull
    private Stage stage;

    @Column(name = "LEVEL")
    //@NotNull
    private int level;

    @Column(name = "GROUP_VALUE")
    //@NotNull
    private String group;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @OneToMany(mappedBy="subjectGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> timeline;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoleStudent> students;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TUTOR_ID")
    private RoleTutor tutor;

    public ClassGroup() { }

    public ClassGroup(String name, Stage stage, int level, String group) {
        this.name = name;
        this.stage = stage;
        this.level = level;
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public List<RoleStudent> getStudents() {
        return students;
    }

    public void setStudents(List<RoleStudent> students) {
        this.students = students;
    }

    public RoleTutor getTutor() {
        return tutor;
    }

    public void setTutor(RoleTutor tutor) {
        this.tutor = tutor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassGroup group1 = (ClassGroup) o;
        return Objects.equals(groupId, group1.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name, stage, level, group);
    }
}
