package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.Role;
import org.antontech.repository.Exception.RoleDaoException;
import org.antontech.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleHibernateDao implements IRoleDao {
    private static final Logger logger = LoggerFactory.getLogger(RoleHibernateDao.class);
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Role> getRoles() {
        logger.info("Start to get roles from postgres via HibernateDao");
        String hql = "From Role";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            List<Role> roles = query.list();
            return roles;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get roles", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public Role getById(long id) {
        logger.info("Start to get role by role id from postgres via HibernateDao");
        String hql = "FROM Role as r WHERE r.id = :Id";
        Role role = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Role> query = session.createQuery(hql);
            query.setParameter("Id", id);
            role = query.uniqueResult();
        } catch (HibernateException e) {
            logger.error("Unable to get role by role id = {}", id, e);
            throw new RoleDaoException("Failed to get role", e);
        } finally {
            if(session != null) session.close();
        }
        return role;
    }

    @Override
    public boolean save(Role role) {
        logger.info("Start to create a role in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                logger.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to save role {}", role);
            throw new RoleDaoException("Failed to save role", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public void delete(Role role) {
        logger.info("Start to delete role in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        String hql = "delete Role as r where r.id = :Id";

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", role.getId());
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if(transaction != null) {
                logger.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Unable to delete role id = {}", role.getId(), e);
            throw new RoleDaoException("Failed to delete role", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String getAllowedResources(Role role) {
        logger.info("Start to get allowed resources from role table via Hibernate DAO.");
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedResources = query.uniqueResult();
            return allowedResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed resources", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String getAllowedReadResources(Role role) {
        logger.info("Start to get allowed read resources from role table via Hibernate DAO.");
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedRead = true";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedReadResources = query.uniqueResult();
            return allowedReadResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed read resources", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String getAllowedCreateResources(Role role) {
        logger.info("Start to get allowed create resources from role table via Hibernate DAO.");
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedCreate = true";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedCreateResources = query.uniqueResult();
            return allowedCreateResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed create resources", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String getAllowedUpdateResources(Role role) {
        logger.info("Start to get allowed update resources from role table via Hibernate DAO.");
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedUpdate = true";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedUpdateResources = query.uniqueResult();
            return allowedUpdateResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed update resources", e);
        } finally {
            if(session != null) session.close();
        }
    }

    @Override
    public String getAllowedDeleteResources(Role role) {
        logger.info("Start to get allowed delete resources from role table via Hibernate DAO.");
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedDelete = true";
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedDeleteResources = query.uniqueResult();
            return allowedDeleteResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed delete resources", e);
        } finally {
            if(session != null) session.close();
        }
    }
}
