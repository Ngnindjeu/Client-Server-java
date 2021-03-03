package pis.hue2.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 *  diese klasse verwaltet alle client die  sich auf dem server verbinden
 *   @author Ngnindjeu und Priso
 *   @version 14.0.1
 */
public class ClientVerwalter {
    /**
     * Deklaration der Klassenvariablen
     */
    Socket socketClient;
    DataOutputStream dataoutputstream;
    DataInputStream datainputstream;
    int num;
/**
 * die methode ClientVerwalter verwaltet der Client auf dem Server
 *
 * @param socketClient ist ein Client der connect sich auf dem Server
 * @param num zaehlt die Anzahl der angemeldeten Clients.
 */

    public ClientVerwalter(Socket socketClient, int num) {
            this.num = num;

        try {
            this.socketClient = socketClient;
            datainputstream = new DataInputStream(socketClient.getInputStream());
            dataoutputstream = new DataOutputStream(socketClient.getOutputStream());


            while (true) {
                String nachrichtausclient = datainputstream.readUTF();

                switch (nachrichtausclient.toUpperCase()) {
                    case "CON": {
                        if (num > 3){
                            send_Nachricht_to_Client("DND");

                        }
                        send_Nachricht_to_Client("ACK");
                        break;
                    }

                    case "LST": {
                        //send_Nachricht_to_Client("ACK");
                        //if (receive_nachricht_fromClient().equalsIgnoreCase("ack")) {
                        File listOfServerFile = new File("ServerFile");
                        File[] files;
                        String fileNames = "";
                        files = listOfServerFile.listFiles();

                        for (File filetmp : files) {
                            fileNames += filetmp.getName() + ",";
                        }
                        send_Nachricht_to_Client(fileNames);
                        break;
                        //}
                    }


                    case "GET": {
                        //while (true) {
                        //send_Nachricht_to_Client("ACK");
                        String f1 = receive_nachricht_fromClient();
                        if ((f1.length()) == 0) {
                            break;
                        } else {
                            File sourceFile = new File("ServerFile/" + f1);
                            File destinationFile = new File("ClientFile/" + f1);

                            if (destinationFile.exists()) {
                                send_Nachricht_to_Client("DND");
                                break;
                            }
                            if (sourceFile.exists()) {
                              //  System.out.println("Ich bin drin");
                                Files.copy(sourceFile.toPath(), destinationFile.toPath());
                                send_Nachricht_to_Client("ACK");


                            } else {
                                send_Nachricht_to_Client("DND");
                                break;
                            }
                        }
                        //}
                        break;
                    }

                    case "DEL": {
                        //send_Nachricht_to_Client("Enter the name of the file");
                        while (true) {
                            String filename = receive_nachricht_fromClient();
                            File file = new File("ServerFile/" + filename);

                            if (file.exists()) {

                                boolean result = file.delete();
                                if (result) {
                                    System.out.println("I'm here");
                                    send_Nachricht_to_Client("ACK");
                                    break;
                                }

                            } else {
                                send_Nachricht_to_Client("DND");
                            }
                        }
                        break;
                    }


                    case "PUT": {
                        //send_Nachricht_to_Client("Enter the name of the File");
                        //while (true) {
                        String f2 = receive_nachricht_fromClient();

                        File sourceOfTheFile = new File("ClientFile/" + f2);
                        File destinationOfTheFile = new File("ServerFile/");
                        boolean fileTransfert = sourceOfTheFile.renameTo(new File(destinationOfTheFile, sourceOfTheFile.getName()));
                        if ((f2.length()) == 0) {
                            break;
                        } else {
                            if (fileTransfert) {
                                send_Nachricht_to_Client("ACK");


                            } else {
                                send_Nachricht_to_Client("DND");
                                break;
                            }
                        }

                        // }
                        break;
                    }

                    case "DAT": {
                        String f2 = receive_nachricht_fromClient();
                        File file1 = new File("ServerFile/" + f2);
                        FileInputStream fileinput = new FileInputStream(file1);
                        DataInputStream dat = new DataInputStream(fileinput);
                        String e = "";
                        if (file1.exists()) {
                            int data = dat.read();
                            while (data != -1) {
                                e = e + data + " ";
                                data = dat.read();
                            }
                            send_Nachricht_to_Client(e);
                            break;
                        } else {
                            break;
                        }


                    }
                    case "DSC": {
                        send_Nachricht_to_Client("DSC");

                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Die Methode send_Nachricht_to_Client entnimmt die Nachricht, die zu senden wird.
     * @param nachricht ist die Nachricht, die gesendet wird.
     *
     */
    public void send_Nachricht_to_Client(String nachricht) {
        try {
            dataoutputstream.writeUTF(nachricht);
            dataoutputstream.flush();//buffer wird freigegeben
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Die Methode receive_nachricht_fromClient gibt die empfagene Nachtricht zurück
     * @return wird die Nachricht, die from Client kommt, zurückgeben.
     */
    public String receive_nachricht_fromClient() {
        String nachricht = "";
        try {
            nachricht = datainputstream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nachricht;
    }
}
