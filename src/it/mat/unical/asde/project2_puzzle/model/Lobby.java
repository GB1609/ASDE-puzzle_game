package it.mat.unical.asde.project2_puzzle.model;

public class Lobby {

	private String name;
	private String owner;
	private String guest;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Lobby(String name) {
		super();
		this.name = name;
	}

	public Lobby(String name, String owner) {
		super();
		this.name = name;
		this.owner = owner;
	}

	public Lobby(String name, String owner, String guest) {
		super();
		this.name = name;
		this.owner = owner;
		this.guest = guest;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getGuest() {
		return this.guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	@Override
	public String toString() {
		return "Lobby [name=" + this.name + ", owner=" + this.owner + ", guest=" + this.guest + "]";
	}

	@Override
	public boolean equals(Object obj) {
		// Two lobbies are equals if they have the same "name"
		// In this way a user can search lobbies by "name"
		return this.name.equals(((Lobby) obj).getName());
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

}
