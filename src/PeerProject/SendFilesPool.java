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

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendFilesPool extends Thread {

    private final int MAXIMUM_CONNECTIONS = 5;
    private final ArrayList<Socket> socketsToSend;
    private final ArrayList<String> fileNames;

    public SendFilesPool() {
        socketsToSend = new ArrayList<>();
        fileNames = new ArrayList<>();
    }

    @Override
    public void run() {

        synchronized (this) {

            ExecutorService executor = Executors.newFixedThreadPool(MAXIMUM_CONNECTIONS);

            SendFileThread sendFile;

            while (true) {

                try {

                    wait();

                    if (!socketsToSend.isEmpty()) {

                        sendFile = new SendFileThread(socketsToSend.get(0), fileNames.get(0));
                        executor.execute(sendFile);
                        socketsToSend.remove(0);
                        fileNames.remove(0);
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(SendFilesPool.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public void addSocket(Socket newSocket, String fileName) {
        socketsToSend.add(newSocket);
        fileNames.add(fileName);

    }

}
