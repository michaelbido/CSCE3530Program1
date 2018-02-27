/* 
Compilation: javac Client.java
Execution  : java Client

Name       : Michael Bido-Chavez (euid: mb0501)
Due Date   : Sept. 28, 2017
Class      : CSCE 3530

Sockets Program - Client
----------------
Description:
A client will connect to a server, and then send a string to
the server so that it may count the number of vowels within that 
string. The client will then receive that message, and repeat the
process until the client types 'bye' to disconnect.
----------------
*/

import java.io.*;
import java.net.*;

public class Client
{
	private static Socket socket;
 
	public static void main(String args[])
	{
		try
		{
			//Connecting with the server
			// String host = "localhost"; //Both in the same machine [IP address 127.0.0.1]
			String host = "129.120.151.94"; //IP address of server
			int port = 13000;
			InetAddress address = InetAddress.getByName(host);
			socket = new Socket(address, port);

			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int length;

			boolean endServerConnection = false;
			
			//Sending the message to the server
			while (!endServerConnection) {
				// read input
				System.out.print("To exit, type 'bye'. Enter a message to count the vowels: ");
				String sendMessage = br.readLine();
	
				// convert and send input to server
				byte[] smessage = new byte[256];
				smessage = sendMessage.getBytes();
				out.writeInt(smessage.length);
				out.write(smessage);
				out.flush();			
				System.out.println("Message sent to the server: " + sendMessage);
				
				// end the loop if the user prints bye
				if (sendMessage.equals("bye")) {
					endServerConnection = true;
				}

				//Receiving the message from the server
				length = in.readInt();
				byte[] rmessage = new byte[256];
				in.readFully(rmessage, 0, length);
				String recvMessage = new String(rmessage);
				System.out.println("Number of vowels in " + sendMessage + " according to server: " + recvMessage);
			}

			//Closing the connection
			socket.close();

			System.out.println("Closing connection. Closing Program.");
		}
		catch (Exception e)
		{
				System.out.println("Client socket error:" + e);
		}
	}
}
