package org.example.dao;

import org.example.entities.Usuario;
import org.example.connection.Connection;
import org.hibernate.Session;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Usuario` entities.
 * This class provides methods to interact with the database for `Usuario` entities.
 */
public class UserDao {

    /**
     * Inserts a new `Usuario` entity into the database.
     *
     * @param usuario the `Usuario` entity to insert.
     */
    public void insertNewUser(Usuario usuario) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Finds a `Usuario` entity by email.
     *
     * @param email the email to search for.
     * @return the `Usuario` entity found, or null if not found.
     */
    public Usuario findByEmail(String email) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = Connection.getInstance().getSession();
            usuario = session.createQuery("FROM Usuario where email=:email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuario;
    }

    /**
     * Finds a `Usuario` entity by username.
     *
     * @param username the username to search for.
     * @return the `Usuario` entity found, or null if not found.
     */
    public Usuario findByUsername(String username) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = Connection.getInstance().getSession();
            usuario = session.createQuery("FROM Usuario where nombre=:username", Usuario.class)
                    .setParameter("username", username)
                    .uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuario;
    }

    /**
     * Finds a `Usuario` entity by username and email.
     *
     * @param username the username to search for.
     * @param email the email to search for.
     * @return the `Usuario` entity found, or null if not found.
     */
    public Usuario findUser(String username, String email) {
        Session session = null;
        Usuario usuario = null;
        try {
            session = Connection.getInstance().getSession();
            usuario = session.createQuery("FROM Usuario WHERE nombre=:nombre AND email=:email", Usuario.class)
                    .setParameter("nombre", username)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuario;
    }

    /**
     * Retrieves all `Usuario` entities from the database.
     *
     * @return a list of `Usuario` entities.
     */
    public List<Usuario> findAllUsers() {
        Session session = null;
        List<Usuario> usuarios = null;
        try {
            session = Connection.getInstance().getSession();
            usuarios = session.createQuery("FROM Usuario", Usuario.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return usuarios;
    }

    /**
     * Builds and returns an instance of `UserDao`.
     *
     * @return a new instance of `UserDao`.
     */
    public static UserDao build() {
        return new UserDao();
    }
}