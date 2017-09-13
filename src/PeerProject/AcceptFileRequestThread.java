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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptFileRequestThread extends Thread {

    private final ArrayList<String> fileList = new ArrayList();
    private ServerSocket serverSocket;
    private final SendFilesPool fileSendPool = new SendFilesPool();

    public AcceptFileRequestThread() {
        fileSendPool.start();
        System.out.println("!Listening for incoming files requests on port " + Configs.getDOWNLOAD_PORT());
    }

    @Override
    public void run() {
        openServerSocket();

        Socket socket;

        while (true) {

            try {

                socket = serverSocket.accept();

                if (socket != null) {

                    buildFileList();

                    BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String fileName = inFromPeer.readLine();

                    if (existFile(fileName)) {

                        //TODO: run communication protocol here
                        synchronized (fileSendPool) {
                            fileSendPool.addSocket(socket,fileName);
                            fileSendPool.notify();
                        }
                    }

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
            this.serverSocket = new ServerSocket(Configs.getDOWNLOAD_PORT());
        } catch (IOException e) {
            System.out.println("Can't open port " + Configs.getDOWNLOAD_PORT());
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

    private boolean existFile(String fileName) {

        return fileList.contains(fileName);

    }

}
