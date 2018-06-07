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
public class Hilo extends Thread { //una clase que hereda de la clase Thread

    public Hilo() {// Contructor porque la clase es heredada 
        super();
    }

    public void run() {
        int nuMin = 0; //El Contador de minutos
        int nuSeg = 0; //El Contador de de segundos
        int nuHora = 0; //El Contador de Horas   
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

    public static void main(String[] args) {
        Hilo c = new Hilo(); //Intancio la clase
        c.start(); //Ejecuto el metodo run del Thread        
    }// Fin main
}//Fin Clase
