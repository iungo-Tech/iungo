package com.dao;

import com.model.Authorities;
import com.model.Ticket;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TicketDaoImpl implements TicketDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addTicket(Ticket ticket){
        System.out.println("ticketCreation");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(ticket);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public void updateTicket(Ticket ticket){
        System.out.println("ticketUpdate");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(ticket);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public List<Ticket> getOngoingCreatedTickets(){
        Session session = sessionFactory.openSession();
        List<Ticket> tickets = session.getNamedQuery("Ticket.findAllCreatedOngoing").list();
        session.close();
        return tickets;
    }

    public Ticket getTicketById(String id){
        Session session = sessionFactory.openSession();
        Ticket tickets = (Ticket) session.getNamedQuery("Ticket.findById").setParameter("id", id).uniqueResult();
        session.close();
        return tickets;
    };
}
