package com.egor.familyTree.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.egor.familyTree.model.Node;

public class NodeDao {
    
    public boolean saveNode(Node node) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(node);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean saveAll(List<Node> nodes) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            for (Node node : nodes) {
                session.persist(node);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<Node> getNodes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("From nodes", Node.class).list();
        }
    }
}
