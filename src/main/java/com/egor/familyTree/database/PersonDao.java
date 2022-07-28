package com.egor.familyTree.database;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.egor.familyTree.model.Person;

public class PersonDao {
    
    public boolean savePerson(Person person) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();

            session.persist(person);

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

    public boolean saveAll(List<Person> persons) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            
            transaction = session.beginTransaction();
            
            for (Person person : persons) {
                session.persist(person);
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

    public List<Person> getPersons() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("From person", Person.class).list();
        }
    }
}
