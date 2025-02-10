package org.example.entities;

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
    public int getUserId(){
        return userLogged.getId();
    }

    public void setUser(Usuario user) {
        userLogged = user;
    }
}