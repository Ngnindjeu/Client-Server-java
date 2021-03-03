package pis.hue2.server;

import pis.hue2.common.Instruction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * in dieser klasse wird der server gestartet
 *   @author Ngnindjeu und Priso
 *  @version 14.0.1
 */

public class LaunchServer {
    /**
     * Deklaration der Variable socket, sc und number
     * Die, die Verbindung zwichen dem Server und einem Client ermoeglicht
     * number steht fuer die Anzahl der angemeldeten Clients
     */
    Socket socket;
    ServerSocket sc;
    int number;
    /**
     * Konstruktor der Klasse
     */

    public LaunchServer(){
        Serverbleibtconnect();

    }
    /**
     * Die Methode Serverbleibtconnect ermoeglicht die Herstellung der ServerSocket und
     * warte auf einem Client fuer eine moegliche Verbindung.
     */

    public  void Serverbleibtconnect(){
        try {
            sc = new ServerSocket(2807);  // creer la communikation entre le client et le serveur
            while (true){

                socket= sc.accept();
                number++;


                Thread clientverwalterthread = new Thread(()->{
                    new ClientVerwalter(socket, number);
                });
                clientverwalterthread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main methode wo der Server gelaucht wird
     * @param args abkurzung von argument parameter der main methode
     */
    public static void main(String[] args) {
        LaunchServer ls = new LaunchServer();


    }





}
