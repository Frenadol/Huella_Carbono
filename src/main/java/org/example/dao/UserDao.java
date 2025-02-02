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

    public Usuario findByEmail(String email) {
        Usuario usuario = null;
        try (Session session = connection.getSession()) {
            usuario = session.createQuery("FROM Usuario where email=:email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }
        return usuario;
    }

    public Usuario findByUsername(String username) {
        Usuario usuario = null;
        try (Session session = connection.getSession()) {
            usuario = session.createQuery("FROM Usuario where nombre=:username", Usuario.class)
                    .setParameter("username", username)
                    .uniqueResult();
        }
        return usuario;
    }

    public Usuario findUser(String username, String password, String email) {
        Usuario usuario = null;
        try (Session session = connection.getSession()) {
            usuario = session.createQuery("FROM Usuario WHERE nombre=:nombre AND email=:email AND contraseña=:contraseña", Usuario.class)
                    .setParameter("nombre", username)
                    .setParameter("email", email)
                    .setParameter("contraseña", password)
                    .uniqueResult();
        }
        return usuario;
    }

    public static UserDao build() {
        return new UserDao();
    }
}