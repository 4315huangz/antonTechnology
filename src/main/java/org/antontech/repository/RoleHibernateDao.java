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
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleHibernateDao implements IRoleDao {
    private static final Logger logger = LoggerFactory.getLogger(RoleHibernateDao.class);
    @Override
    public List<Role> getRoles() {
        logger.info("Start to get roles from postgres via HibernateDao");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "From Role";
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql);
            List<Role> roles = query.list();
            session.close();
            return roles;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get roles", e);
        }
    }

    @Override
    public Role getById(long id) {
        logger.info("Start to get role by role id from postgres via HibernateDao");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "FROM Role as r WHERE r.id = :Id";
        Role role = null;
        try {
            Session session = sessionFactory.openSession();
            Query<Role> query = session.createQuery(hql);
            query.setParameter("Id", id);
            role = query.uniqueResult();
            session.close();
            return role;
        } catch (HibernateException e) {
            logger.error("Unable to get role by role id = {}", id, e);
            throw new RoleDaoException("Failed to get role", e);
        }
    }

    @Override
    public boolean save(Role role) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to create a role in postgres via HibernateDao");
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                logger.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Failed to save role {}", role);
            throw new RoleDaoException("Failed to save role", e);
        }
    }

    @Override
    public void delete(Role role) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to delete role in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "delete Role as r where r.id = :Id";

        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", role.getId());
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if(transaction != null) {
                logger.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            logger.error("Unable to delete role id = {}", role.getId(), e);
            throw new RoleDaoException("Failed to delete role", e);
        }
    }

    @Override
    public String getAllowedResources(Role role) {
        logger.info("Start to get allowed resources from role table via Hibernate DAO.");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID";
        try {
            Session session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedResources = query.uniqueResult();
            session.close();
            return allowedResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed resources", e);
        }
    }

    @Override
    public String getAllowedReadResources(Role role) {
        logger.info("Start to get allowed read resources from role table via Hibernate DAO.");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedRead = true";
        try {
            Session session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedReadResources = query.uniqueResult();
            session.close();
            return allowedReadResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed read resources", e);
        }
    }

    @Override
    public String getAllowedCreateResources(Role role) {
        logger.info("Start to get allowed create resources from role table via Hibernate DAO.");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedCreate = true";
        try {
            Session session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedCreateResources = query.uniqueResult();
            session.close();
            return allowedCreateResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed create resources", e);
        }
    }

    @Override
    public String getAllowedUpdateResources(Role role) {
        logger.info("Start to get allowed update resources from role table via Hibernate DAO.");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedUpdate = true";
        try {
            Session session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedUpdateResources = query.uniqueResult();
            session.close();
            return allowedUpdateResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed update resources", e);
        }
    }

    @Override
    public String getAllowedDeleteResources(Role role) {
        logger.info("Start to get allowed delete resources from role table via Hibernate DAO.");
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        String hql = "SELECT r.allowedResource From Role as r WHERE r.id = :roleID AND r.allowedDelete = true";
        try {
            Session session = sessionFactory.openSession();
            Query<String> query = session.createQuery(hql);
            query.setParameter("roleID", role.getId());
            String allowedDeleteResources = query.uniqueResult();
            session.close();
            return allowedDeleteResources;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception");
            throw new RoleDaoException("Failed to get allowed delete resources", e);
        }
    }
}
