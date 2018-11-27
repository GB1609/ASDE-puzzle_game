package it.mat.unical.asde.project2_puzzle.components.persistence;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.Match;
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
		return dbManager.save(user);

	}

	public User getUser(String username) {
		Session openSession = session.openSession();
		Query<User> query = openSession
				.createQuery("from User as usr JOIN FETCH usr.matches where usr.username=:u", User.class)
				.setParameter("u", username);
		User user = query.uniqueResult();

		System.out.println(user == null);
		for (Match match : user.getMatches()) {
			for (User user2 : match.getUsers()) {
				System.out.println(user2.getUsername());
			}
		}
		openSession.close();
		return user;

	}

	public boolean updateUserInformation(String firstname, String lastname, String username) {

		if (firstname == "" && lastname == "")
			return true;

		Session openSession = session.openSession();
		Transaction tx = null;
		tx = openSession.beginTransaction();
		int query = 0;
		if (firstname != "" && lastname != "")
			query += openSession.createQuery(
					"update User as usr set usr.firstName = :firstN , usr.lastName = :lastN where usr.username = :u")
					.setParameter("u", username).setParameter("firstN", firstname).setParameter("lastN", lastname)
					.executeUpdate();
		else if (firstname != "")
			query += openSession.createQuery("update User as usr set usr.firstName = :firstN where usr.username = :u")
					.setParameter("u", username).setParameter("firstN", firstname).executeUpdate();
		else if (lastname != "")
			query += openSession.createQuery("update User as usr set usr.lastName = :lastN where usr.username = :u")
					.setParameter("u", username).setParameter("lastN", lastname).executeUpdate();
		tx.commit();
		openSession.close();
		return query > 0;

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
