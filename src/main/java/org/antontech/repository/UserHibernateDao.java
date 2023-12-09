package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HibernateUtil;

import java.sql.SQLException;
import java.util.List;

public class UserHibernateDao implements IUserDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Logger logger = LoggerFactory.getLogger(UserHibernateDao.class);

    @Override
    public List<User> getUsers() throws SQLException {
        List<User> users;
        String hql = "From User";
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            users = query.list();
            session.close();
            return users;
        } catch (HibernateException e) {
            logger.error("Open session exception or close session exception", e);
            throw e;
        }
    }

    @Override
    public boolean save(User user) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                transaction.rollback();
            }
            logger.error("Failed to save user ${}", user);
            throw e;
        }

    }

    @Override
    public User getById(long id) {
        User user;
        String hql = "From User as u WHERE u.user_id = :id";
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            user = query.uniqueResult();
            session.close();
            return  user;
        } catch (HibernateException e) {
            logger.error("Unable to get user by user_id = ${}", id, e);
            throw e;
        }
    }

    @Override
    public List<User> getUsersByIndustry(String industry) {
        List<User> users;
        String hql = "From User as u WHERE u.industry = :industry";
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            query.setParameter("industry", industry);
            users = query.list();
            session.close();
            return  users;
        } catch (HibernateException e) {
            logger.error("Unable to get users by industry = ${}", industry, e);
            throw e;
        }
    }

    @Override
    public void updateCompanyName(long id, String name) {
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.company_name = :name WHERE u.user_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("name", name);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user company_name for ${}", id, e);
            throw e;
        }
    }

    @Override
    public void updateAddress(long id, String address) {
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.address = :address WHERE u.user_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("address", address);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user address for ${}", id, e);
            throw e;
        }
    }

    @Override
    public void updateIndustry(long id, String industry) {
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.industry = :industry WHERE u.user_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("industry", industry);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user industry for ${}", id, e);
            throw e;
        }

    }

    @Override
    public void updateManager(long id, String manager, String title, String email, String phone) {
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.manager_name = :manager, u.title= :title," +
                "u.email=:email, u.phone = :phone WHERE u.user_id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("manager", manager);
            query.setParameter("title", title);
            query.setParameter("email", email);
            query.setParameter("phone", phone);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user's manager information for ${}", id, e);
            throw e;
        }
    }

    @Override
    public void delete(long id) {
        Transaction transaction = null;
        String hql = "delete User as u where u.user_id = :Id";

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
            logger.error("Unable to delete user id = ${}", id, e);
            throw e;
        }

    }
}
