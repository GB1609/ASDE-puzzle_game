package it.mat.unical.asde.project2_puzzle.components.persistence;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.GameMatch;
import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class MatchDAO {

	@Autowired
	private SessionFactory session;

	@Autowired
	private DBManager dbManager;

	public boolean save(GameMatch match) {

		boolean result = dbManager.save(match);
		if (result) {
			Set<User> users = match.getUsers();
			for (User user : users) {
				result = dbManager.update(user);
				if (!result)
					break;
			}
		}
		return result;

	}

	public ArrayList<GameMatch> getMatches(String username) {
		Session openSession = session.openSession();
		Query<GameMatch> query = openSession.createQuery("from Match m LEFT JOIN FETCH m.users ", GameMatch.class);
		ArrayList<GameMatch> results = new ArrayList<GameMatch>(query.getResultList());
		openSession.close();
		return results;
	}

}
