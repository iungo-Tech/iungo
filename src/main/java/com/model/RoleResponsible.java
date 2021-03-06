package com.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "role_responsible")
@NamedQueries({
        @NamedQuery(name = "RoleResponsible.getResponsibles", query ="SELECT o FROM RoleResponsible o JOIN o.child c WHERE c.roleId IN (:students) AND o.child.size > 0 ORDER BY o.userR.name, o.userR.surname, o.userR.secondSurname"),
        @NamedQuery(name = "RoleResponsible.getAll", query ="SELECT o.userR FROM RoleResponsible o"),
        @NamedQuery(name = "RoleResponsible.getAllRoleResponsible", query ="SELECT o FROM RoleResponsible o"),
        @NamedQuery(name = "RoleResponsible.getChildren", query ="SELECT o FROM RoleResponsible o LEFT JOIN FETCH o.child WHERE o.userR.userId =:id"),


})
public class RoleResponsible extends RoleClass {

    @ManyToMany(mappedBy="responsibles" , fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RoleStudent> child = new LinkedList<>();

    public RoleResponsible() { }

    public List<RoleStudent> getChild() { return child; }

    public void setChild(List<RoleStudent> child) { this.child = child; }

    public void addChild(RoleStudent child){
        this.child.remove(child);
        this.child.add(child);
    }
}
