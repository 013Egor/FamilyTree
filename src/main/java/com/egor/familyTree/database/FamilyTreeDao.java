package com.egor.familyTree.database;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.egor.familyTree.model.FamilyTree;

public class FamilyTreeDao {
    
    public boolean saveTree(FamilyTree tree) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();

            session.persist(tree);

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

    public FamilyTree getPersons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("From familyTree", FamilyTree.class).getSingleResultOrNull();
        }
    }
}
