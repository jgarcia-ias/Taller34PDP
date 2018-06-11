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
public class Llamada {
    String idLlamada;
    Asesor asesor;
    Cliente cliente;
    String telefono;
    String tiempo;
    String valor;

    public Llamada(String idLlamada, Asesor asesor, Cliente cliente, String telefono, String tiempo, String valor) {
        this.idLlamada = idLlamada;
        this.asesor = asesor;
        this.cliente = cliente;
        this.telefono = telefono;
        this.tiempo = tiempo;
        this.valor = valor;
    }

    public Llamada() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getIdLlamada() {
        return idLlamada;
    }

    public void setIdLlamada(String idLlamada) {
        this.idLlamada = idLlamada;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    
    
}
