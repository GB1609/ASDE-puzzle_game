package it.mat.unical.asde.project2_puzzle.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToMany(mappedBy = "matches", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<User> users = new HashSet<User>();


	@ManyToOne(optional = false)
	@JoinColumn(nullable = false)
	private User winner;

	@Column(nullable = false)
	private int time;

	public Match() {
		super();
	}

	public Match(User winner, int time) {
		super();
		setWinner(winner);
		this.time = time;
	}

	@Override
	public String toString() {
		return "Match [id=" + id + ", users=" + users + ", winner=" + winner + ", time=" + time + "]";
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Set<User> getUsers() {
		return users;
	}

	public User getWinner() {
		return winner;
	}

	public void addUser(User user) {
		if (!users.contains(user))
			this.users.add(user);
	}

	public void setWinner(User winner) {
		if (!users.contains(winner))
			users.add(winner);
		this.winner = winner;
	}

}
