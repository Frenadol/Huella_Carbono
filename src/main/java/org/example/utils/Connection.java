package org.example.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.cfg.Configuration;

public class Connection {

    // 1. Atributo estático para la instancia única
    private static Connection instance;

    // 2. Atributo para la "fábrica de sesiones"
    private SessionFactory sessionFactory;

    // 3. Constructor privado
    private Connection() {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al construir la SessionFactory");
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }
    public Session getSession(){
        return sessionFactory.openSession();
    }

    public void close() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }
}
