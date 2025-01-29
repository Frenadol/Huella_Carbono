package org.example.utils;

import org.example.entities.Usuario;

public class Session {
    private static Session _instance;
    private static Usuario userLogged;
    private Session(){

    }
    /**
     * Gets the singleton instance of the Session class.
     * If the instance doesn't exist, it creates one and logs in the user.
     * @return The singleton instance of Session.
     */
    public static Session getInstance(){
        if (_instance==null){
            _instance = new Session();
            _instance.logIn(userLogged);
        }
        return _instance;
    }
    public void logIn(Usuario user){
        userLogged=user;
    }

    public Usuario getUserLogged(){
        return userLogged;
    }

    public void logOut(){
        userLogged=null;
    }

    public void setUser(Usuario user) {
        userLogged = user;
    }
}