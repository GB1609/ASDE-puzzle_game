package it.mat.unical.asde.project2_puzzle.components.persistence;

import java.util.ArrayList;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.Match;
import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class MatchDAO {

	@Autowired
	private SessionFactory session;

	@Autowired
	private DBManager dbManager;

	public boolean save(Match match) {

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

	public ArrayList<Match> getMatches(String username) {
		Session openSession = session.openSession();
		Query<Match> query = openSession.createQuery("from Match m LEFT JOIN FETCH m.users ", Match.class);
		ArrayList<Match> results = new ArrayList<Match>(query.getResultList());
		openSession.close();
		return results;
	}

}
