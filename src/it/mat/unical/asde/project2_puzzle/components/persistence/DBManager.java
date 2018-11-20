package it.mat.unical.asde.project2_puzzle.components.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DBManager {

	@Autowired
	private SessionFactory session;

	public boolean save(Object object) {
		boolean results = false;
		Session openSession = this.session.openSession();
		Transaction tx = null;
		try {
			tx = openSession.beginTransaction();
			openSession.save(object);
			tx.commit();
			results = true;
		} catch (Exception e) {
			tx.rollback();
			results = false;
		}
		openSession.close();
		return results;
	}
}
