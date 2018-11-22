package it.mat.unical.asde.project2_puzzle.model;

public class Lobby {
	private String name;
	private String owner;
	private String guest;
	private static int TOTAL_LOBBY = 0;
	private int lobbyID = 0;

	public Lobby(String name) {
		super();
		this.name = name;
		this.lobbyID = TOTAL_LOBBY++;
	}

	public Lobby(String name, String owner) {
		super();
		this.name = name;
		this.owner = owner;
		this.lobbyID = TOTAL_LOBBY++;

	}

	public Lobby(String name, String owner, String guest) {
		super();
		this.name = name;
		this.owner = owner;
		this.guest = guest;
		this.lobbyID = TOTAL_LOBBY++;
	}

	public Integer getLobbyID() {
		return this.lobbyID;
	}

	@Override
	public boolean equals(Object obj) {
		// Two lobbies are equals if they have the same "name"
		// In this way a user can search lobbies by "name"
		return name.equals(((Lobby) obj).getName());
	}

	public String getGuest() {
		return guest;
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Lobby [name=" + name + ", owner=" + owner + ", guest=" + guest + "]";
	}
}
