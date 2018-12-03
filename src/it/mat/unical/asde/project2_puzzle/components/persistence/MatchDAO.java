package it.mat.unical.asde.project2_puzzle.components.persistence;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import it.mat.unical.asde.project2_puzzle.model.GameMatch;
import it.mat.unical.asde.project2_puzzle.model.User;

@Repository
public class MatchDAO {

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


}
