package co.com.server;

import co.com.controlador.Controlador;
import java.io.IOException;
import static java.lang.Thread.yield;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final int PUERTO = 12345;
    static String numero = "1985";
    static String resp = "";
    static Random random = new Random();
    static int aleatorio;
    static int nMenor;
    static int promedio;

    public static void main(String[] args) {
        DatagramSocket conexion;
        DatagramPacket entrada;
        DatagramPacket salida;
        byte[] datoEntrada;
        byte[] datoSalida;
        String mensaje;

        System.out.println("Inicializando servidor. . .");

        try {
            conexion = new DatagramSocket(PUERTO);
            while (true) {
                datoEntrada = new byte[1024];
                entrada = new DatagramPacket(datoEntrada, datoEntrada.length);
                conexion.receive(entrada);
                mensaje = new String(entrada.getData(), 0, entrada.getLength());
                
                
                
                Controlador controlador = new Controlador();
                long respuesta = controlador.controller(mensaje);
                
                
                datoSalida = (respuesta+"").getBytes();

                salida = new DatagramPacket(datoSalida, datoSalida.length, entrada.getAddress(), entrada.getPort());
                conexion.send(salida);
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
