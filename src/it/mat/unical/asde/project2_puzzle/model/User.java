package it.mat.unical.asde.project2_puzzle.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {

	@Id
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false)
	private String firstName;
	@Column(nullable = false)
	private String lastName;
	@Column(nullable = false)
	private String avatar;

	public User() {
		super();
	}

	public User(String username, String firstName, String lastName, String avatar) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAvatar() {
		return this.avatar;
	}

	@Override
	public String toString() {
		return "User [username=" + this.username + ", firstName=" + this.firstName + ", lastName=" + this.lastName
				+ ", avatar=" + this.avatar + "]";
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
