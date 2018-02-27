// ServerThread.java

import java.io.*;
import java.util.*;
import java.net.*;

public class ServerThread extends Thread
{
    // variables
    int length;
    int threadID;
    boolean endClientConnection;
	DataInputStream in;
    DataOutputStream out;
    // thread
    private Thread t;
    // constructor, receives input/output data stream from socket
    public ServerThread(DataInputStream dataIn, DataOutputStream dataOut, int threadNum) {
        try {
            in = dataIn;
            out = dataOut;
            endClientConnection = false;
            threadID = threadNum;
        }   
        catch(Exception e) {
            System.out.println("There was a problem with creating a thread");
        }     
    }
    // run the thread
    public void run() {
        System.out.println("Running thread " + threadID);
        try {
            // Reading the message from the client
            length = in.readInt();
            byte[] rmessage = new byte[256];
            in.readFully(rmessage, 0, length);
            String recvMessage = new String(rmessage);
            System.out.println("Message received from a client: " + recvMessage);

            while(!endClientConnection) {
                // if the client wishes to end connection, send goodbye message and end it for good
                if (recvMessage.equals("Bye") || recvMessage.equals("bye")) {
                    // end loop
                    endClientConnection = true;
                    // send outgoing message
                    String goodbyeMessage = "Goodbye, I'll miss you!";
                    byte[] gmessage = new byte[256];
                    gmessage = goodbyeMessage.getBytes();
                    out.writeInt(gmessage.length);
                    out.write(gmessage);
                    out.flush();	
                    // System.out.print("bug0");
                }
                else {
                    // System.out.print("bug1");											
                    // Sending the response back to the client
                    String sendMessage = countVowels(recvMessage);
                    byte[] smessage = new byte[256];
                    smessage = sendMessage.getBytes();
                    out.writeInt(smessage.length);
                    out.write(smessage);
                    out.flush();			
                    System.out.print("Based on input " + recvMessage + ", " + sendMessage + " vowel(s) were counted. Sending to client...\n");
                }
                // Reading the message from the client, again
                length = in.readInt();
                rmessage = new byte[256];
                in.readFully(rmessage, 0, length);
                recvMessage = new String(rmessage);
                System.out.println("Message received from a client: " + recvMessage);
            }
        }
        catch(Exception e) {
            System.out.println("There was a problem with running a thread");            
        }
        System.out.println(threadID + " ending");
    }

    public void start () {
        if (t = null) {
            t = new Thread (this, threadName);
            t.start ();
        }
    }

    public static String countVowels(String s)
	{
		return "wow\n";
	}
}