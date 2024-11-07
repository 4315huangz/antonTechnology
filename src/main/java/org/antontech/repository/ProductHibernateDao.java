package org.antontech.repository;

import org.antontech.model.Product;
import org.antontech.repository.Exception.ProductDaoException;
import org.hibernate.HibernateException;
import org.hibernate.ObjectNotFoundException;
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
public class ProductHibernateDao implements IProductDao {
    private static final Logger log = LoggerFactory.getLogger(ProductHibernateDao.class);
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Product> getProducts() {
        log.info("Start to getProducts from postgres via HibernateDao");
        List<Product> products = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "from Product";
            Query<Product> query = session.createQuery(hql);
            products = query.list();
        } catch (HibernateException e) {
            log.error("Unexpected exception occurred", e);
            throw new ProductDaoException("Failed to get products due to unexpected error", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return products;
    }

    @Override
    public Product getById(long id) {
        log.info("Start to getProductById from postgres via HibernateDao");
        String hql = "FROM Product P WHERE P.id = :Id";
        Product product = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", id);
            product = query.uniqueResult();
        } catch (HibernateException e) {
            log.error("Unexpected exception occurred", e);
            throw new ProductDaoException("Failed to get product due to unexpected error", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return product;
    }

    @Override
    public boolean save(Product product) {
        log.info("Start to save product in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(product);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Save transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to save product {}", product);
            throw new ProductDaoException("Failed to save product due to unexpected error", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }

    @Override
    public void updateName(long id, String name) {
        log.info("Start to update product name in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        String hql = "UPDATE Product as p set p.name = :name WHERE p.id = :id";
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("name", name);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Update transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to update product name for {}", id, e);
            throw new ProductDaoException("Failed to update product name", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void updateDescription(long id, String description) {
        log.info("Start to update product description in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        String hql = "UPDATE Product as p set p.description = :description WHERE p.id = :id";
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("id", id);
            query.setParameter("description", description);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Update transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Failed to update product description for {}", id, e);
            throw new ProductDaoException("Failed to update product description", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void delete(long id) {
        log.info("Start to delete product in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        String hql = "delete Product as p where p.id = :Id";

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("Id", id);
            query.executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Unable to delete product id = {}", id, e);
            throw new ProductDaoException("Failed to delete product", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public String getPictureUrl(long id) {
        Session session = null;
        String url = "";
        log.info("Start to get product's picture url in postgres via HibernateDao");
        try {
            session = sessionFactory.openSession();
            Product product = session.get(Product.class, id);
            if (product != null) {
                url = product.getPictureUrl();
            }
        } catch (HibernateException e) {
            log.error("Error while retrieving picture URL for product with id " + id, e);
            throw new ProductDaoException("Failed to get product picture url", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return url;
    }

    @Override
    public void savePictureUrl(long id, String url) {
        log.info("Start to delete product in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                product.setPictureUrl(url);
                session.update(product);
                transaction.commit();
            }
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Delete transaction failed, rollback.");
                transaction.rollback();
            }
            log.error("Error while saving picture URL for product with id " + id, e);
            throw new ProductDaoException("Failed to save product picture url", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void deletePictureUrl(long id) {
        log.info("Start to delete product's picture url in postgres via HibernateDao");
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Product product = session.get(Product.class, id);
            if (product != null) {
                product.setPictureUrl("");
                session.update(product);
                transaction.commit();
            }
        } catch (HibernateException e) {
            if (transaction != null) {
                log.error("Delete product picture failed, rollback.");
                transaction.rollback();
            }
            log.error("Error while deleting picture URL for product with id " + id, e);
            throw new ProductDaoException("Failed to delete product picture url", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Product> searchByDescription(String keyword) {
        log.info("Start to search product by keyword in postgres via HibernateDao");
        String hql = "FROM Product P WHERE lower(P.description) LIKE lower(:keyword)";
        List<Product> products = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Query<Product> query = session.createQuery(hql);
            query.setParameter("keyword", "%" + keyword + "%");
            products = query.getResultList();
        } catch (ObjectNotFoundException ex) {
            log.error("A User entity referenced by a product was not found.");
            return products;
        } catch (HibernateException e) {
            log.error("Unable to search products by keyword = {}", keyword, e);
            throw new ProductDaoException("Failed to get products", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return products;
    }
}
