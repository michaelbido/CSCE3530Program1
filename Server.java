/* 
Compilation: javac Server.java
Execution  : java Server

Name       : Michael Bido-Chavez (euid: mb0501)
Due Date   : Sept. 28, 2017
Class      : CSCE 3530

Sockets Program - Server
----------------
Description:
A Server will wait client connection, and then accept a string to 
count the number of vowels within that string. The Server will then
send back the vowel count to the client, and repeat the process 
until the client types 'bye' to disconnect. Even if the client
disconnects, the server will still run.
----------------

*/

import java.io.*;
import java.net.*;
import java.util.*;

// ServerThread method, creates a thread for each connected client to 
// handle vowel count processing. After accepting the string, the 
// server will check if it equals 'bye' before counting the vowels.
// If the string is 'bye', send goodbye message, otherwise send
// the vowel count. repeat until connection ends.

class ServerThread extends Thread
{
    // variables
    int length;
    int threadID;
    boolean endClientConnection;
	DataInputStream in;
	DataOutputStream out;
	BufferedReader br;	
    // thread
	private Thread t;
	// private String threadName;
    // constructor, receives input/output data stream from socket
    public ServerThread(DataInputStream dataIn, DataOutputStream dataOut, int threadNum) {
        try {
            in = dataIn;
            out = dataOut;
            endClientConnection = false;
			threadID = threadNum;
			br = new BufferedReader(new InputStreamReader(System.in));
        }   
        catch(Exception e) {
            System.out.println("There was a problem with creating a thread");
        }     
    }
    // run the thread
    public void run() {
        System.out.println("Running thread " + threadID);
        try {
            while(!endClientConnection) {

				// Reading the message from the client
				length = in.readInt();
				byte[] rmessage = new byte[256];
				in.readFully(rmessage, 0, length);
				String recvMessage = new String(rmessage);

				System.out.println("Message received from a client: " + recvMessage);				
				// System.out.println(recvMessage.indexOf('\0') + " <- pos of null character");
				recvMessage = recvMessage.substring(0, recvMessage.indexOf('\0'));

				// Block of 'bug' checking output
				// System.out.println(recvMessage + " <- string, result -> " + recvMessage.equals("bye"));
				// System.out.println(Integer.valueOf(recvMessage) + " <- int of string, real val -> " + Integer.valueOf("bye"));
				
				String sendMessage;			

				// if the client wishes to end connection, send goodbye message and end it for good				
                if (recvMessage.equals("bye")) {
                    // end loop
                    endClientConnection = true;
                    // send outgoing message
                    sendMessage = "Goodbye, I'll miss you!";
                    System.out.println("Client said bye, saying bye back. Closing client connection...");
                }
                else {
                    // Reteive the vowel count
					sendMessage = countVowels(recvMessage);
					System.out.print("Based on input " + recvMessage + ", " + sendMessage + " vowel(s) were counted. Sending to client...\n");					
				}
				// send message back
				byte[] smessage = new byte[256];
				smessage = sendMessage.getBytes();
				out.writeInt(smessage.length);
				out.write(smessage);
				out.flush();			
            }
        }
        catch(Exception e) {
            System.out.println("There was a problem with running a thread: " + e);            
        }
        System.out.println("ending thread number " + threadID);
    }
	// start thread
    public void start () {
        if (t == null) {
            t = new Thread (new ServerThread(in, out, threadID));
            t.start ();
        }
    }
	// count the number of vowels by converting string
	// to char array, and switch-case of each vowel
    public static String countVowels(String s)
	{
		char[] charString = s.toCharArray();
		Integer vowelCount = 0;
		for (char letter : charString) {
			switch(letter) {
				case 'a':
					vowelCount++;
					break;
				case 'e':
					vowelCount++;
					break;
				case 'i':
					vowelCount++;
					break;
				case 'o':
					vowelCount++;
					break;
				case 'u':
					vowelCount++;
					break;
				case 'A':
					vowelCount++;
					break;
				case 'E':
					vowelCount++;
					break;
				case 'I':
					vowelCount++;
					break;
				case 'O':
					vowelCount++;
					break;
				case 'U':
					vowelCount++;
					break;
				default:
					;
			}
		}
		return vowelCount.toString();
	}
}

// Server handler, binds socket and accepts connections
// before handing processes to each thread.

public class Server
{
	private static Socket socket;
 
	public static void main(String[] args)
	{
		int threadID = 0;

		try {
			//Connecting with the client
			int port = 13000;
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server Started and listening to the port 13000");
		
			//Server is running always. This is done using this while(true) loop
			while(true) {
				//Connecting to the client
				socket = serverSocket.accept();

				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				// start thread
				threadID++;
				ServerThread s = new ServerThread(in, out, threadID);
				s.start();
				// Closing the connection
				// socket.close();
			}
		}
		catch (Exception e) {
			System.out.println("Server socket error!");
		}
	}
}

