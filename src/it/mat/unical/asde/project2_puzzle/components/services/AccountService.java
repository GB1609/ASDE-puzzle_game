package it.mat.unical.asde.project2_puzzle.components.services;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import it.mat.unical.asde.project2_puzzle.components.persistence.CredentialsDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.MatchDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.UserDAO;
import it.mat.unical.asde.project2_puzzle.model.Credentials;
import it.mat.unical.asde.project2_puzzle.model.Match;
import it.mat.unical.asde.project2_puzzle.model.User;

@Service
public class AccountService {

	private final String AvatarsFolder = "resources/images/avatars/";

	@Autowired
	private MatchDAO matchDAO;

	@Autowired
	private CredentialsDAO credentialsDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ServletContext servletContext;

	public boolean loginAccepted(String username, String password) {
		return this.credentialsDAO.exists(new Credentials(username, password));
	}

	public boolean accountCreated(String firstName, String lastName, String username, String password, String avatar) {
		boolean value = this.credentialsDAO.save(new Credentials(username, password));
		if (value) {
			value = this.userDAO.save(new User(username, firstName, lastName, avatar));
		}
		return value;
	}

	public ArrayList<Match> getMatches(String username) {
		return this.matchDAO.getMatches(username);
	}

	public void addMatch(Match match) {
		this.matchDAO.save(match);
	}

	public User getUser(String username) {
		return this.userDAO.getUser(username);

	}

	public Credentials getCredentials(String username) {
		return this.credentialsDAO.getCredentials(username);
	}

	public boolean updateUserInformation(String firstname, String lastname, String password, String username) {
		boolean status = this.userDAO.updateUserInformation(firstname, lastname, username);
		if (status) {
			status = this.credentialsDAO.updateUserPassword(password, username);
		}
		return status;
	}

	public void fillUserInformation(String username, Model model) {
		User user = this.getUser(username);
		Credentials credentials = this.getCredentials(username);
		model.addAttribute("firstname", user.getFirstName());
		model.addAttribute("lastname", user.getLastName());
		model.addAttribute("password", credentials.getPassword());
		model.addAttribute("avatar", user.getAvatar());
		model.addAttribute("matches", this.getMatches(username));

	}

	public ArrayList<String> getAvatarsList() {
		return this.loadAvatarsList();
	}

	private ArrayList<String> loadAvatarsList() {
		ArrayList<String> avatars = new ArrayList<>();
		File dir = new File(this.servletContext.getRealPath("/WEB-INF/" + this.AvatarsFolder));
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				avatars.add(file.getName());
			}
		}
		return avatars;
	}
}
