package it.mat.unical.asde.project2_puzzle.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

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

import org.springframework.lang.NonNull;

@Entity
@Table
public class GameMatch {

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column
	private String time;

	@Column(nullable = false)
	private LocalDateTime date;

	public GameMatch() {
		super();
		date = LocalDateTime.now();
		DateTimeFormatter.ofPattern("dd/mm/yyyy", Locale.ITALIAN).format(date);

	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		DateTimeFormatter.ofPattern("dd/mm/yyyy", Locale.ITALIAN).format(date);
		this.date = date;
	}

	@Override
	public String toString() {
		return date.getDayOfMonth() + "/" + date.getMonthValue();
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
		return super.equals(obj);
	}

}
