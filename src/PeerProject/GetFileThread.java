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
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetFileThread implements Runnable {

    private final Request request;
    private InputStream inputStream;
    private Socket socket;

    public GetFileThread(Request newRequest) {
        this.request = newRequest;
    }

    @Override
    public void run() {

        try {

            socket = new Socket(request.getAddress(), request.getPort());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            //Send file name
            out.writeBytes(request.getFileName() + "\n");

            //ACCEPT FILE
            inputStream = socket.getInputStream();

            
            //START OF THE COMMUNICATION WITH THE PROTOCOL
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            String message;
            System.out.println(in.readLine());

            while ((userInput = stdIn.readLine()) != null) {

                output.println(userInput);
                message = in.readLine();
                System.out.println(message);
                if (message.contains("Accept")) {
                    receiveFile();
                    break;
                }
                if (message.contains("Canceled")) {
                    System.out.println("! Download canceled by your order");
                    break;
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(GetFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void receiveFile() throws IOException {
        
        System.out.println(":Downloading file . . .");
        FileOutputStream fileOs = new FileOutputStream(Configs.getPATH() + Configs.getFOLDER() + request.getFileName());

        //Receive filesize
        byte[] buffer = new byte[socket.getReceiveBufferSize()];

        int read = 0;
        int readTotal = 0;

        while ((read = inputStream.read(buffer)) != -1) {
            readTotal += read;
            fileOs.write(buffer, 0, read);
        }
        float total = readTotal / 1048576;
        System.out.println("!SUCCESS");
        System.out.println(":Total Download: " + total + " MB");
        System.out.println("!File " + request.getFileName() + " saved to " + Configs.getPATH() + Configs.getFOLDER());

        fileOs.close();
        socket.close();
    }

}
