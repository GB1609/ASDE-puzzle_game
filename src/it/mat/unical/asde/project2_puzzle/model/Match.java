package it.mat.unical.asde.project2_puzzle.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Match {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false)
	private String user1;

	@Column(nullable = false)
	private String user2;

	@Column(nullable = false)
	private int winner;

	public Match() {
		super();
	}

	public Match(String user1, String user2, int winner) {
		super();
		this.user1 = user1;
		this.user2 = user2;
		this.winner = winner;
	}

	public long getId() {
		return id;
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

	public String getWinner() {
		return (winner == 0) ? user1 : user2;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	public void setWinner(String user) {
		if (user.equals(user1))
			this.winner = 0;
		if (user.equals(user2))
			this.winner = 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Match other = (Match) obj;

		if (user1 == null) {
			if (other.user1 != null)
				return false;
		} else if (!user1.equals(other.user1))
			return false;
		if (user2 == null) {
			if (other.user2 != null)
				return false;
		} else if (!user2.equals(other.user2))
			return false;

		if (id != other.id)
			return false;

		return true;

	}

}
