/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.controlador;

/**
 *
 * @author chori
 */
public class Manager extends Thread {

    // boolean que pondremos a false cuando queramos parar el hilo
    private boolean continuar = true;
    private int nuMin = 0; //El Contador de minutos
    private int nuSeg = 0; //El Contador de de segundos
    private int nuHora = 0;

    // metodo para poner el boolean a false.
    public String detenElHilo() {
        continuar = false;
        return "HH:" + nuHora + " mm:" + nuMin + " ss:" + nuSeg;
    }

    // Metodo del hilo
    public void run() {
        // mientras continuar ...
        while (continuar) {
            nuMin = 0; //El Contador de minutos
            nuSeg = 0; //El Contador de de segundos
            nuHora = 0; //El Contador de Horas   
            try {//si ocurre un error al dormir el proceso(sleep(999))
                for (;;) { //inicio del for infinito           
                    if (nuSeg != 59) {//si no es el ultimo segundo
                        nuSeg++; //incremento el numero de segundos                                  
                    } else {
                        if (nuMin != 59) {//si no es el ultimo minuto
                            nuSeg = 0;//pongo en cero los segundos 
                            nuMin++;//incremento el numero de minutos
                        } else {//incremento el numero de horas
                            nuHora++;
                            nuMin = 0;//pongo en cero los minutos
                            nuSeg = 0;//pongo en cero los segundos           
                        }
                    }
                    System.out.println(nuHora + ":" + nuMin + ":" + nuSeg);//Muestro en pantalla el cronometro
                    sleep(999);//Duermo el hilo durante 999 milisegundos(casi un segundo, quintandole el tiempo de proceso)
                }//Fin del for infinito             
            } catch (Exception ex) {
                System.out.println(ex.getMessage());//Imprima el error
            }
        }
    }

}
