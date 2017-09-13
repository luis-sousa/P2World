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

public class GlobalList {

    private static ArrayList<PartialList> GLOBAL_LIST = new ArrayList<>();

    protected GlobalList() {
    }

    public static void addNew(PartialList newPartialList) {
        GLOBAL_LIST.add(newPartialList);
    }

    public static void print() {

        System.out.println("----------- FILES -----------");
        if (GLOBAL_LIST.isEmpty()) {
            System.out.println("!No files to display");
        } else {

            for (PartialList partialList : GLOBAL_LIST) {
                for (String fileName : partialList.getFiles()) {
                    System.out.println(fileName);
                }

            }
        }
        System.out.println("-----------------------------");

    }

    public static boolean empty() {
        if (!GLOBAL_LIST.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean exist(String fileName) {
        boolean found = false;

        for (PartialList partialList : GLOBAL_LIST) {
            for (String name : partialList.getFiles()) {
                if (partialList.exist(fileName)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    public static Request newRequest(String fileName) {
        Request newRequest = new Request();

        for (PartialList partialList : GLOBAL_LIST) {
            for (String name : partialList.getFiles()) {
                if (partialList.exist(fileName)) {
                    newRequest.setFileName(fileName);
                    newRequest.setAddress(partialList.getAddress());
                    newRequest.setPort(partialList.getPort());
                    break;
                }
            }
        }

        return newRequest;
    }
    
    public static void emptyList(){
        GLOBAL_LIST.removeAll(GLOBAL_LIST);
    }

}
