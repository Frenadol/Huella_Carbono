package org.example.dao;

import org.example.entities.Categoria;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class FingerPrintDao {
    public static FingerPrintDao build() {
        return new FingerPrintDao();
    }
    public void saveFingerPrint(Huella huella) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        session.save(huella);
        session.getTransaction().commit();
    }

    public List<Huella> viewFingerPrints(Usuario idusuario) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        Query<Huella> findFingerPrintQuery = session.createQuery("From Huella where idUsuario=:idusuario", Huella.class);
        findFingerPrintQuery.setParameter("idusuario", idusuario);
        return findFingerPrintQuery.list();
    }

    public List<Huella> viewFingerPrintsByCategory(Usuario idusuario, Categoria category) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        Query<Huella> findFingerPrintQuery = session.createQuery("From Huella where idUsuario=:idusuario and idActividad.idCategoria=:category", Huella.class);
        findFingerPrintQuery.setParameter("idusuario", idusuario);
        findFingerPrintQuery.setParameter("category", category);
        return findFingerPrintQuery.list();
    }
}