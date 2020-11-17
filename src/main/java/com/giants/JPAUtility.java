package com.giants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtility {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.giants");
    private static EntityManager em = emf.createEntityManager();

    public static EntityManager getEntityManager() {
        return em;
    }

    public static void close() {
        emf.close();
    }
}
