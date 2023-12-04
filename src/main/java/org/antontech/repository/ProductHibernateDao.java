package org.antontech.repository;

import org.antontech.model.Product;
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

public class ProductHibernateDao implements IProductDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final Logger log = LoggerFactory.getLogger(ProductHibernateDao.class);
    @Override
    public List<Product> getProducts() throws SQLException {
        List<Product> products;
        try {
            Session session = sessionFactory.openSession();
            String hql = "from Product";
            Query<Product> query = session.createQuery(hql);
            products = query.list();
            session.close();
            return products;
        } catch (HibernateException e) {
            log.error("Open session exception or close session exception", e);
            throw e;
        }
    }

    @Override
    public List<Product> getById(int id) throws SQLException {
        return null;
    }

    @Override
    public boolean save(Product product) {
        Transaction transaction = null;
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
            session.close();
            return true;
        } catch (HibernateException e) {
            if( transaction != null ){
                transaction.rollback();
            }
            log.error("Failed to save product ${}", product);
            throw e;
        }

    }

    @Override
    public Product updateName(int id, String name) {
        return null;
    }

    @Override
    public void delete(int id) {
        Transaction transaction = null;
        String hql = "delete Product as p where p.id = :Id";

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
            log.error("Unable to delete product id = ${}", id, e);
            throw e;
        }

    }
}
