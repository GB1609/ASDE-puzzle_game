package it.mat.unical.asde.project2_puzzle.components.persistence;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.GameMatch;
import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class UserDAO {

	@Autowired
	private SessionFactory session;

	@Autowired
	private DBManager dbManager;

	@PostConstruct
	public void init() {

	}

	public boolean save(User user) {
		if (!user.getMatches().isEmpty()) {
			throw new PersistenceException("YOU CANNOT CREATE A NEW USER WITH MATCHES INSIDE");
		}
		return dbManager.save(user);

	}

	public User getFullUser(String username) {
		Session openSession = session.openSession();
		Query<User> query = openSession
				.createQuery("from User as usr LEFT JOIN FETCH usr.matches where usr.username=:u", User.class)
				.setParameter("u", username);
		User user = query.uniqueResult();

		for (GameMatch match : user.getMatches()) {
			for (User user2 : match.getUsers()) {
				user2.getUsername();
			}
		}

		openSession.close();
		return user;

	}

	public User getUser(String username) {
		Session openSession = session.openSession();
		Query<User> query = openSession.createQuery("from User as usr where usr.username=:u", User.class)
				.setParameter("u", username);
		User user = query.uniqueResult();
		openSession.close();
		return user;

	}

	public boolean updateUserInformation(String firstname, String lastname, String username, String avatar) {

		if (firstname == "" && lastname == "" && avatar == "")
			return true;

		User user = getUser(username);

		if (firstname != "")
			user.setFirstName(firstname);
		if (lastname != "")
			user.setLastName(lastname);
		if (avatar != "")
			user.setAvatar(avatar);

		return dbManager.update(user);
	}

	public User getUserMatches(String username) {
		Session openSession = session.openSession();
		Query<User> query = openSession.createQuery("from User_Match as um where um.username=:u", User.class)
				.setParameter("u", username);

		User user = query.uniqueResult();
		openSession.close();

		return user;
	}
}
