package com.dao;

import com.model.Space;
import com.model.Task;
import com.model.Ticket;
import com.model.UserTask;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TaskDaoImpl implements TaskDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void addTask(Task task){
        System.out.println("ticketCreation");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(task);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    @Override
    public List<UserTask> getUserTaskByUserAndSubject(String userId, String subjectId) {
        Session session = sessionFactory.openSession();
        List<UserTask> userTasks = session.getNamedQuery("UserTask.findByUserAndSubject").setParameter("subjectId", subjectId).setParameter("userId", userId).list();
        session.close();
        return userTasks;
    }

    @Override
    public List<UserTask> getUserTaskByUser(String userId) {
        Session session = sessionFactory.openSession();
        List<UserTask> userTasks = session.getNamedQuery("UserTask.findByUser").setParameter("userId", userId).list();
        session.close();
        return userTasks;
    }

    @Override
    public List<UserTask> getUserTaskByStudent(String studentId) {
        Session session = sessionFactory.openSession();
        List<UserTask> userTasks = session.getNamedQuery("UserTask.findByUserAndSubject").setParameter("userId", studentId).list();
        session.close();
        return userTasks;
    }

    @Override
    public void addUserTask(UserTask userTask){
        System.out.println("ticketUpdate");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.saveOrUpdate(userTask);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public List<UserTask> getUserTaskByTaskId(String taskId){
        Session session = sessionFactory.openSession();
        List<UserTask> userTasks = session.getNamedQuery("UserTask.findUserTaskByTaskId").setParameter("taskId", taskId).list();
        session.close();
        return userTasks;
    }

    public Task getTaskById(String taskId){
        Session session = sessionFactory.openSession();
        Task task = (Task) session.getNamedQuery("Task.findTaskById").setParameter("id", taskId).uniqueResult();
        session.close();
        return task;
    }

    public UserTask getUserTaskByUserAndTask(String userId, String taskId){
        Session session = sessionFactory.openSession();
        UserTask task = (UserTask) session.getNamedQuery("UserTask.findByUserAndTask").setParameter("userId", userId).setParameter("taskId", taskId).uniqueResult();
        session.close();
        return task;
    }

    public void deleteTask(Task task){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.delete(task);
            tx.commit();
        }catch(Exception e){
            if(tx != null) tx.rollback();
            throw e;
        }finally {
            session.close();
        }
    }

    public void deleteUserTask(String taskId){
        Session session = sessionFactory.openSession();
        session.getNamedQuery("UserTask.deleteByTaskId").setParameter("taskId", taskId).executeUpdate();
        session.close();
    }
}
