var endGame = false;
var bar;
var value;
var numHint = 3;
var timer;

function allowHint() {

	if (numHint > 0) {
		numHint--;
		$('#show-modal').modal("toggle");
		if (numHint === 0)
			$('#show-image-button').prop("disabled", true);
		$("#numHint").text("Hint remains: " + numHint);
	}
}


function allowDrop(ev) {
	if ( /* !ev.target.hasChildNodes() && */
		ev.target.getAttribute("class") == "box_piece") {
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
document.addEventListener("dragenter", function (event) {
	if (!event.target.hasChildNodes() &&
		event.target.getAttribute("class") == "box_piece") {
		event.target.style.background = "#C37500";
	}
}, false);

document.addEventListener("dragleave", function (event) {
	if (!event.target.hasChildNodes() &&
		event.target.getAttribute("class") == "box_piece") {
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
		url: "move_piece",
		type: "POST",
		data: ({
			"old_location": old,
			"old_position": old_position,
			"new_location": ev.target.parentElement.getAttribute("id"),
			"new_position": $(ev.target).prevAll(".box_piece").length,
			"piece": data,
			"timer": timer.getTimeValues().toString()
		}),
		success: function (resultData) {
			if(resultData==="error")
				swal("Update Error","Sorry, an error was occured","error");
		},
		error: function (e) {
			console.log("old position" + old_position + "\n" +
				"new_location   " +
				ev.target.parentElement.getAttribute("id") + "\n" +
				"old_location   " + old + "\n" + "piece  " + data + "\n" +
				"new_position   " +
				$(ev.target).prevAll(".box_piece").length);
			console.log(e.responseText);
			console.log("ERROR: ", e);
		}
	});
}

function createMessageNode(message) {
	var li = document.createElement("LI");
	li.classList.add("list-group-item");
	li.classList.add("message-box-card");
	var t = document.createTextNode(message);
	li.appendChild(t);
	return li;
}

function appendMessage(message, isSender) {
	var node = createMessageNode(message);
	node.className += " col-12";
	if (isSender) {
		node.className += " justify-content-end";
		node.style.background = "#DAA520";
	} else
		node.style.background = "#C8C8C8";
	node.style.color = "black";
	document.getElementById("chat_content").appendChild(node);
	var pNode = document.getElementById("chat_content").parentNode.parentNode;

	var numberBefore = $("#chat_content").children().length;
	var toSum=node.clientHeight;
	console.log(numberBefore);
	if (numberBefore > 6)
		pNode.scrollTop = pNode.scrollHeight;
	else if (numberBefore >2)
	    pNode.scrollTop=(numberBefore*toSum)-toSum;
}

function getEventsFromServer() {
	// var gameId = $("#gameId").val();
	$.ajax({
		url: "get_progress",
		type: "post",
		data: ({
			// "gameId" : gameId
		}),
		success: function (result) {
			if ($.trim(result)) {				
					var r = JSON.parse(result);
					if(r.end_game){
						endGame=true;
						if(r.player_offline)
							window.location.href = "/ASDE-puzzle_game/end_game?offline="+r.player_offline;
						else
							window.location.href = "/ASDE-puzzle_game/end_game";
					}else
					if (r.message) {
						appendMessage(r.message_text, false);
					} else {
						value = r.progress;
						bar.animate(value / 100);
					}	
				}
			getEventsFromServer();
		},
		error: function (e) {
			console.log(e.responseText);
			setTimeout(function () {
				getEventsFromServer();
			}, 5000);
		}
	});
}

function makeRequest(action, type, data, onsuccess, onerror) {
	$.ajax({
		url: action,
		type: type,
		data: data,
		success: onsuccess,
		error: onerro
	});

}



function sendMessage() {
	var message = $("#message_text").val();
	$("#message_text").val("");
	$.ajax({
		url: "send_message",
		type: "post",
		data: ({
			"message": message
		}),
		success: function (result) {
			if(result==="error")
				swal("Error Chat","Sorry, an error was occured","error");
			else
				appendMessage(message, true);
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});
}



window.onbeforeunload = function () {
	if (!endGame)
		return "Are you sure";
	else
		return;
};
window.onunload = function () {
	alert(!endGame);
	if (!endGame)
		$.ajax({
			url: "leave_game",
			async: false,
			data: ({})
		});
}

function initProgressBar() {
	bar = new ProgressBar.SemiCircle(dynamic, {
		strokeWidth: 6,
		color: '#FFEA82',
		trailColor: '#eee',
		trailWidth: 1,
		easing: 'easeInOut',
		duration: 1400,
		svgStyle: null,
		text: {
			value: '',
			alignToBottom: false
		},
		from: {
			color: '#FFEA82'
		},
		to: {
			color: '#ED6A5A'
		},
		// Set default step function for all animate calls
		step: (state, bar) => {
			bar.path.setAttribute('stroke', state.color);
			value = Math.round(bar.value() * 100);
			if (value === 0) {
				bar.setText('0 %');
			} else {
				bar.setText(value + " %");
			}

			bar.text.style.color = state.color;
		}
	});
	bar.text.style.fontSize = '2rem';

}
$(document).ready(function () {
	// if (performance.navigation.type == 1) {
	// window.location.href = "/ASDE-puzzle_game/end_game";
	// }
	getEventsFromServer();
	initProgressBar();
	$("#numHint").text("Hint remains: " + numHint);
	timer = new Timer();
	timer.start();
	timer.addEventListener('secondsUpdated', function (e) {
		$('#time').html(timer.getTimeValues().toString());
	});
});