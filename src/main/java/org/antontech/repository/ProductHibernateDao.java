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
        log.info("Start to getProducts from postgres via HibernateDao");
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
    public Product getById(long id)  {
        log.info("Start to getProductById from postgres via HibernateDao");
        String hql = "FROM Product P WHERE P.id = :Id";
        Product product = null;
        try {
            Session session = sessionFactory.openSession();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", id);
            product = query.uniqueResult();
            session.close();
            return product;
        } catch (HibernateException e) {
            log.error("Unable to get product by product id = {}", id, e);
            throw e;
        }
    }

    @Override
    public boolean save(Product product) {
        log.info("Start to save product in postgres via HibernateDao");
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
                log.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to save product {}", product);
            throw e;
        }
    }

    @Override
    public void updateName(long id, String name) {
        log.info("Start to update product name in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Product as p set p.name = :name WHERE p.id = :id";
        try {
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("name", name);
            query.executeUpdate();
            transaction.commit();
            session.close();
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Update transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to update product name for {}", id, e);
            throw e;
        }
    }

    @Override
    public void updateDescription(long id, String description) {
        log.info("Start to update product description in postgres via HibernateDao");
        Transaction transaction = null;
        String hql = "UPDATE Product as p set p.description = :description WHERE p.id = :id";
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
            if(transaction != null){
                log.error("Update transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to update product description for {}", id, e);
            throw e;
        }

    }

    @Override
    public void delete(long id) {
        log.info("Start to delete product in postgres via HibernateDao");
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
                log.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Unable to delete product id = {}", id, e);
            throw e;
        }
    }
}
