package it.mat.unical.asde.project2_puzzle.components.services.utility;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class MessageMaker {
	public static final String JOINER = "joiner";
	public static final String LOBBY_NAME = "lobby_name";
	public static final String OWNER = "owner";
	public final static String JOIN_MESSAGE = "join";
	public final static String START_MESSAGE = "start";
	public final static String LEAVE_MESSAGE = "leave";
	public final static String CHAT_MESSAGE = "message";
	public final static String UPDATE_MESSAGE = "update";
	public final static String PROGRESS_MESSAGE = "progress";
	public final static String TEXT_MESSAGE = "message_text";
	public final static String WHO_JOIN = "joiner";
	public static final String FOR_CLEANING = "for_cleaning";
	public static final String END_GAME = "end_game";
	public static final String CAUSE_OFFLINE = "player_offline";
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";

	public String makeMessage(String message_type) {
		return (new JSONObject().put(message_type, true)).toString();
	}

	public String makeMessage(String message_type, boolean value) {
		return (new JSONObject().put(message_type, value)).toString();
	}

	public String makeMessage(String message_type, String message, String message_content) {
		return (new JSONObject().put(message_type, true).put(message, message_content)).toString();
	}

}
