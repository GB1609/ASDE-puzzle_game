package it.mat.unical.asde.project2_puzzle.components.persistence;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import it.mat.unical.asde.project2_puzzle.model.Credentials;

@Repository
public class CredentialsDAO {
	
	@Autowired
	private SessionFactory session;
	
	@PostConstruct
	public void init()
	{
		save(new Credentials("Ciccio","ciccio"));
		save(new Credentials("Giovanni","giovanni"));
	}
	
	public boolean save(Credentials credentials) {
		boolean results = false;
		Session session = this.session.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(credentials);
			tx.commit();
			results = true;
		} catch (Exception e) {
			tx.rollback();
			results = false;
		}
		session.close();
		return results;
	}
	
	public boolean exists(Credentials credentials) {
		Session openSession = session.openSession();
		Query<Credentials> query = openSession
				.createQuery("from Credentials as c where c.username=:u and c.password=:p", Credentials.class)
				.setParameter("u", credentials.getUsername()).setParameter("p", credentials.getPassword());		
		boolean results = query.uniqueResult() != null;
		openSession.close();
		return results;

	}

}
