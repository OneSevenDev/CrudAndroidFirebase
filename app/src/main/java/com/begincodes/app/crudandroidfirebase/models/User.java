package com.begincodes.app.crudandroidfirebase.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by manuelguarniz on 16/11/16.
 */
@IgnoreExtraProperties
public class User {
    public String nombre;
    public String apellidos;
    public int edad;

    public User() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
