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
import java.util.*;


public class GetListRequestThread extends Thread {

    private final AcceptListRequestThread acceptRequest = new AcceptListRequestThread();

    private MulticastSocket mSocket = null;
    private Socket cSocket = null;
    private DatagramPacket receivedPacket = null;
    private byte[] buf = new byte[256];
    private InetAddress peerNetwork = null;

    public GetListRequestThread() {
        System.out.println("!Waiting for file list requests from group " + Configs.getGROUP_ADDRESS() + " on port " + Configs.getGROUP_PORT());
    }

    @Override
    public void run() {

        acceptRequest.start();

        try {
            mSocket = new MulticastSocket(Configs.getGROUP_PORT());
            peerNetwork = InetAddress.getByName(Configs.getGROUP_ADDRESS());
            mSocket.joinGroup(peerNetwork);

            String clientPort;

            while (true) {
                buf = new byte[256];
                receivedPacket = new DatagramPacket(buf, buf.length);
                mSocket.receive(receivedPacket);

                clientPort = new String(receivedPacket.getData()).trim();

                if (Integer.parseInt(clientPort) != Configs.getANSWER_PORT()) {

                    System.out.println(":Peer " + receivedPacket.getAddress().getHostAddress() + " requested for a file list to port " + clientPort);

                    //Sending the PORT number to open a socket for the donwload
              
                    String listPortInfo = String.valueOf(Configs.getLIST_PORT());

                    cSocket = new Socket(receivedPacket.getAddress().getHostAddress(), Integer.parseInt(clientPort));

                    DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
                    out.writeBytes(listPortInfo);

                    cSocket.close();

                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

}
