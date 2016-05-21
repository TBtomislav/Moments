package com.dev.alt.devand.helper;

public class PersonEntity {
    //private variables
    private String _login;
    private String _mail;
    private String _socialKey;
    private boolean _loggedIn;

    // Empty constructor
    public PersonEntity(){
    }

    // constructor
    public PersonEntity(String login, String mail){
        this._login = login;
        this._mail = mail;
    }

    // constructor
    public PersonEntity(String login, String mail, String socialKey, boolean loggedIn){
        this._login = login;
        this._mail = mail;
        this._socialKey = socialKey;
        this._loggedIn = loggedIn;
    }

    // getting name
    public String getLogin(){
        return this._login;
    }

    // setting name
    public void setLogin(String login){
        this._login = login;
    }

    // getting phone number
    public String getMail(){
        return this._mail;
    }

    // setting phone number
    public void setMail(String mail){
        this._mail = mail;
    }

    // getting phone number
    public String getSocialKey(){
        return this._socialKey;
    }

    // setting phone number
    public void setSocialKey(String socialKey){
        this._socialKey = socialKey;
    }

    // getting phone number
    public boolean getLoggedIn(){
        return this._loggedIn;
    }

    // setting phone number
    public void setLoggedIn(boolean loggedIn){
        this._loggedIn = loggedIn;
    }
}
