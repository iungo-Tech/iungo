package com.model;

import com.model.enums.TicketStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import static com.model.enums.TicketStatus.CREATED;

@Entity
@Table(name = "tickets")
@NamedQueries({
        @NamedQuery(name = "Ticket.findAll", query = "SELECT t FROM Ticket t"),
        @NamedQuery(name = "Ticket.findById", query = "SELECT t FROM Ticket t WHERE t.ticketId = :id"),
        @NamedQuery(name = "Ticket.findAllCreatedOngoing", query ="SELECT t FROM Ticket t WHERE t.status=1 OR t.status=0"),

})
public class Ticket {

    private static final long serialVersionUID = 2681531852204068105L;

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "TICKET_ID")
    private String ticketId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    //@Column("ATTACHED_FILES")
    //@OneToMany
    //private List<String> filePaths;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "STATUS")
    private TicketStatus status;

    @Column(name = "AD_RESPONSE")
    private String adminResponse = "";



    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "USER_ID")
    private User userT;

    public Ticket(){}

    public Ticket(String title, String description, User users) {
        this.title = title;
        this.description = description;
        this.userT = users;
        //this.creationDate = new Date();
        this.status = CREATED;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public User getuser() {
        return userT;
    }

    public void setuser(User users) {
        this.userT = users;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return creationDate.equals(ticket.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, title, description, creationDate, userT);
    }
}
