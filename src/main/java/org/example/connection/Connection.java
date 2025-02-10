package org.example.connection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Connection {
    private static Connection instance;
    private SessionFactory sessionFactory;

    private Connection() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static  Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}