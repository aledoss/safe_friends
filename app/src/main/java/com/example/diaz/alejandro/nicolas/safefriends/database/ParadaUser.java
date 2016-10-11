package com.example.diaz.alejandro.nicolas.safefriends.database;

import java.io.Serializable;

/**
 * Created by Lenwe on 01/10/2016.
 */
public class ParadaUser implements Serializable {
    private int id;
    private String nameParada;
    private String nameUser;
    private String latitud;
    private String longitud;


    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameParada() {
        return nameParada;
    }

    public void setNameParada(String name) {
        this.nameParada = name;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

}
