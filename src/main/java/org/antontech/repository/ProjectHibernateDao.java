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
import org.springframework.stereotype.Repository;
import org.antontech.util.HibernateUtil;

import java.util.List;

@Repository
public class ProjectHibernateDao implements IProjectDao{
    private final Logger logger = LoggerFactory.getLogger(ProjectHibernateDao.class);

    @Override
    public List<Project> getProjects() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to getProjects from postgres via HibernateDao");
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to save project in postgres via HibernateDao");
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
                logger.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to save project ${}", project);
            throw e;
        }
    }

    @Override
    public Project getById(long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to get projectById in postgres via HibernateDao");
        String hql = "FROM Project Pr WHERE Pr.projectId = :Id";
        Project project = null;
        try {
            Session session = sessionFactory.openSession();
            Query<Project> query = session.createQuery(hql);
            query.setParameter("Id", id);
            project = query.uniqueResult();
            session.close();
            return project;
        } catch (HibernateException e) {
            logger.error("Unable to get project by project id = {}", id, e);
            throw e;
        }
    }

    @Override
    public void updateDescription(long id, String description) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update project description in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.description = :description WHERE pr.projectId = :id";
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
                logger.error("Update description transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to update project description for {}", id, e);
            throw e;
        }
    }

    @Override
    public void updateManager(long id, String manager) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update project manager in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.manager = :manager WHERE pr.projectId = :id";
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
                logger.error("Update manager transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to update project manager for {}", id, e);
            throw e;
        }
    }

    @Override
    public void delete(long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to delete project description in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "delete Project as pr where pr.projectId = :Id";

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
                logger.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Unable to delete project id = {}", id, e);
            throw e;
        }
    }
}
