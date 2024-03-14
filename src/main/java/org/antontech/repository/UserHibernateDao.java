package org.antontech.repository;

import org.antontech.model.User;
import org.antontech.repository.Exception.UserDaoException;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.antontech.util.HibernateUtil;

import java.util.List;

@Repository
public class UserHibernateDao implements IUserDao {
    private final Logger logger = LoggerFactory.getLogger(UserHibernateDao.class);

    @Override
    public List<User> getUsers() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to getUsers from postgres via HibernateDao");
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
            throw new UserDaoException("Failed to get users", e);
        }
    }

    @Override
    public boolean save(User user) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to create user via HibernateDao");
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.saveOrUpdate(user);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                transaction.rollback();
            }
            logger.error("Failed to save user ${}", user);
            throw new UserDaoException("Failed to save user", e);
        }

    }

    @Override
    public User getById(long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to get userById via HibernateDao");
        User user=null;
        String hql = "From User u left join fetch u.projects WHERE u.userId = :id";
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            user = query.uniqueResult();
            session.close();
            return  user;
        } catch (HibernateException e) {
            logger.error("Unable to get user by user_id = {}", id, e);
            throw new UserDaoException("Failed to get user", e);
        }
    }

    @Override
    public List<User> getByIndustry(String industry) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to get userByIndustry via HibernateDao");
        List<User> users;
        String hql = "From User as u WHERE lower(u.industry) = :industry";
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            query.setParameter("industry", industry.toLowerCase().trim());
            users = query.list();
            session.close();
            return  users;
        } catch (HibernateException e) {
            logger.error("Unable to get users by industry = {}", industry, e);
            throw new UserDaoException("Failed to get user", e);
        }
    }

    @Override
    public void updateEmail(long id, String email) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update user email via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.email = :email WHERE u.userId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("email", email);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user email for {}", id, e);
            throw new UserDaoException("Failed to update user email", e);
        }
    }

    @Override
    public void updatePassword(long id, String password) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update user address via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.password = :password WHERE u.userId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("password", password);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user password for {}", id, e);
            throw new UserDaoException("Failed to update user password", e);
        }
    }

    @Override
    public void updateCompany(long id, String companyName, String address, String industry) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update user company name via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.companyName = :companyName, u.address= :address," +
                "u.industry=:industry WHERE u.userId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("companyName", companyName);
            query.setParameter("address",address);
            query.setParameter("industry", industry);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user company information for {}", id, e);
            throw new UserDaoException("Failed to update user company", e);
        }
    }

    @Override
    public void updateManager(long id, String firstName, String lastName, String title, String phone) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to update user manager via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE User as u set u.firstName = :firstName, u.lastName = :lastName, u.title= :title," +
                "u.phone = :phone WHERE u.userId = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName",lastName);
            query.setParameter("title", title);
            query.setParameter("phone", phone);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Failed to update user's manager information for ${}", id, e);
            throw new UserDaoException("Failed to update user manager", e);
        }
    }

    @Override
    public void delete(long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to delete user via HibernateDao");
        Transaction transaction = null;
        String hql = "delete from User as u where u.userId = :Id";

        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<User> query = session.createQuery(hql);
            query.setParameter("Id", id);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
            logger.error("Unable to delete user id = {}", id, e);
            throw new UserDaoException("Failed to delete user", e);
        }
    }

    @Override
    public User getUserByCredentials(String email, String password) throws Exception {
        String hql = "FROM User as u where lower(u.email) = :email and u.password = :password";
        logger.info(String.format("User email: %s, password: %s", email, password));
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        logger.info("Start to get user credential via HibernateDao");
        try {
            Session session = sessionFactory.openSession();
            Query<User> query = session.createQuery(hql);
            query.setParameter("email", email.toLowerCase().trim());
            query.setParameter("password", password);
            logger.info("Get user {}", query.uniqueResult());
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            throw new UserDaoException("Failed to find the user for given email and password", e);
        }
    }
}
