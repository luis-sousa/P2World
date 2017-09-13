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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Peer {

    public static void main(String[] args) throws IOException {

        /**
         * ######### !! IMPORTANT !! ######### 
         * 
         * If you are running multiple Peers in the same machine
         * please change your settings in Configs.java
         *
         * |Settings to change FOLDER, ANSWER_PORT, DOWNLOAD_PORT, LIST_PORT
         */
        ListRequestThread newListRequest;
        AcceptFileRequestThread fileRequests;
        DownloadFilePool downloadFilesPool;
        GetListRequestThread listRequests;
        Scanner reader = new Scanner(System.in);
        int menu_option;
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String fileToDownload;

        //STARTING THREADS
        listRequests = new GetListRequestThread();
        listRequests.start();

        downloadFilesPool = new DownloadFilePool();
        downloadFilesPool.start();

        fileRequests = new AcceptFileRequestThread();
        fileRequests.start();

        // DISPLAY MENU
        do {
            System.out.println("\n==========# Main Menu #==========");
            System.out.println();
            System.out.println("1 - Download a File");
            System.out.println();
            System.out.println("0 - Exit");
            System.out.println();
            System.out.println("=================================");

            try {
                menu_option = Integer.parseInt(reader.nextLine());

            } catch (NumberFormatException ne) {
                menu_option = -1;
            }

            switch (menu_option) {
                case 1:
                    
                    newListRequest = new ListRequestThread();
                    newListRequest.start();
                    while (newListRequest.isAlive()) {
                    
                    }
                    
                    //Waiting to fill the Global List with data
                    try {
                                Thread.sleep(3000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                    
                    if (!GlobalList.empty()) {
                        GlobalList.print();
                        System.out.print("Download: ");
                        fileToDownload = bufferRead.readLine();

                        if (GlobalList.exist(fileToDownload)) {

                            Request newRequest = GlobalList.newRequest(fileToDownload);

                            synchronized (downloadFilesPool) {
                                downloadFilesPool.addRequest(newRequest);
                                downloadFilesPool.notify();
                            }

                            // A timer to give the user the time to complete the communication protocol
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            System.out.println("\n!File not found");
                        }
                    }
                    break;
                case 0:
                    System.out.println("\n!Good Bye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("\n!Not a valid option");
                    break;
            }

        } while (menu_option != 0);

    }

}
