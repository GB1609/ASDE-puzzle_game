package it.mat.unical.asde.project2_puzzle.model;

import java.util.HashSet;
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

@Entity
@Table
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToMany(mappedBy = "matches")
	private Set<User> users = new HashSet<User>();

	@ManyToOne
	@JoinColumn(name = "winner", foreignKey = @ForeignKey(name = "USER_FK"))
	private User winner;

	@Column(nullable = false)
	private int time;

	public Match() {
		super();
	}

	public Match(int time) {
		super();
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
		if(!users.contains(user))
		{
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Match match = (Match) o;
		return Objects.equals(time, match.time);
	}

}
