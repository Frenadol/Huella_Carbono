package org.example.services;

import org.example.dao.UserDao;
import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;

public class UserService {
    private UserDao userDao = UserDao.build();
    private Connection connection = Connection.getInstance();

    public boolean userExists(String email) {
        return findByEmail(email) != null;
    }

    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    public void checkAndInsertNewUser(Usuario newUser) {
        if (!userExists(newUser.getEmail()) && !usernameExists(newUser.getNombre())) {
            userDao.insertNewUser(newUser);
        }
    }

    private Usuario findByEmail(String email) {
        Usuario usuario = null;

        try (Session session = connection.getSession()) {
            usuario = session.createQuery("FROM Usuario where email=:email", Usuario.class)
                    .setParameter("email", email)
                    .uniqueResult();
        }

        return usuario;
    }

    private Usuario findByUsername(String username) {
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
}