package co.com.server;

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
        Hilo hilo = new Hilo();
        int[] numeros =new int[4];
        
        

        System.out.println("Inicializando servidor. . .");

        try {
            conexion = new DatagramSocket(PUERTO);
            while (true) {
                datoEntrada = new byte[1024];
                entrada = new DatagramPacket(datoEntrada, datoEntrada.length);
                conexion.receive(entrada);
                mensaje = new String(entrada.getData(), 0, entrada.getLength());

                String var [] = obtenerNM(mensaje);
                String n = var [0];
                String m = var [1];
                
                int matrizRespuesta[][] = generarMatriz(Integer.parseInt(n), Integer.parseInt(m));
                
                hilo.run(numeros);
                
                int x = Integer.parseInt(n);
                int y = Integer.parseInt(m);
                System.out.println("x: "+ x + " y "  + y);
                
                int arrayPromedio[] = new int[x];
                int arrayNMenor[] = new int[x];
                
                for (int i = 0; i < x; i++) {
                    int filaParametro[] = new int[y];
                    for (int j = 0; j < y; j++) {
                        filaParametro[j] = matrizRespuesta[i][j];
                    }
                    hilo.run(filaParametro);
                    arrayPromedio[i] = promedio;
                    arrayNMenor[i] = nMenor;
                }
                
                String matriz = "";
                String vProm = "";
                String vNMenor = "";
                
                System.out.println(" imprimir matriz  ");
                
                for (int i = 0; i < x; i++) {
                    for (int j = 0; j < y; j++) {
                        //System.out.println(" | "+matrizRespuesta[i][j]+" | ");
                        matriz += " | "+matrizRespuesta[i][j]+" | ";
                    }
                    //System.out.println("  \n");
                    matriz += "  \n";
                     
                }
                
                System.out.println(" arrayPromedio  ");
                
                for (int i = 0; i < arrayPromedio.length; i++) {
                    //System.out.println(" | "+ arrayPromedio[i] +  " | ");
                    vProm += " | "+ arrayPromedio[i] +  " | ";
                    
                }
                
                System.out.println(" arrayNMenor  ");
                
                for (int i = 0; i < arrayNMenor.length; i++) {
                    //System.out.println(" | "+ arrayNMenor[i] +  " | ");
                    vNMenor += " | "+ arrayNMenor[i] +  " | ";
                }
                
                datoSalida = ("Matriz: \n"+matriz+"\n Vector numeros menores \n"+vNMenor+" \n Vector promedios \n"+vProm).getBytes();

                salida = new DatagramPacket(datoSalida, datoSalida.length, entrada.getAddress(), entrada.getPort());
                conexion.send(salida);
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String[] obtenerNM(String mensaje){
        String coordenadas[] = mensaje.split("x");
        return coordenadas;
    }
    
    public static int [][] generarMatriz (int n, int m){
        int [][] matriz = new int [n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matriz[i][j] = random.nextInt(50) + 1;
            }
        }
        return matriz;
    }

    public static class Hilo extends Thread {

        public void run(int fila []) {
            System.out.println("Calculando fila ");
            promedio = calcularPromedio(fila);
            nMenor = calcularNumeroMenor(fila);
            yield();
        }
        
        public int calcularPromedio(int fila[]){
            int promedio = 0;
            int sumatoria = 0;
            for (int i = 0; i < fila.length; i++) {
                sumatoria += fila[i];
            }
            //System.out.println("sumatoria: "+ sumatoria+" divido: "+ fila.length );
            promedio = sumatoria / fila.length;
            return promedio;
        }
        
        public int calcularNumeroMenor(int fila[]){
            int nMenor = 9999;
            for (int i = 0; i < fila.length; i++) {
                if(nMenor > fila[i]){
                    nMenor = fila[i];
                }
            }
            return nMenor;
        }
    }


    

}
