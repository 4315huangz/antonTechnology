package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.Project;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import util.HibernateUtil;

import java.sql.SQLException;
import java.util.List;


public class ProjectHibernateDao implements IProjectDao{
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Logger logger = LoggerFactory.getLogger(ProjectHibernateDao.class);

    @Override
    public List<Project> getProjects() throws SQLException {
        List<Project> projects;
        try {
            Session session = sessionFactory.openSession();
            String hql = "From Project";
            Query<Project> query = session.createQuery(hql);
            projects = query.list();
            session.close();
            return projects;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception", e);
            throw e;
        }
    }

    @Override
    public boolean save(Project project) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(project);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                transaction.rollback();
            }
            logger.error("Failed to save project ${}", project);
            throw e;
        }
    }

    @Override
    public Project getById(long id) {
        String hql = "FROM Project Pr WHERE Pr.project_id = :Id";
        Project project = null;
        try {
            Session session = sessionFactory.openSession();
            Query<Project> query = session.createQuery(hql);
            query.setParameter("Id", id);
            project = query.uniqueResult();
            session.close();
            return project;
        } catch (HibernateException e) {
            logger.error("Unable to get project by project id = ${}", id, e);
            throw e;
        }
    }

    @Override
    public void updateDescription(long id, String description) {
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.description = :description WHERE pr.project_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("description", description);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update project description for ${}", id, e);
            throw e;
        }
    }

    @Override
    public void updateManager(long id, String manager) {
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.manager = :manager WHERE pr.project_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("manager", manager);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update project manager for ${}", id, e);
            throw e;
        }
    }

    @Override
    public void delete(long id) {
        Transaction transaction = null;
        String hql = "delete Project as pr where pr.project_id = :Id";

        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", id);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            logger.error("Unable to delete project id = ${}", id, e);
            throw e;
        }

    }
}
