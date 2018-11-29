package it.mat.unical.asde.project2_puzzle.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class User {

	@Id
	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false)
	private String avatar;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
//	@JoinTable(name = "User_Match", joinColumns = { @JoinColumn(name = "username") }, inverseJoinColumns = {
//			@JoinColumn(name = "id") })
	private Set<Match> matches = new HashSet<Match>();

	public User() {
		super();

	}

	public User(String username, String firstName, String lastName, String avatar) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
	}

	public Set<Match> getMatches() {
		return matches;
	}

	public void addMatch(Match match) {
		if (!matches.contains(match)) {
			matches.add(match);
			match.addUser(this);
		}
	}

	public void removeMatch(Match match) {
		matches.remove(match);
		match.removeUser(this);
	}

	public void setMatches(Set<Match> matches) {
		this.matches = matches;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName)
				&& Objects.equals(lastName, user.lastName) && Objects.equals(avatar, user.avatar);
	}

}
