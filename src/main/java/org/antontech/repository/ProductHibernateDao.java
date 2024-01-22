package org.antontech.repository;

import org.antontech.model.Product;

import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import util.HibernateUtil;


import java.util.List;

@Repository
public class ProductHibernateDao implements IProductDao {
    private static final Logger log = LoggerFactory.getLogger(ProductHibernateDao.class);

    @Override
    public List<Product> getProducts()  {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
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

    @Override
    public List<Product> searchByDescription(String keyword) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        log.info("Start to search product by keyword in postgres via HibernateDao");
        String hql = "FROM Product P WHERE lower(P.description) LIKE lower(:keyword)";
        List<Product> products=null;
        try {
            Session session = sessionFactory.openSession();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("keyword", "%" + keyword + "%");
            products = query.getResultList();
            session.close();
            return products;
        } catch (ObjectNotFoundException ex) {
            log.error("A User entity referenced by a product was not found.");
            return products;
        } catch (HibernateException e) {
            log.error("Unable to search products by keyword = {}", keyword, e);
            throw e;
        }
    }
}
