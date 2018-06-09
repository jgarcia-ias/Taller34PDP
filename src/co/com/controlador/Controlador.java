/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import co.com.entidad.*;
import co.com.util.Conexion;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author chori
 */
public class Controlador {

    private final String ASESOR = "ASESOR";
    private final String LLAMADA = "LLAMADA";
    private final String CLIENTE = "CLIENTE";

    public long controller(String clientMessage) {
        String[] parts = clientMessage.split(",");
        String asesor_id = parts[0];
        String password = parts[1];
        String numeroCliente = parts[2];
        String operacion = parts[3];
        String ThreadIDMessage = parts[4];

        Manager m = new Manager();
        long ThreadID = 0;

        try {
            if (authentication(asesor_id, password)) {
                if ("1".equals(operacion)) {
                    m.start();
                    ThreadID = m.getId();
                } else {
                    Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
                    for (Thread thread : setOfThread) {
                        if (thread.getId() == Long.parseLong(ThreadIDMessage)) {
                            Manager x = (Manager) thread;
                            String tiempo = x.detenElHilo();
                            System.out.println("ENCONTRADO " + tiempo);
                            thread.stop();
                            Cliente cliente = getClienteXNumberPhoneClient(numeroCliente);
                            Asesor asesor = getAsesor(asesor_id);
                            saveCalls(cliente, asesor, tiempo);
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ThreadID;
    }

    public List<Llamada> getAllCalls(String idAsesor) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        List lLlamada = new ArrayList();
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM  " + this.LLAMADA + " WHERE ASESOR_LLAMADA = ?");
            consulta.setString(1, idAsesor);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                Asesor asesor = new Asesor();
                Cliente cliente = new Cliente();
                Llamada llamada = new Llamada();

                asesor.setIdAsesor(resultado.getString("ASESOR_LLAMADA"));
                cliente.setIdCliente(resultado.getString("CLIENTE_LLAMADA"));

                llamada = new Llamada(resultado.getString("ID_LLAMADA"), asesor, cliente, resultado.getString("TELEFONO_LLAMADA"), resultado.getString("TIEMPO_LLAMADA"));
                lLlamada.add(llamada);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return lLlamada;
    }

    public boolean authentication(String id, String contrasena) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        Asesor asesor = null;
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM " + this.ASESOR + " WHERE  ID_ASESOR = ? AND CONTRASENA = ?");
            consulta.setString(1, id);
            consulta.setString(2, contrasena);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                asesor = new Asesor(resultado.getString("ID_ASESOR"), resultado.getString("USERNAME"), resultado.getString("NOMBRE_ASESOR"), resultado.getString("APELLIDO_ASESOR"), resultado.getString("TELEFONO_ASESOR"), resultado.getString("CONTRASENA"));
            }

        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return contrasena.equals(asesor.getContrasena());
    }

    public List<Cliente> getClientsNotAdviced() throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        List lClientes = new ArrayList();
        Cliente cliente = null;
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM  " + this.CLIENTE + " WHERE ASESORADO = ? AND LIMIT 0, 5");
            consulta.setInt(1, 0);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                cliente = new Cliente(resultado.getString("ID_CLIENTE"), resultado.getString("NOMBRE_CLIENTE"), resultado.getString("APELLIDO_CLIENTE"), resultado.getString("TELEFONO_CLIENTE"), resultado.getString("CATEGORIA"), resultado.getInt("ASESORADO"));
                lClientes.add(cliente);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return lClientes;
    }

    public void saveCalls(Cliente cliente, Asesor asesor, String tiempoLlamada) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        System.out.println("Insert ( " + String.valueOf((int) (Math.random() * 9999999) + 1)
                + " , " + asesor.getIdAsesor()
                + " , " + cliente.getIdCliente()
                + " , " + cliente.getTelefono()
                + " , " + tiempoLlamada + " )");
        try {
            PreparedStatement query = conexion.prepareStatement("INSERT INTO " + this.LLAMADA + " (ID_LLAMADA, ASESOR_LLAMADA, CLIENTE_LLAMADA, TELEFONO_LLAMADA, TIEMPO_LLAMADA) VALUES (?,?,?,?,?)");
            query.setString(1, String.valueOf((int) (Math.random() * 9999999) + 1));
            query.setString(2, asesor.getIdAsesor());
            query.setString(3, cliente.getIdCliente());
            query.setString(4, cliente.getTelefono());
            query.setString(5, tiempoLlamada);
            query.executeQuery();
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public Cliente getClienteXNumberPhoneClient(String number) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        Cliente cliente = null;
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM  " + this.CLIENTE + " WHERE TELEFONO_CLIENTE = ?");
            consulta.setString(1, number);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                cliente = new Cliente(resultado.getString("ID_CLIENTE"), resultado.getString("NOMBRE_CLIENTE"), resultado.getString("APELLIDO_CLIENTE"), resultado.getString("TELEFONO_CLIENTE"), resultado.getString("CATEGORIA"), resultado.getInt("ASESORADO"));
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return cliente;
    }

    public Asesor getAsesor(String id) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        Asesor asesor = null;
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM  " + this.ASESOR + " WHERE ID_ASESOR = ?");
            consulta.setString(1, id);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                asesor = new Asesor(resultado.getString("ID_ASESOR"), resultado.getString("USERNAME"), resultado.getString("NOMBRE_ASESOR"), resultado.getString("APELLIDO_ASESOR"), resultado.getString("TELEFONO_ASESOR"), resultado.getString("CONTRASENA"));
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return asesor;
    }

}
