package com.dao;

import com.model.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class GroupDaoImpl implements GroupDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void addGroup(Group group){
        System.out.println("groupCreation");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.save(group);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public List<Group> getQueryResults(String query){
        Session session = sessionFactory.openSession();
        List<Group> groups = session.createQuery(query).list();
        return groups;
    }
}