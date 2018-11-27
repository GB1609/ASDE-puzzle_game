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
public class CredentialsDAO{
    @Autowired
    private SessionFactory session;
    @Autowired
    private DBManager dbManager;

    @PostConstruct
    public void init(){
        dbManager.save(new Credentials("Ciccio", "ciccio"));
        dbManager.save(new Credentials("Giovanni", "giovanni"));
        dbManager.save(new Credentials("a", "a"));
        dbManager.save(new Credentials("b", "b"));
    }

    public boolean save(Credentials credentials){
        return dbManager.save(credentials);
    }

    public boolean exists(Credentials credentials){
        Session openSession = session.openSession();
        Query<Credentials> query = openSession.createQuery("from Credentials as c where c.username=:u and c.password=:p", Credentials.class)
                .setParameter("u", credentials.getUsername()).setParameter("p", credentials.getPassword());
        boolean results = query.uniqueResult() != null;
        openSession.close();
        return results;
    }

    public Credentials getCredentials(String username){
        Session openSession = session.openSession();
        Query<Credentials> query = openSession.createQuery("from Credentials as c where c.username=:u", Credentials.class).setParameter("u",
                username);
        Credentials credentials = query.uniqueResult();
        openSession.close();
        return credentials;
    }

    public boolean updateUserPassword(String password,String username){
        if (password == "")
            return true;
        Session openSession = session.openSession();
        Transaction tx = null;
        tx = openSession.beginTransaction();
        int query = openSession.createQuery("update Credentials as c set c.password = :p where c.username = :u").setParameter("u", username)
                .setParameter("p", password).executeUpdate();
        tx.commit();
        openSession.close();
        return query > 0;
    }
}
