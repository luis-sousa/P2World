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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadFilePool extends Thread {

    private final int MAXIMUM_CONNECTIONS = 5;
    private final ArrayList<Request> requestList;

    public DownloadFilePool() {
        requestList = new ArrayList<>();
    }

    @Override
    public void run() {

        synchronized (this) {

            ExecutorService executor = Executors.newFixedThreadPool(MAXIMUM_CONNECTIONS);

            GetFileThread fileRequest;

            while (true) {
                try {
                    
                    wait();
                    
                    if (!requestList.isEmpty()) {
                        
                        fileRequest = new GetFileThread(requestList.get(0));
                        executor.execute(fileRequest);
                        requestList.remove(0);
                        
                    }
                    
                } catch (InterruptedException ex) {
                    Logger.getLogger(DownloadFilePool.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    public void addRequest(Request newRequest) {
        requestList.add(newRequest);
    }

}
