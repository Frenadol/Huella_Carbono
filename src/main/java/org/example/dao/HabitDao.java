package org.example.dao;

import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.utils.Connection;
import org.hibernate.Session;

public class HabitDao {

    public void createHabit(Habito habito){
        try(Session session= Connection.getInstance().getSession()){
            session.beginTransaction();
            session.save(habito);
            session.getTransaction().commit();
        }
    }
    public boolean exists(HabitoId habitoId) {
        Session session = Connection.getInstance().getSession();
        try {
            Habito habito = session.get(Habito.class, habitoId);
            return habito != null;
        } finally {
            session.close();
        }
    }
}
