package pis.hue2.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Ngnindjeu und Priso
 * @version 14.0.1
 * Der klasse GuiClient kümmert sich um Grafische Oberflasche der Client
 */
public class GuiClient extends JFrame {
    private JButton lst, put, get, del, con, dec, ack, dat;
    private JLabel  filename, serveranswer;
    private JTextArea fileserver, fileclient, serverJF;
    private JTextField  filenameJF;
    private JScrollPane scrollpane_fileServer, scrollpane_fileclien, scrollpane_serveranswer;
    private Container pane;
    private LaunchClient client;

    /**
     * konstrucktor der GuiClient hier wird alles geladen
     */
    public GuiClient() {
        client = new LaunchClient();
        button_setting();
        label_setting();
        jfieldsetting();
        textarea_setting_mit_ScrollPane();
        component_positionierung();
        guiSetting();
        add_in_Container();
        event_setting();

        enableAllbutton(false);
    }

    /**
     * hier wird der Rahmen der Gui definiert
     */

    public void guiSetting() {
        pane = this.getContentPane();//container
        pane.setLayout(null);//ich wollte die Positionierung selber machen
        this.setTitle("Client-Gui");
        this.setSize(420, 650);
        this.setResizable(false);// dimensionierung
        this.setAlwaysOnTop(true);// fordergrund
        this.setLocationRelativeTo(null);//center
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);//visibilität des Fensters
    }

    /**
     * alle Tasten der Gui werden hier hergestellt
     */

    public void button_setting() {
        lst = new JButton("LST");
        put = new JButton("PUT");
        get = new JButton("GET");
        del = new JButton("DEL");
        con = new JButton("CON");
        dec = new JButton("DSC");
        ack = new JButton("ACK");
        dat = new JButton("DAT");
    }

    /**
     *Mit hilfe dieser Methode wird die Label severname und filename hergestellt
     */

    public void label_setting() {

        serveranswer = new JLabel("Answer vom Server: ");
        filename = new JLabel("File_Name:");

    }

    /**
     *Kuemmert sich um die Rahmen der filename
     */

    public void jfieldsetting() {

        filenameJF = new JTextField();

        filenameJF.setBackground(new Color(234, 217, 68));
        filenameJF.setForeground(new Color(36, 59, 3));

    }

    /**
     *hier diese Methode erlaubt uns der fileserver fileclient und Serveranswer zu scrollen
     */

    public void textarea_setting_mit_ScrollPane() {
        serverJF = new JTextArea();
        serverJF.setBackground(new Color(118, 232, 210));
        serverJF.setForeground(new Color(7, 33, 123));

        fileserver = new JTextArea("Server File: ");
        fileclient = new JTextArea("Client File: ");

        scrollpane_serveranswer = new JScrollPane(serverJF);
        scrollpane_fileclien = new JScrollPane(fileclient);
        scrollpane_fileServer = new JScrollPane(fileserver);

    }

    /**
     * in diese Methode component_positionierung wird alle tasten der Gui wie ich definiert habe positioniert
     * dh alle Abmessungen werden hier definiert und können auch verändert werden
     */

    public void component_positionierung() {
        lst.setBounds(0, 0, 200, 50);

        put.setBounds(200, 0, 200, 50);

        get.setBounds(0, 50, 200, 50);

        del.setBounds(200, 50, 200, 50);

        ack.setBounds(0, 100, 200, 20);
        dat.setBounds(200, 100, 200, 20);

        serveranswer.setBounds(0, 130, 150, 20);
        scrollpane_serveranswer.setBounds(160, 130, 200, 100);
        filename.setBounds(0, 240, 100, 20);
        filenameJF.setBounds(160, 240, 150, 20);

        scrollpane_fileclien.setBounds(0, 270, 200, 300);
        scrollpane_fileServer.setBounds(220, 270, 200, 300);
        con.setBounds(0, 570, 100, 50);
        dec.setBounds(200, 570, 100, 50);


    }

    /**
     * hier wird alle componente in container hinzugefugt
     */

    public void add_in_Container() {
        pane.add(lst);

        pane.add(put);

        pane.add(get);

        pane.add(del);

        pane.add(con);
        pane.add(dec);
        pane.add(scrollpane_fileServer);
        pane.add(scrollpane_fileclien);
        pane.add(filename);
        pane.add(filenameJF);
        pane.add(scrollpane_serveranswer);
        pane.add(serveranswer);
        pane.add(ack);
        pane.add(dat);
    }

    /**
     *Alle Tasten sind verbunden, d. h. nach dem Drücken einer von ihnen gibt es ein Ereignis, dem es entspricht
     */
    //add Action-Event
    public void event_setting() {
        //mit anonymer Klasse
        con.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker<String, Void>() {
                    @Override
                    protected String doInBackground() throws Exception {
                        client.send_Nachricht_to_server("CON");
                        enableAllbutton(true);
                        con.setEnabled(false);
                        return client.receive_nachricht_fromServer();
                    }

                    @Override
                    protected void done() {
                        try {
                            String response = get();
                            if(response.equalsIgnoreCase("DND")){
                                enableAllbutton(false);
                            }
                            serverJF.append(response + "\n");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }.execute();


            }
        });

        //mit lamda Ausdruck
        dec.addActionListener(e -> {

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("DSC");
                    return client.receive_nachricht_fromServer();
                }

                @Override
                protected void done() {
                    try {
                        String dsc = get();
                        serverJF.append(dsc+"\n");
                        enableAllbutton(false);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        });

        lst.addActionListener(e -> {

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("LST");
                    return client.receive_nachricht_fromServer();
                }

                @Override
                protected void done() {
                    try {
                        String tmpFileList = get();
                        String[] tmpFile = tmpFileList.split(",");
                        String result = "";
                        for (String file : tmpFile) {
                            result += file + "\n";
                        }
                        serverJF.append("ACK\n");
                        fileserver.append("\n" + result);
                        fileserver.append("********************\n");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }.execute();

        });

        put.addActionListener(e -> {
            new SwingWorker<String, Void>(){
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("PUT");
                    client.send_Nachricht_to_server(filenameJF.getText());
                    if(!filenameJF.getText().isEmpty()){
                        return client.receive_nachricht_fromServer();
                    }
                    return null;

                }

                @Override
                protected void done() {
                    try{
                        String a = get();
                        if (a == null){
                            JOptionPane.showMessageDialog(GuiClient.this, " You must enter a FILE NAME", "Error", JOptionPane.WARNING_MESSAGE);

                        }else{
                            serverJF.append(a+"\n");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute();

        });

        del.addActionListener(e -> {

            new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("DEL");
                    client.send_Nachricht_to_server(filenameJF.getText());
                    if (!filenameJF.getText().isEmpty()) {
                        return client.receive_nachricht_fromServer();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        String a = get();
                        if (a == null) {
                            JOptionPane.showMessageDialog(GuiClient.this, "You must enter the FILE NAME", "Error", JOptionPane.WARNING_MESSAGE);
                        } else {
                            serverJF.append(a+"\n");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();


        });

        get.addActionListener(e -> {

            new SwingWorker<String, Void>(){
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("GET");
                    client.send_Nachricht_to_server(filenameJF.getText());
                    if (!filenameJF.getText().isEmpty()){
                        return client.receive_nachricht_fromServer();
                    }
                    return null;


                }

                @Override
                protected void done() {
                    try{
                        String b = get();
                        if (b == null){
                            JOptionPane.showMessageDialog(GuiClient.this, "You must enter  FILE NAME", "Error", JOptionPane.WARNING_MESSAGE);

                        }else{
                            serverJF.append(b+"\n");
                        }
                    }catch (Exception e){

                    }
                }
            }.execute();

        });

        dat.addActionListener(e -> {
            new SwingWorker<String, Void>(){
                @Override
                protected String doInBackground() throws Exception {
                    client.send_Nachricht_to_server("DAT");
                    client.send_Nachricht_to_server(filenameJF.getText());
                    if (!filenameJF.getText().isEmpty()){
                        return client.receive_nachricht_fromServer();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        String getText = get();
                        if (getText == null){
                            JOptionPane.showMessageDialog(GuiClient.this, "  You must enter a FILE NAME", "Error", JOptionPane.WARNING_MESSAGE);
                        }else{
                            serverJF.append(getText+"\n");
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.execute();
        });


    }

    /**
     * hier wird die folgende bouton an anfang desactiviert und dann activiert wenn man den button con geklicket hat
     * @param setenable ist ein boolean wenn er hat die value false dann sind alle entsprenchende boutons an anfang desactiviert sonst activiert
     */
    // pour que les bouton soient desactive au debut sauf connect
    public void enableAllbutton(boolean setenable) {
        lst.setEnabled(setenable);
        dec.setEnabled(setenable);
        put.setEnabled(setenable);
        get.setEnabled(setenable);
        del.setEnabled(setenable);
        dat.setEnabled(setenable);
        ack.setEnabled(setenable);

    }

    /**
     * in dieser Methode wird the files von der client genommen und in einem file array gespeichert
     */
    public void clientFile(){
        File file = new File("ClientFile/");
        File[] files;
        String fileNames = "\n";
        files = file.listFiles();

        for(File  tmpfile : files){
            fileNames += tmpfile.getName() + "\n";
        }
        fileclient.append(fileNames);
    }



    /**
     * hier wird den code ausgefurt
     * @param args abkuerzung von Argument  parameter der main methode
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GuiClient gc = new GuiClient();
                GuiClient ge = new GuiClient();
                GuiClient gd = new GuiClient();
                gc.clientFile();
                ge.clientFile();
                gd.clientFile();


            }
        });



    }


}