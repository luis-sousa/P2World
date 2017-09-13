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

public class Configs {

    /**
     * EDIT THIS SETTINGS IF YOU ARE RUNNING MULTIPLE PEERS FROM THE SAME
     * MACHINE
     */
    private static final String FOLDER          = "Peer_1/";
    private static final int    ANSWER_PORT     = 4001;
    private static final int    DOWNLOAD_PORT   = 4101;
    private static final int    LIST_PORT       = 4201;

    /**
     * DEFAULT CONFIGS
     */
    private static final String PATH            = "src/Peers/";
    private static final String GROUP_ADDRESS   = "224.0.0.1";
    private static final int    GROUP_PORT      = 6789;

    protected Configs() {
    }

    public static String getPATH() {
        return PATH;
    }

    public static String getFOLDER() {
        return FOLDER;
    }

    public static int getANSWER_PORT() {
        return ANSWER_PORT;
    }

    public static String getGROUP_ADDRESS() {
        return GROUP_ADDRESS;
    }

    public static int getGROUP_PORT() {
        return GROUP_PORT;
    }

    public static int getDOWNLOAD_PORT() {
        return DOWNLOAD_PORT;
    }

    public static int getLIST_PORT() {
        return LIST_PORT;
    }

}
