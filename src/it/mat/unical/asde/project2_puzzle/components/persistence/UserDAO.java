package it.mat.unical.asde.project2_puzzle.components.persistence;

import javax.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class UserDAO{
    @Autowired
    private SessionFactory session;
    @Autowired
    private DBManager dbManager;

    @PostConstruct
    public void init(){
        dbManager.save(new User("Ciccio", "Francesco", "Pasticcio", "avatar.svg"));
        dbManager.save(new User("Giovanni", "Francesco", "Pasticcio", "avatar_1.png"));
        dbManager.save(new User("a", "a", "a", "avatar_1.png"));
        dbManager.save(new User("b", "b", "b", "avatar_1.png"));
    }

    public boolean save(User user){
        return dbManager.save(user);
    }

    public User getUser(String username){
        Session openSession = session.openSession();
        Query<User> query = openSession.createQuery("from User as usr where usr.username=:u", User.class).setParameter("u", username);
        User user = query.uniqueResult();
        openSession.close();
        return user;
    }

    public boolean updateUserInformation(String firstname,String lastname,String username){
        if (firstname == "" && lastname == "")
            return true;
        Session openSession = session.openSession();
        Transaction tx = null;
        tx = openSession.beginTransaction();
        int query = 0;
        if (firstname != "" && lastname != "")
            query += openSession.createQuery("update User as usr set usr.firstName = :firstN , usr.lastName = :lastN where usr.username = :u")
                    .setParameter("u", username).setParameter("firstN", firstname).setParameter("lastN", lastname).executeUpdate();
        else if (firstname != "")
            query += openSession.createQuery("update User as usr set usr.firstName = :firstN where usr.username = :u").setParameter("u", username)
                    .setParameter("firstN", firstname).executeUpdate();
        else if (lastname != "")
            query += openSession.createQuery("update User as usr set usr.lastName = :lastN where usr.username = :u").setParameter("u", username)
                    .setParameter("lastN", lastname).executeUpdate();
        tx.commit();
        openSession.close();
        return query > 0;
    }
}
