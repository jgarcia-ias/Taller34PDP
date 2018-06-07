/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.entidad;

/**
 *
 * @author chori
 */
public class Cliente {
    String idCliente;
    String nombre;
    String apellido;
    String telefono;
    String categoria;
    int asesorado;

    public Cliente(String idCliente, String nombre, String apellido, String telefono, String categoria, int asesorado) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.categoria = categoria;
        this.asesorado = asesorado;
    }
    

    public int getAsesorado() {
        return asesorado;
    }

    public void setAsesorado(int asesorado) {
        this.asesorado = asesorado;
    }
    
    public Cliente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    
}
