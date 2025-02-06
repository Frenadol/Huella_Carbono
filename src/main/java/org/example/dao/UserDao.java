package org.example.dao;

import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;

import java.util.List;

public class UserDao {
    private Connection connection = Connection.getInstance();

    public void insertNewUser(Usuario usuario) {
        try (Session session = connection.getSession()) {
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        }
    }
    public List<Object[]> getUserImpactByCategory(int userId) {
        List<Object[]> results = null;
        try (Session session = connection.getSession()) {
            results = session.createQuery(
                            "SELECT c.nombre, SUM(h.valor * c.factorEmision) " +
                                    "FROM Huella h JOIN h.idActividad a JOIN a.idCategoria c " +
                                    "WHERE h.idUsuario.id = :userId " +
                                    "GROUP BY c.nombre", Object[].class)
                    .setParameter("userId", userId)
                    .list();
        }
        return results;
    }

    public List<Object[]> getAverageImpactByCategory() {
        List<Object[]> results = null;
        try (Session session = connection.getSession()) {
            results = session.createQuery(
                            "SELECT c.nombre, AVG(h.valor * c.factorEmision) " +
                                    "FROM Huella h JOIN h.idActividad a JOIN a.idCategoria c " +
                                    "GROUP BY c.nombre", Object[].class)
                    .list();
        }
        return results;
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
    public List<Usuario> findAllUsers(){
        List<Usuario> usuarios=null;
        try(Session session=connection.getSession()){
            usuarios=session.createQuery("FROM Usuario",Usuario.class).list();
        }
        return usuarios;
    }
    public List<String> findAllUsernames() {
        List<String> usernames = null;
        try (Session session = connection.getSession()) {
            usernames = session.createQuery("SELECT nombre FROM Usuario", String.class).list();
        }
        return usernames;
    }

    public static UserDao build() {
        return new UserDao();
    }
}