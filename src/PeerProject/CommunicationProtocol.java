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

/*
 * This protocol is a variation of the KnockKnock protocol
 * 
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 */
public class CommunicationProtocol {

    //STATE Values depending on the current cicle of the Protocol
    private static final int WAITING = 0;
    private static final int SENT_QUESTION = 1;

    //FINAL DECISION MESSAGES
    private static final String ACCEPT = "# Accept, sending file";
    private static final String REJECT = "# Canceled by your order";

    //First time the protocol is running, the default state is WAITING for a response to the sent question
    private int state = WAITING;

    public String processInput(String theInput) {
        String theOutput = null;

        if (state == WAITING) {
            //When WAITING, the protocol will do a question
            theOutput = "#Do you want to download the file? (yes/no)";
            
            //Changing the state to SENT_QUESTION after the protocol asked the question
            state = SENT_QUESTION;
            
           
        } else if (state == SENT_QUESTION) {
            //ONLY TWO possible responses for the question
            
            if (theInput.equalsIgnoreCase("yes")) {
                //If the user ACCEPTS the file, he will be notified and the thread will send the file
                theOutput = ACCEPT;
                state = WAITING;
            } else if (theInput.equalsIgnoreCase("no")) {
                //If the user DOESN'T ACCEPT the file, he will be notified and the send/reception will be canceled
                theOutput = REJECT;
                state = WAITING;
            } else {
                //ALL of the unacceptable responses will be discarted and after notify the user, we ask the question again
                theOutput = "#You're supposed to say \"yes\" or \"no\"! "
                        + "Try again. Do you want to download the file? (yes/no)";
            }
        }
        
        //Returning the Question / Decision messages , depending on the STATE value
        return theOutput;
    }
}
