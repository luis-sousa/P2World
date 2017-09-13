/**
 * P2World @ Sistemas Distribuidos, ESTGF 2013/14
 *
 * PEER TO PEER FILE TRANSMISSION APPLICATION
 *
 * Developed by
 *
 * 8090228, Luís Sousa and 8090242, Ricardo Barbosa
 */

package PeerProject;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.*;

public class AcceptListRequestThread extends Thread {
    
    private final ArrayList<String> fileList = new ArrayList();
    private ServerSocket serverSocket;

    public AcceptListRequestThread() {

        System.out.println("!Listening for incoming requests on port " + Configs.getLIST_PORT());
    }

    @Override
    public void run() {
        openServerSocket();

        Socket clientSocket;

        while (true) {

            try {

                clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    
                    buildFileList();

                    sendFileList(clientSocket);

                }

            } catch (IOException ex) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(AcceptListRequestThread.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

    }

    private void openServerSocket() {

        try {
            this.serverSocket = new ServerSocket(Configs.getLIST_PORT());
        } catch (IOException e) {
            System.out.println("!Can't open port " + Configs.getLIST_PORT());
        }

    }

    private void buildFileList() {

        fileList.removeAll(fileList);
        File[] files = null;

        if ((files = new File(Configs.getPATH() + Configs.getFOLDER()).listFiles()) != null) {

            for (File file : files) {
                if (file.isFile()) {

                    fileList.add(file.getName());
                }
            }
        }
    }

    private void sendFileList(Socket clientSocket) throws IOException {

        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        
        System.out.println("!File list sent to " + clientSocket.getInetAddress().getHostAddress() + "|" + clientSocket.getLocalAddress().getHostName());
        
        
        out.writeBytes(Configs.getDOWNLOAD_PORT() + "\n");
        
        
        for (int i = 0; i < fileList.size(); i++) {
            out.writeBytes(fileList.get(i).toString() + "\n");
        }
       
        out.close();

    }
    
}
