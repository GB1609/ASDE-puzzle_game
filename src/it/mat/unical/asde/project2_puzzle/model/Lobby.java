package it.mat.unical.asde.project2_puzzle.model;

public class Lobby {

	private String name;
	private String user1;
	private String user2;
	public Lobby(String name, String user1, String user2) {
		super();
		this.name = name;
		this.user1 = user1;
		this.user2 = user2;
	}
	public Lobby(String name, String user1) {
		super();
		this.name = name;
		this.user1 = user1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	
	@Override
	public String toString() {
		return "Lobby [name=" + name + ", user1=" + user1 + ", user2=" + user2 + "]";
	}
	
	
	@Override
	public boolean equals(Object obj) {
		//Two lobbies are equals if they have the same "name"
		//In this way a user can search lobbies by "name"
		return name.equals(((Lobby)obj).getName());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	
	
}
