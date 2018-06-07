package co.com.cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cliente {

    private static final int PUERTO = 12345;
    
    public static void main(String[] args) {
        try {
            InetAddress IP = InetAddress.getByName("localHost");
            //InetAddress IP = InetAddress.getByName("10.124.13.141");
            DatagramSocket conexion;
            DatagramPacket entrada;
            DatagramPacket salida;
            byte[] datoSalida;
            byte[] datoEntrada;
            String mensaje;

            Scanner teclado = new Scanner(System.in);

            System.out.println("- - - - - -Inicializando cliente- - - - - - - - - - - \n ");
            conexion = new DatagramSocket();
            while (true) {
                datoSalida = teclado.nextLine().getBytes();
                salida = new DatagramPacket(datoSalida, datoSalida.length, IP, PUERTO);
                conexion.send(salida);
                datoEntrada = new byte[1024];
                entrada = new DatagramPacket(datoEntrada, datoEntrada.length);
                conexion.receive(entrada);
                mensaje = new String(entrada.getData(), 0, entrada.getLength());
                System.out.println("Servidor:  " + mensaje + "\n");
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String concatenarValores(String valor){
        System.out.println("");
        return "";
    }
    
}
