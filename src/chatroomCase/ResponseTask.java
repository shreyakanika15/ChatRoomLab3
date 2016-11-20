package chatroomCase;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ResponseTask implements Task{

	public void perform(Socket clientSocket){
		PrintWriter pw = null;
		BufferedReader bufRead = null;
		try{ 
			pw = new PrintWriter(clientSocket.getOutputStream(), true);                   
			pw.println("server> To join existing room or create a new room type in JOIN_CHATROOM:<name of chat room>");
			pw.println("server> To leave room type in LEAVE_CHATROOM:<name of chat room>");

			bufRead = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		}
		catch (IOException ex){
			System.out.println("Error trying to receive/send data from/to client.");
		} 
	}

	private void checkAndClose(BufferedReader bufRead, PrintWriter pw, Socket clientSocket) throws IOException{
		String leavingRoomId = null;
		String leavingUserId = null;
		String leavingClientName = null;

		String inputLine;
		while ((inputLine = bufRead.readLine()) != null) {
			if ((inputLine == null) || inputLine.startsWith("LEAVE_CHATROOM")) {
				leavingRoomId = inputLine.substring(inputLine.indexOf(':')+1, inputLine.length());
			} else if ((inputLine == null) || inputLine.startsWith("JOIN_ID")) {
				leavingUserId = inputLine.substring(inputLine.indexOf(':')+1, inputLine.length());
			} else if ((inputLine == null) || inputLine.startsWith("CLIENT_NAME")) {
				leavingClientName = inputLine.substring(inputLine.indexOf(':')+1, inputLine.length());					
			}
			if(leavingRoomId != null && leavingUserId != null && leavingClientName != null){
				ThreadPoolManager.getInstance().releaseTask();

				// close client socket, the return text is used to terminate the client
				pw.println("server> Client connection closed");
				pw.close();
				bufRead.close();

				return;
			}
		}
	}
}
