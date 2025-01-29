package org.example.testDao;

import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;

import java.time.LocalDate;

public class TestConnection {
    public static void main(String[] args) {
Session session=Connection.getInstance().getSession();
        if(session!=null){
            System.out.println("Sesion iniciada correctamente");
        } else{
            System.out.printf("NAO NAO");
        }

    }
}
