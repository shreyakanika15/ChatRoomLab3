package chatroomCase;

import java.net.Socket;

public class ThreadPool extends Thread{
	public static final String STATUS_BUSY = "BUSY";
	public static final String STATUS_AVAILABLE = "AVAILABLE";

	private volatile Task task = null;
	private volatile Socket clntSocket = null;
	private volatile String status = null;

	public ThreadPool(){
		status = new String();
	}

	public void setClientSocket(Socket clientSocket){
		this.clntSocket = clientSocket;
	}
	public Socket getClientSocket(){
		return clntSocket;
	}

	public void setTask(Task task){
		this.task = task;
	}

	public String getRunnbingStatus() {
		return status;
	}

	public void setRunningStatus(String status) {
		this.status = status;
	}

	public void run() {
		while(true){
			if(status.equals(STATUS_BUSY)){
				System.out.println("For thread "+getName()+" the state has changed to "+STATUS_BUSY);
				task.perform(clntSocket);
				System.out.println("Seems like client has lost connection or explicitly closed connection. Now the thread "+getName()+" with state "+getRunnbingStatus()+" will be available for other client.");
			}
		}
	}
}
