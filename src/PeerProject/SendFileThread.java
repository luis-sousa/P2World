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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SendFileThread implements Runnable {

    private final Socket socket;
    private final String fileName;

    public SendFileThread(Socket newRequest, String fileName) {
        this.socket = newRequest;
        this.fileName = fileName;
    }

    @Override
    public void run() {

        try {

            CommunicationProtocol cProtocol = new CommunicationProtocol();

            System.out.println("!File " + fileName + " requested for sending");

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String userInput;
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(cProtocol.processInput(""));

            String output;

            while ((userInput = stdIn.readLine()) != null) {

                output = cProtocol.processInput(userInput);

                out.println(output);

                if (output.contains("Accept")) {
                    System.out.println("!Sending " + fileName);
                    sendFile();
                    break;
                }

                if (output.contains("Canceled")) {
                    System.out.println("!Download canceled by requester order");
                    socket.close();
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void sendFile() throws IOException {
        try {

            // FILE TRANSFER
            FileInputStream fileInputStream = new FileInputStream(Configs.getPATH() + Configs.getFOLDER() + fileName);
            byte[] buffer = new byte[socket.getSendBufferSize()];
            int send = 0;
            int sendTotal = 0;

            while ((send = fileInputStream.read(buffer)) != -1) {
                sendTotal = send + sendTotal;

                socket.getOutputStream().write(buffer, 0, send);
            }
            float total = sendTotal / 1048576;

            System.out.println(":Total Sent: " + total + " MB");

            socket.getOutputStream().flush();
            fileInputStream.close();
            socket.close();

            System.out.println("!Complete sending " + fileName);
        } catch (SocketException ex) {
            Logger.getLogger(SendFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
