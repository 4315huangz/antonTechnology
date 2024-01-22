package org.antontech.repository;

import org.antontech.model.Account;
import org.antontech.repository.Exception.AntonAccountNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import util.HibernateUtil;

import javax.security.auth.login.AccountNotFoundException;

@Repository
public class AccountDaoImpl implements IAccountDao {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Account getAccountByCredentials(String email, String password) throws Exception {
        String hql = "FROM Account a where lower(a.email) = :email and a.password = :password";
        logger.info(String.format("Account email: %s, password: %s", email, password));
        try {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            Query<Account> query = session.createQuery(hql);
            query.setParameter("email", email);
            query.setParameter("password", password);
            logger.info("Set account {}", query.uniqueResult());
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            throw new AntonAccountNotFoundException("Can't find account with email = " + email + ", password = " + password);
        }
    }

    @Override
    public Account getAccountById(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        String hql = "From Account a where a.id = :Id";
        logger.info("Get account from postgres by HibernateDao");
        try {
            Query<Account> query = session.createQuery(hql);
            query.setParameter("Id", id);
            Account result = query.uniqueResult();
            session.close();
            return result;
        } catch (Exception e) {
            logger.error("Session close exception try again", e);
            session.close();
            return null;
        }
    }
}
