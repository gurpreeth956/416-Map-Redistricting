package com.giants;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtility {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.giants");
    private static EntityManager em1 = emf.createEntityManager();
//    private static EntityManager em2 = emf.createEntityManager();

    public static EntityManager getEntityManager() {
        return em1;
    }

//    public static EntityManager getEntityManager2() {
//        return em2;
//    }

    public static void close() {
        emf.close();
    }
}
