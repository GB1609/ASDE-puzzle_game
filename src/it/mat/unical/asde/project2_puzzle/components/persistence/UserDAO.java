package it.mat.unical.asde.project2_puzzle.components.persistence;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class UserDAO {

	@Autowired
	private SessionFactory session;

	@Autowired
	private DBManager dbManager;

	@PostConstruct
	public void init() {
		dbManager.save(new User("Ciccio", "Francesco", "Pasticcio"));
		dbManager.save(new User("Giovanni", "Francesco", "Pasticcio"));
	}

	public boolean save(User user) {
		return dbManager.save(user);

	}

	public User getUser(String username) {
		Session openSession = session.openSession();
		Query<User> query = openSession.createQuery("from User as u where u.username=:u", User.class).setParameter("u",
				username);
		User user = query.uniqueResult();
		openSession.close();
		return user;

	}
}
