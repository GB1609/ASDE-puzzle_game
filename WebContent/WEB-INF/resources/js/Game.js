var endGame = false;
var bar;
var value;
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

function createMessageNode(message){
	var div = document.createElement("DIV");
	var t = document.createTextNode(message);
	div.appendChild(t);
	return div;
}
function appendMessage(message, isSender){
	var node=createMessageNode(message);
	if(isSender)
		node.style.background="#DAA520";
	else
		node.style.background="darkorange";
	document.getElementById("chat_content").appendChild(node);	
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
					{					
						var r = JSON.parse(result);
						if (r.message) {
							appendMessage(r.message_text,false);
						} else {
							value=r.progress;
							bar.animate(value/100);
						}					
						
					}

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

function makeRequest(action, type, data, onsuccess, onerror){
	$.ajax({
		url : action,
		type : type,
		data : data,
		success : onsuccess,
		error : onerro
	});
	
}



function sendMessage(){
	var message=$("#message_text").val();
	$.ajax({
		url : "send_message",
		type : "post",
		data : ({
		"message" : message
		}),
		success : function(result) {
			appendMessage(message,true);
		},
		error : function(e) {
			console.log(e.responseText);
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
function initProgressBar(){
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
		        bar.setText(value+" %");
		      }

		      bar.text.style.color = state.color;
		    }
		  });
		  bar.text.style.fontSize = '2rem';

}
$(document).ready(function() {
	if (performance.navigation.type == 1) {
		window.location.href = "/ASDE-puzzle_game/end_game";
	}
	getEventsFromServer();
	initProgressBar();
		 		 // bar.animate(0.7);
	
	
	
	
});
