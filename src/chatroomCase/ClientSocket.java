package chatroomCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSocket {
	public static void main(String[] args) throws IOException {

		if (args.length != 2) {
			System.err.println(
					"Usage: java ClientSocket <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try {
			Socket s = new Socket(hostName, portNumber);
			ClientSocket clientSock = new ClientSocket();

			//starting server data reader thread
			clientSock.new ServerDataReader(s).start();

			//starting client data sender thread
			clientSock.new ClinetDataSender(s).start();

		} 
		catch (UnknownHostException e) {
			System.err.println("No information about the host :" + hostName);
			System.exit(1);
		} 
		catch (IOException e) {
			System.err.println("I/O error... Not connected to " +hostName);
			System.exit(1);
		} 
	}

	class ServerDataReader extends Thread{
		BufferedReader bufRead = null;
		Socket clntSocket = null;
		public ServerDataReader(Socket clientSocket){
			this.clntSocket = clientSocket;
		}

		public void run(){
			try{
				bufRead = new BufferedReader(new InputStreamReader(clntSocket.getInputStream()));
				String serverResponse;
				while ((serverResponse = bufRead.readLine()) != null){
					System.out.println(serverResponse);
					if(serverResponse.equalsIgnoreCase("Client connection closed")){
						System.out.println("Client loop will be broken and it can be exited.");
						closeConnection();
						break;
					}	
				}
				System.out.println("Terminating client process.");
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			finally{
				closeConnection();
			}
		}

		private void closeConnection(){
			try{
				bufRead.close();
			}catch(Exception ex){
				// now i don't care
			}
		}
	}

	class ClinetDataSender extends Thread{
		Socket clientSocket= null;
		PrintWriter out = null;
		BufferedReader stdIn = null;

		public ClinetDataSender(Socket clientSocket){
			this.clientSocket = clientSocket;
		}

		public void run(){
			try{
				out = new PrintWriter(clientSocket.getOutputStream(), true);            
				stdIn = new BufferedReader(new InputStreamReader(System.in));

				String clientInput;
				while ((clientInput = stdIn.readLine()) != null) {
					out.println(clientInput);                
				}
			} 
			catch(Exception ex) {
				ex.printStackTrace();
			} 
			finally {
				closeConnection();
			}

		}

		private void closeConnection(){
			try{
				out.close();
				stdIn.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	}
}
