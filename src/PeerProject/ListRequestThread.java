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
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListRequestThread extends Thread {

    private final int MAX_TIME = 3000;
    private final DownloadListPool listDownload;
    private PeerInfo peerInfo = null;

    public ListRequestThread() {

        listDownload = new DownloadListPool();
    }

    @Override
    public void run() {
        this.Request();
        listDownload.start();
        this.interrupt();

    }

    private void Request() {

        MulticastSocket requestSocket = null;
        DatagramPacket packet = null;
        InetAddress group = null;
        byte[] buf = null;
        String newListRequest;

        newListRequest = String.valueOf(Configs.getANSWER_PORT());

        try {
            buf = new byte[256];

            requestSocket = new MulticastSocket(Configs.getGROUP_PORT());
            group = InetAddress.getByName(Configs.getGROUP_ADDRESS());

            buf = newListRequest.getBytes();

            packet = new DatagramPacket(buf, buf.length, group, Configs.getGROUP_PORT());
            requestSocket.send(packet);

            requestSocket.close();

            this.WaitResponse();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void WaitResponse() {

        ServerSocket answerSocket = null;
        Socket portInfoSocket = null;
        Boolean accepting = true;
        Boolean existPeers = false;

        try {

            answerSocket = new ServerSocket();
            answerSocket.setReuseAddress(true);
            answerSocket.bind(new InetSocketAddress(Configs.getANSWER_PORT()));

            System.out.println(":Downloading lists . . .");
            GlobalList.emptyList();
           
            while (accepting) {

                answerSocket.setSoTimeout(MAX_TIME);
                portInfoSocket = answerSocket.accept();
                BufferedReader inFromPeer = new BufferedReader(new InputStreamReader(portInfoSocket.getInputStream()));

                String received = inFromPeer.readLine();

                received = received.trim();

                if (received != null) {
                    existPeers = true;
                    peerInfo = new PeerInfo(portInfoSocket.getInetAddress().getHostAddress(), Integer.parseInt(received));
                }

                synchronized (listDownload) {
                    listDownload.addPeerInfo(peerInfo);
                    listDownload.notify();
                }

                portInfoSocket.close();
               
            } 

        } catch (SocketTimeoutException t) {
            try {
                if (existPeers) {
                    System.out.println("!Complete, not accepting more files list");
                } else {
                    System.out.println("!No Peers avaiable for downloading lists");
                }
                
                answerSocket.close();
                accepting = false;
            } catch (IOException ex) {
                Logger.getLogger(ListRequestThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(ListRequestThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
