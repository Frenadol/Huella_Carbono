package org.example.dao;

import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class FingerPrintDao {
    public void saveFingerPrint(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(huella);
        transaction.commit();
    }


    public List<Huella> viewFingerPrints(Usuario idusuario){
        Session session=Connection.getInstance().getSession();
        session.beginTransaction();
        Query<Huella> findFingerPrintQuery=session.createQuery("From Huella where idUsuario=:idusuario",Huella.class);
        findFingerPrintQuery.setParameter("idusuario",idusuario);
        return findFingerPrintQuery.list();
    }

}