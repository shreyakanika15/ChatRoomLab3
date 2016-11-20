package chatroomCase;



import java.net.Socket;

public interface Task {
	void perform(Socket clientSocket);
}
