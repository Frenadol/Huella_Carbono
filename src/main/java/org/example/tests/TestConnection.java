package org.example.tests;

import org.example.connection.Connection;
import org.hibernate.Session;

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
