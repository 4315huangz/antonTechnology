package org.antontech.repository;

import org.antontech.model.Project;
import org.antontech.repository.Exception.ProjectDaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectHibernateDao implements IProjectDao{
    private final Logger logger = LoggerFactory.getLogger(ProjectHibernateDao.class);
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Project> getProjects() {
        logger.info("Start to getProjects from postgres via HibernateDao");
        List<Project> projects = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "From Project p left join fetch p.users";
            Query<Project> query = session.createQuery(hql);
            projects = query.list();
        } catch (HibernateException e) {
            logger.error("Unexpected exception occurred", e);
            throw new ProjectDaoException("Failed to get projects due to unexpected error", e);
        } finally {
            if(session != null) session.close();
        }
        return projects;
    }

    @Override
    public boolean save(Project project) {
        logger.info("Start to save project in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                logger.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to save project ${}", project);
            throw new ProjectDaoException("Failed to save project due to unexpected exception", e);
        }  finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Project getById(long id) {
        logger.info("Start to get projectById in postgres via HibernateDao");
        String hql = "FROM Project p left join fetch p.users WHERE p.projectId = :Id";
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
            throw new ProjectDaoException("Failed to get project due to unexpected exception: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateDescription(long id, String description) {
        logger.info("Start to update project description in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.description = :description WHERE pr.projectId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Project> query = session.createQuery(hql);
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
            throw new ProjectDaoException("Failed to update project description due to unexpected exception" + e.getMessage(), e);
        }
    }

    @Override
    public void updateManager(long id, String manager) {
        logger.info("Start to update project manager in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Project as pr set pr.manager = :manager WHERE pr.projectId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Project> query = session.createQuery(hql);
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
            throw new ProjectDaoException("Failed to update project manager due to unexpected exception" + e.getMessage(), e);
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Start to delete project description in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "delete Project as pr where pr.projectId = :Id";

        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Project> query = session.createQuery(hql);
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
            throw new ProjectDaoException("Failed to delete project due to unexpected exception" + e.getMessage(), e);
        }
    }
}
