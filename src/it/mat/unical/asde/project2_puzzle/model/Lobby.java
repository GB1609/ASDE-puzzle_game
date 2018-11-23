package it.mat.unical.asde.project2_puzzle.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Lobby {

	private static final AtomicInteger count = new AtomicInteger(0);
	private final int id;
	private String name;
	private String owner;
	private String guest;

	public Lobby(String name) {
		super();
		this.name = name;
		this.id = count.incrementAndGet();
	}

	public Lobby(String name, String owner) {
		super();
		this.name = name;
		this.owner = owner;
		this.id = count.incrementAndGet();
	}

	public Lobby(String name, String owner, String guest) {
		super();
		this.name = name;
		this.owner = owner;
		this.guest = guest;
		this.id = count.incrementAndGet();
	}

	@Override
	public boolean equals(Object obj) {
		// Two lobbies are equals if they have the same "name"
		// In this way a user can search lobbies by "name"
		return this.name.equals(((Lobby) obj).getName());
	}

	public int getId() {
		return this.id;
	}

	public String getGuest() {
		return this.guest;
	}

	public String getName() {
		return this.name;
	}

	public String getOwner() {
		return this.owner;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
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
		return "Lobby [id=" + this.id + ", name=" + this.name + ", owner=" + this.owner + ", guest=" + this.guest + "]";
	}
}
