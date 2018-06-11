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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author chori
 */
public class Controlador {

    private final String ASESOR = "ASESOR";
    private final String LLAMADA = "LLAMADA";
    private final String CLIENTE = "CLIENTE";

    public Map<String, Object> receiveMessage(String message) {
        Map<String, Object> response = new HashMap<String, Object>();
        Map<String, Object> valores = new HashMap<String, Object>();
        Cliente cliente = null;
        String[] parts = message.split(",");
        try {
            if (null != parts[0]) {
                switch (parts[0]) {
                    case "autenticar":
                        System.out.println("autenticar");
                        String id_asesor = parts[1];
                        String password = parts[2];
                        boolean validate = authentication(id_asesor, password);
                        if (validate) {
                            cliente = getClientNotAdviced();
                            valores.put("authentication", "True");
                            valores.put("cliente", cliente);
                        } else {
                            valores.put("authentication", "False");
                        }
                        response.put("rAutenticar", valores);
                        break;
                    case "llamar":
                        System.out.println("llamar");
                        Manager m = new Manager();
                        long ThreadID = 0;
                        m.start();
                        ThreadID = m.getId();
                        valores.put("ThreadID", ThreadID);
                        response.put("rLlamar", valores);
                        break;
                    case "colgar":
                        System.out.println("colgar");
                        String idAsesor = parts[1];
                        String telefonoCliente = parts[2];
                        String categoriaCliente = parts[3];
                        String ThreadIDMessage = parts[4];
                        Set<Thread> setOfThread = Thread.getAllStackTraces().keySet();
                        for (Thread thread : setOfThread) {
                            if (thread.getId() == Long.parseLong(ThreadIDMessage)) {
                                Manager x = (Manager) thread;
                                String tiempo = x.detenElHilo();
                                System.out.println("ENCONTRADO " + tiempo);
                                thread.stop();
                                Cliente clientSaved = getClienteXNumberPhoneClient(telefonoCliente);
                                Asesor asesor = getAsesor(idAsesor);

                                String costoLlamada = calcularCostoLlamada(categoriaCliente, tiempo) + "";
                                saveCalls(clientSaved, asesor, tiempo, costoLlamada);
                                //modificar registro de cliente a llamado
                                cliente = getClientNotAdviced();
                                valores.put("costo", costoLlamada);
                                valores.put("cliente", cliente);
                                response.put("rColgar", valores);
                            }
                        }
                        System.out.println("colgando");
                        break;
                    default:
                        break;
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
    
    public int calcularCostoLlamada(String categoria, String tiempo){
        String[] parts = tiempo.split(":");
        int horas = Integer.parseInt(parts[0]);
        int minutos = Integer.parseInt(parts[1]);
        int segundos = Integer.parseInt(parts[2]);
        int h = horas*3600;
        int m = minutos*60;
        int s = h+m+segundos;
        int msxll = 0;
        int vpps = 0;
        int valor = 0;
        
        if (null != categoria)switch (categoria) {
            case "1":
                msxll = 10;
                vpps = 10;
                valor = (s/msxll)*vpps;
                break;
            case "2":
                msxll = 15;
                vpps = 15;
                valor = (s/msxll)*vpps;
                break;
            case "3":
                msxll = 20;
                vpps = 20;
                valor = (s/msxll)*vpps;
                break;
            default:
                break;
        }
        return valor;
    }

    public String getClientToMap(Map<String, Object> input) {
        String response = "";
        if (null != input.get("rAutenticar")) {
            Map<String, Object> valores = new HashMap<>();
            valores = (Map<String, Object>) input.get("rAutenticar");
            Cliente cliente = (Cliente) valores.get("cliente");
            String authentication = (String) valores.get("authentication");
            response += "rAutenticar," + cliente.getNombre() + " " + cliente.getApellido() + ","
                    + cliente.getTelefono() + ","
                    + cliente.getCategoria() + ","
                    + authentication;
            return response;
        } else if (null != input.get("rLlamar")) {
            Map<String, Object> valores = new HashMap<>();
            valores = (Map<String, Object>) input.get("rLlamar");
            response += "rLlamar," + valores.get("ThreadID");

        } else if (null != input.get("rColgar")) {
            Map<String, Object> valores = new HashMap<>();
            valores = (Map<String, Object>) input.get("rColgar");
            Cliente cliente = (Cliente) valores.get("cliente");
            response += "rColgar," + valores.get("costo") + ","
                    + cliente.getNombre() + " " + cliente.getApellido() + ","
                    + cliente.getTelefono() + ","
                    + cliente.getCategoria();
        }
        return response;
    }

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
                            //saveCalls(cliente, asesor, tiempo);
                            
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

                asesor.setIdAsesor(resultado.getString("ASESOR_LLAMADA"));
                cliente.setIdCliente(resultado.getString("CLIENTE_LLAMADA"));

                Llamada llamada = new Llamada(resultado.getString("ID_LLAMADA"), asesor, cliente, resultado.getString("TELEFONO_LLAMADA"), resultado.getString("TIEMPO_LLAMADA"), resultado.getString("VALOR_LLAMADA"));
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

    public Cliente getClientNotAdviced() throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        Cliente cliente = null;
        try {
            PreparedStatement consulta = conexion.prepareStatement("SELECT * FROM  " + this.CLIENTE + " WHERE ASESORADO = ? AND rownum = 1");
            consulta.setInt(1, 0);
            ResultSet resultado = consulta.executeQuery();
            while (resultado.next()) {
                System.out.println("Resultado: " + resultado.getString("ID_CLIENTE"));
                cliente = new Cliente(resultado.getString("ID_CLIENTE"), resultado.getString("NOMBRE_CLIENTE"), resultado.getString("APELLIDO_CLIENTE"), resultado.getString("TELEFONO_CLIENTE"), resultado.getString("CATEGORIA"), resultado.getInt("ASESORADO"));
            }
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
        return cliente;
    }

    public void saveCalls(Cliente cliente, Asesor asesor, String tiempoLlamada, String valorLlamada) throws SQLException, ClassNotFoundException {
        Connection conexion = Conexion.obtener();
        System.out.println("Insert ( " + String.valueOf((int) (Math.random() * 9999999) + 1)
                + " , " + asesor.getIdAsesor()
                + " , " + cliente.getIdCliente()
                + " , " + cliente.getTelefono()
                + " , " + tiempoLlamada + " )");
        try {
            PreparedStatement query = conexion.prepareStatement("INSERT INTO " + this.LLAMADA + " (ID_LLAMADA, ASESOR_LLAMADA, CLIENTE_LLAMADA, TELEFONO_LLAMADA, TIEMPO_LLAMADA, VALOR_LLAMADA) VALUES (?,?,?,?,?,?)");
            query.setString(1, String.valueOf((int) (Math.random() * 9999999) + 1));
            query.setString(2, asesor.getIdAsesor());
            query.setString(3, cliente.getIdCliente());
            query.setString(4, cliente.getTelefono());
            query.setString(5, tiempoLlamada);
            query.setString(6, valorLlamada);
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
