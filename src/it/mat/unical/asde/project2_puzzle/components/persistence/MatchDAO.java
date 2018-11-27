package it.mat.unical.asde.project2_puzzle.components.persistence;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

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

	@PostConstruct
	public void init() {
		User ciccio = new User("Ciccio", "Francesco", "Pasticcio", "avatar.svg");
		User giovanni = new User("Giovanni", "giovanni", "giovanni", "avatar_1.png");

		Match match = new Match(ciccio, 132);
		Match match1 = new Match(giovanni, 132);
		
		
		match.addUser(giovanni);
		match.addUser(ciccio);
		
		
		
		ciccio.addMatch(match);
		giovanni.addMatch(match);
		
		dbManager.save(ciccio);
		
		System.out.println("ijijijij");
		dbManager.save(giovanni);

		
		dbManager.save(match1);
		
	//	System.out.println(dbManager.save(match));

//		ciccio.addMatch(match);

//		giovanni.addMatch(match1);
//		match1.addUser(ciccio);

//		for (User user : match.getUsers()) {
//			System.out.println(user.getUsername());
//		}

		// ciccio.addMatch(match1);

		// giovanni.addMatch(match);


		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("Utenti nel match");
		ArrayList<Match> allMatch = getMatches(ciccio.getUsername());
		System.out.println(allMatch.size());
		for (Match match2 : allMatch) {
			System.out.println(match2);
//			for (User user : match2.getUsers()) {
//				System.out.println(user.getUsername());
//			}
		}
		
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");
		System.out.println("**************");

//		dbManager.save(match1);
//		
//		
//		ArrayList<Match> allMatch = getAllMatch();
//		
//		for (Match match2 : allMatch) {
//			System.out.println(match2.getTime());
//		}

	}

	public boolean save(Match match) {
		return dbManager.save(match);
		
	}

	public ArrayList<Match> getMatches(String username) {
		Session openSession = session.openSession();
		Query<Match> query = openSession.createQuery("from Match m JOIN FETCH m.users ", Match.class);
		ArrayList<Match> results = new ArrayList<Match>(query.getResultList());
		openSession.close();
		return results;
	}

//	//testtttttttttttttttttttttttttttttt
//	public ArrayList<Match> getAllMatch() {
//		Session openSession = session.openSession();
//		Query<Match> query = openSession.createQuery("from User_Match", Match.class);
//		ArrayList<Match> results = new ArrayList<Match>(query.getResultList());
//		openSession.close();
//		return results;
//	}

}
