package it.mat.unical.asde.project2_puzzle.components.services;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.mat.unical.asde.project2_puzzle.components.persistence.CredentialsDAO;
import it.mat.unical.asde.project2_puzzle.components.persistence.MatchDAO;
import it.mat.unical.asde.project2_puzzle.model.Credentials;
import it.mat.unical.asde.project2_puzzle.model.Match;

@Service
public class AccountService{
    @Autowired
    private MatchDAO matchDAO;
    @Autowired
    private CredentialsDAO userDAO;

    public boolean accountCreated(String username,String password){
        return userDAO.save(new Credentials(username, password));
    }

    public void addMatch(Match match){
        matchDAO.save(match);
    }

    public ArrayList<Match> getMatches(String username){
        return matchDAO.getMatches(username);
    }

    public boolean loginAccepted(String username,String password){
        return userDAO.exists(new Credentials(username, password));
    }
}
