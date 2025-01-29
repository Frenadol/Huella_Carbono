package org.example.dao;

import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;

public class UserDao {
    private Connection connection = Connection.getInstance();

    public void insertNewUser(Usuario usuario) {
        try (Session session = connection.getSession()) {
            session.beginTransaction();
            session.persist(usuario);
            session.getTransaction().commit();
        }
    }

    public static UserDao build() {
        return new UserDao();
    }
}