package it.mat.unical.asde.project2_puzzle.components.persistence;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import it.mat.unical.asde.project2_puzzle.model.Match;

@Repository
public class MatchDAO{
    @Autowired
    private SessionFactory session;

    public ArrayList<Match> getMatches(String username){
        Session openSession = session.openSession();
        Query<Match> query = openSession.createQuery("from Match as m where m.user1=:u or m.user2=:u", Match.class).setParameter("u", username);
        ArrayList<Match> results = new ArrayList<Match>(query.getResultList());
        openSession.close();
        return results;
    }

    @PostConstruct
    public void init(){
        save(new Match("Ciccio", "Giovanni", 0));
    }

    public void save(Match match){
        Session session = this.session.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(match);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }
        session.close();
    }
}
