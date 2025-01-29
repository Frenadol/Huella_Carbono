package org.example.dao;

import org.example.entities.Huella;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FingerPrintDao {
    public void save(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.save(huella);
        transaction.commit();
    }
}