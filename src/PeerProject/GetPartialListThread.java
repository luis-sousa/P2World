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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GetPartialListThread implements Runnable {

    private final PeerInfo peerInfo;
    private PartialList partialList;

    public GetPartialListThread(PeerInfo newPeerInfo) {
        this.peerInfo = newPeerInfo;
    }

    @Override
    public void run() {

        Socket listSocket = null;

        try {

            listSocket = new Socket(peerInfo.getAddress(), peerInfo.getPort());

            readAll(listSocket);

        } catch (IOException e) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
        }

    }

    public void readAll(Socket socket) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String portInfo;
        String fileName;

        //Get the port to download file
        if ((portInfo = reader.readLine()) != null) {

            //Initializes a partial list for THIS peer ADDRESS and PORT
            partialList = new PartialList(peerInfo.getAddress(), Integer.parseInt(portInfo));
        }

        //Get all the files in List
        while ((fileName = reader.readLine()) != null) {
            partialList.AddFile(fileName);
        }

        //FILL THE GLOBAL_LIST
        synchronized(this){
            GlobalList.addNew(partialList);
        }
        
        System.out.println(":Downloaded file list from " + peerInfo.getAddress() + ":" + peerInfo.getPort());        

    }

}
