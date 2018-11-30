package it.mat.unical.asde.project2_puzzle.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class GameMatch {
	Map<User, String> times = new HashMap<>();
	Map<User, Integer> progresses = new HashMap<>();
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@ManyToMany(mappedBy = "matches")
	private Set<User> users = new HashSet<User>();
	@ManyToOne
	@JoinColumn(name = "winner", foreignKey = @ForeignKey(name = "USER_FK"))
	private User winner;
	@Transient
	private String lobbyName;

	public GameMatch() {
		super();
	}


	@Override
	public String toString() {
		
		return ""; //TODO
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public long getId() {
		return id;
	}


	public void setTime(User user, String time) {
		times.put(user, time);
	}

	public Set<User> getUsers() {
		return users;
	}

	public User getWinner() {
		return winner;
	}

	public void addUser(User user) {
		if (!users.contains(user)) {
			users.add(user);
			user.addMatch(this);
		}
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public void removeWinner() {
		this.winner = null;
	}

	public void removeUser(User user) {
		users.remove(user);
	}

	public void setLobbyName(String lobby_name) {
		this.lobbyName = lobby_name;
	}

	public String getLobbyName() {
		return this.lobbyName;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}


	public void setProgress(User user, Integer progress) {
		progresses.put(user, progress);
	}

}
