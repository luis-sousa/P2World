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

public class DownloadListPool extends Thread {

    private final int MAXIMUM_CONNECTIONS = 5;
    private final ArrayList<PeerInfo> peerInfoList;

    public DownloadListPool() {
        peerInfoList = new ArrayList<>();
    }

    @Override
    public void run() {

        synchronized (this) {

            ExecutorService executor = Executors.newFixedThreadPool(MAXIMUM_CONNECTIONS);

            GetPartialListThread listRequest;

            while (true) {

                if (!peerInfoList.isEmpty()) {

                    listRequest = new GetPartialListThread(peerInfoList.get(0));
                    executor.execute(listRequest);
                    peerInfoList.remove(0);

                } else {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DownloadListPool.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }

    }

    public void addPeerInfo(PeerInfo newPeerInfo) {

        peerInfoList.add(newPeerInfo);

    }

}
