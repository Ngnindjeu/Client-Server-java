package pis.hue2.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
/**
 * dieser klasse kummert sich um der client zu gelanden
 * @author Ngnindjeu und Priso
 * @version 14.0.1
 */
public class LaunchClient extends Thread {
    Socket socketClient;
    DataOutputStream dataoutputstream;
    DataInputStream datainputstream;
    Scanner scanner = new Scanner(System.in);
    String message,test;

    /**
     * in dieser Methode wird der Client gelanden das heisst gelaucht und es ist auch für die input output verantwortlich
     */
    public LaunchClient(){
        try {
            socketClient = new Socket("localhost", 2807);
            datainputstream = new DataInputStream(socketClient.getInputStream());
            dataoutputstream = new DataOutputStream(socketClient.getOutputStream());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *in dieser Methode wird the Nachricht to the server gesendet
     * @param nachricht der nachricht wird hier gespeichert und zuruckgegeben
     */
    public void send_Nachricht_to_server(String nachricht)  {
        try {
            dataoutputstream.writeUTF(nachricht);
            dataoutputstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *in dieser Methode wird der Nachricht from der server bekommen und gespeichert in der variable nachricht
     * @return  hier wird die nachricht  gespeichert und zuruckgegeben
     */
    public String receive_nachricht_fromServer(){
        String nachricht ="";
        try {
            nachricht = datainputstream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nachricht;
    }


}
