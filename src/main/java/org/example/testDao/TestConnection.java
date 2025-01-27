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
            session.getTransaction();
            Usuario usuario = new Usuario();
            usuario.setNombre("Juan");
            usuario.setEmail("1@gmail.com");
            usuario.setContraseña("123gf");
            usuario.setFechaRegistro(LocalDate.now());
            session.save(usuario);
            session.close();
            System.out.println(usuario);

        } else{
            System.out.printf("NAO NAO");


        }

    }
}
