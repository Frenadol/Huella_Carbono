package org.example.dao;

import org.example.entities.Actividad;
import org.example.entities.Categoria;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
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

    public void deleteFingerPrint(Huella huella) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.delete(huella);
            session.getTransaction().commit();
        }
    }

    public void updateFingerPrint(Huella huella) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.update(huella);
            session.getTransaction().commit();
        }
    }

    public List<Huella> viewFingerPrints(Usuario usuario) {
        try (Session session = Connection.getInstance().getSession()) {
            Query<Huella> query = session.createQuery("FROM Huella WHERE idUsuario = :usuario", Huella.class);
            query.setParameter("usuario", usuario);
            List<Huella> huellas = query.list();
            huellas.forEach(huella -> {
                huella.getIdActividad().getIdCategoria().getFactorEmision(); // Force eager loading
            });
            return huellas;
        }
    }

    public List<Huella> viewFingerPrintsByCategory(Usuario usuario, Categoria category) {
        try (Session session = Connection.getInstance().getSession()) {
            Query<Huella> query = session.createQuery("FROM Huella WHERE idUsuario = :usuario AND idActividad.idCategoria = :category", Huella.class);
            query.setParameter("usuario", usuario);
            query.setParameter("category", category);
            List<Huella> huellas = query.list();
            huellas.forEach(huella -> {
                huella.getIdActividad().getIdCategoria().getFactorEmision(); // Force eager loading
            });
            return huellas;
        }
    }
    public void updateFingerPrintDetails(Huella huella, Actividad nuevaActividad, BigDecimal nuevoValor, String nuevaUnidad) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            huella.setIdActividad(nuevaActividad);
            huella.setValor(nuevoValor);
            huella.setUnidad(nuevaUnidad);
            session.update(huella);
            session.getTransaction().commit();
        }
    }
}