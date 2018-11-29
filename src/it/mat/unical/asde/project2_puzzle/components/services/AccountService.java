package it.mat.unical.asde.project2_puzzle.components.services;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import it.mat.unical.asde.project2_puzzle.components.persistence.MatchDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.UserDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.CredentialsDAO;
import it.mat.unical.asde.project2_puzzle.model.Credentials;
import it.mat.unical.asde.project2_puzzle.model.GameMatch;
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
		return credentialsDAO.exists(new Credentials(username, password));
	}

	public boolean accountCreated(String firstName, String lastName, String username, String password, String avatar) {
		boolean value = credentialsDAO.save(new Credentials(username, password));
		if (value)
			value = userDAO.save(new User(username, firstName, lastName, avatar));
		return value;
	}

	public ArrayList<GameMatch> getMatches(String username) {
		return matchDAO.getMatches(username);
	}

	public void addMatch(GameMatch match) {
		matchDAO.save(match);
	}

	public User getUser(String username) {
		return userDAO.getUser(username);

	}

	public String getAvatarUser(String username) {
		return AvatarsFolder + userDAO.getUser(username).getAvatar();
	}

	public User getFullUser(String username) {
		return userDAO.getFullUser(username);

	}

	public Credentials getCredentials(String username) {
		return credentialsDAO.getCredentials(username);
	}

	public boolean updateUserInformation(String firstname, String lastname, String password, String username,
			String avatar) {
		boolean status = userDAO.updateUserInformation(firstname, lastname, username, avatar);
		if (status)
			status = credentialsDAO.updateUserPassword(password, username);
		return status;
	}

	public void fillUserInformation(String username, Model model) {
		User user = getFullUser(username);
		Credentials credentials = getCredentials(username);
		model.addAttribute("user", user);
		model.addAttribute("password", credentials.getPassword());
		model.addAttribute("avatars", loadAvatarsList());

	}

	public ArrayList<String> getAvatarsList() {

		return loadAvatarsList();
	}

	private ArrayList<String> loadAvatarsList() {
		ArrayList<String> avatars = new ArrayList<String>();
		File dir = new File(servletContext.getRealPath("/WEB-INF/" + AvatarsFolder));
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				avatars.add(file.getName());

			}
		}
		return avatars;
	}
}
