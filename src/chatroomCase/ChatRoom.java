package chatroomCase;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatRoom {
	//declaration of variables

	private String name;
	private String id;
	private Set<ChatUser> users;
	private Set<Socket> clntSockets;

	public ChatRoom() {
		id = String.valueOf(Math.random());
		users = new HashSet<ChatUser>();
		clntSockets = new HashSet<Socket>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<ChatUser> getUsers() {
		return users;
	}

	public void setUsers(Set<ChatUser> users) {
		this.users = users;
	}

	public void addUser(ChatUser user) {
		users.add(user);
	}


	public Set<Socket> getClientSockets() {
		return clntSockets;
	}
	public void setClientSockets(Set<Socket> clientSockets) {
		this.clntSockets = clientSockets;
	}
	public void addClientSocket(Socket clientSocket){
		clntSockets.add(clientSocket);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		ChatRoom other = (ChatRoom) o;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatRoom [name=" + name + ", id=" + id + ", users=" + users
				+ ", clientSockets=" + clntSockets + "]";
	}
}
