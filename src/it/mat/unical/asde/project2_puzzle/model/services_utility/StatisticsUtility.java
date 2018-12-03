package it.mat.unical.asde.project2_puzzle.model.services_utility;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import it.mat.unical.asde.project2_puzzle.model.GameMatch;
import it.mat.unical.asde.project2_puzzle.model.User;

public class StatisticsUtility {

	public static String createMatchesInfoLineChart(User user) {
		String matches = null;
		if (user.getMatches().isEmpty())
			return matches;
		matches = "";
		HashMap<String, Integer> matchesWin = new HashMap<>();
		HashMap<String, Integer> matchesLose = new HashMap<>();
		for (GameMatch match : user.getMatches()) {
			if (match.getWinner().equals(user))
				matchesWin.put(match.toString(),
						(matchesWin.get(match.toString()) != null ? matchesWin.get(match.toString()) : 0) + 1);
			else
				matchesLose.put(match.toString(),
						(matchesLose.get(match.toString()) != null ? matchesLose.get(match.toString()) : 0) + 1);
		}

		Set<String> keys = new HashSet<>();
		keys.addAll(matchesWin.keySet());
		keys.addAll(matchesLose.keySet());

		for (String key : keys) {
			String match = key + "," + matchesWin.get(key) + "," + matchesLose.get(key) + ",";
			matches += match;
		}
		matches = matches.substring(0, matches.length() - 1);
		return matches;
	}

	public static String createMatchesInfoForDonutChart(User user) {
		int matches[] = { 0, 0 };
		if (user.getMatches().isEmpty())
			return null;

		int win = 0;
		int lose = 1;

		for (GameMatch match : user.getMatches()) {
			if (match.getWinner().equals(user))
				matches[win]++;
			else
				matches[lose]++;
		}
		return matches[win] + "," + matches[lose];
	}

}
