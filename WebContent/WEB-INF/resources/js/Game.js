var endGame = false;
function allowDrop(ev) {
	if (!ev.target.hasChildNodes()
			&& ev.target.getAttribute("class") == "box_piece") {
		ev.preventDefault();
	}
}

function drag(ev) {
	ev.dataTransfer.setData("pieceMoved", ev.target.id);
	ev.dataTransfer.setData("old_location",
			ev.target.parentElement.parentElement.id);
	ev.dataTransfer.setData("old_position", $(ev.target.parentElement).prevAll(
			".box_piece").length);
}
document.addEventListener("dragenter", function(event) {
	if (!event.target.hasChildNodes()
			&& event.target.getAttribute("class") == "box_piece") {
		event.target.style.background = "#C37500";
	}
}, false);

document.addEventListener("dragleave", function(event) {
	if (!event.target.hasChildNodes()
			&& event.target.getAttribute("class") == "box_piece") {
		event.target.style.background = "";
	}
}, false);

function drop(ev) {
	ev.preventDefault();
	event.target.style.background = "";
	var data = ev.dataTransfer.getData("pieceMoved");
	var old = ev.dataTransfer.getData("old_location");
	var old_position = ev.dataTransfer.getData("old_position");
	ev.target.appendChild(document.getElementById(data));
	$.ajax({
		url : "move_piece",
		type : "POST",
		data : ({
			"old_location" : old,
			"old_position" : old_position,
			"new_location" : ev.target.parentElement.getAttribute("id"),
			"new_position" : $(ev.target).prevAll(".box_piece").length,
			"piece" : data
		}),
		success : function(resultData) {
			console.log("ok" + resultData);
		},
		error : function(e) {

			console.log("old position" + old_position + "\n"
					+ "new_location   "
					+ ev.target.parentElement.getAttribute("id") + "\n"
					+ "old_location   " + old + "\n" + "piece  " + data + "\n"
					+ "new_position   "
					+ $(ev.target).prevAll(".box_piece").length);
			console.log(e.responseText);

			console.log("ERROR: ", e);
		}
	});
}

function getEventsFromServer() {
	// var gameId = $("#gameId").val();
	$.ajax({
		url : "get_progress",
		type : "post",
		data : ({
		// "gameId" : gameId
		}),
		success : function(result) {
			if ($.trim(result)) {
				if (result == "END-GAME") {
					endGame = true;
					window.location.href = "/ASDE-puzzle_game/end_game";
				} else
					$("#dynamic").css("width", result + "%").attr(
							"aria-valuenow", result)
							.text(result + "% Complete");

			}
			getEventsFromServer();
		},
		error : function(e) {
			console.log(e.responseText);
			setTimeout(function() {
				getEventsFromServer();
			}, 5000);
		}
	});
}
window.onbeforeunload = function() {
	if (!endGame)
		return "Are you sure";
	else
		return;
};
window.onunload = function() {
	$.ajax({
		url : "leave_game",
		async : false,
		data : ({})
	});
}
$(document).ready(function() {
	if (performance.navigation.type == 1) {
		window.location.href = "/ASDE-puzzle_game/end_game";
	}
	getEventsFromServer();
});
