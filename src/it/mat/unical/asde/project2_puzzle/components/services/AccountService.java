package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import it.mat.unical.asde.project2_puzzle.components.persistence.MatchDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.UserDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.CredentialsDAO;
import it.mat.unical.asde.project2_puzzle.model.Credentials;
import it.mat.unical.asde.project2_puzzle.model.Match;
import it.mat.unical.asde.project2_puzzle.model.User;

@Service
public class AccountService {

	@Autowired
	private MatchDAO matchDAO;

	@Autowired
	private CredentialsDAO credentialsDAO;

	@Autowired
	private UserDAO userDAO;

	public boolean loginAccepted(String username, String password) {
		return credentialsDAO.exists(new Credentials(username, password));
	}

	public boolean accountCreated(String firstName, String lastName, String username, String password) {
		boolean value = credentialsDAO.save(new Credentials(username, password));
		if (value)
			value = userDAO.save(new User(username, firstName, lastName));
		return value;
	}

	public ArrayList<Match> getMatches(String username) {
		return matchDAO.getMatches(username);
	}

	public void addMatch(Match match) {
		matchDAO.save(match);
	}

	public User getUser(String username) {
		return userDAO.getUser(username);

	}

	public Credentials getCredentials(String username) {
		return credentialsDAO.getCredentials(username);
	}

	public boolean updateUserInformation(String firstname, String lastname, String password, String username) {
		boolean status = userDAO.updateUserInformation(firstname, lastname, username);
		if (status)
			status = credentialsDAO.updateUserPassword(password, username);
		return status;
	}

	public void fillUserInformation(String username, Model model) {
		User user = getUser(username);
		Credentials credentials = getCredentials(username);
		model.addAttribute("firstname", user.getFirstName());
		model.addAttribute("lastname", user.getLastName());
		model.addAttribute("password", credentials.getPassword());
		model.addAttribute("matches", getMatches(username));

	}
}
