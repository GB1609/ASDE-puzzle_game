var listElm;
var user;
$(document).ready(	function() {
	listElm = document.querySelector('#lobbies_div');
	listElm.addEventListener('scroll',
	function() {
		if (listElm.scrollTop
				+ listElm.clientHeight >= (listElm.scrollHeight - 1)) {
			getLobbies(false);
		}
	});
	getLobbies(true);
	// $('#base').addClass($('#base').attr('value'));
});

function getLobbies(reset) {
	$.ajax({
		url : "get_lobbies",
		type : "POST",
		data : ({
			"reset_counter" : reset
		}),
		success : function(resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				user = r.username;
				reloadList(reset, r.lobbies, r.lobbies_guest, r.lobbies_owner);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("REFRESH ERROR: ", e);
		}
	});
}

function joinLobby(ev, id_lobby) {
	var lobby_name = $("#" + id_lobby).children('#lobby_name_div').text();
	$.ajax({
		url : "join_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("join ok");//: " + resultData);

			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				getLobbies(true);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
	ev.preventDefault();
}

function createLobby(ev) {
	var lobby_name = $('#id_lobby_name').val();
	$.ajax({
		url : "create_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("lobby create ok");//: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				getLobbies(true);
				$('#create-modal').modal("toggle");
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("LOBBY CREATE ERROR: ", e);
		}
	});
}

function searchLobby(ev, searchBy) {
	var name = $('#id_search_txt').val();
	ev.preventDefault();
	$.ajax({
		url : "search_lobby",
		type : "POST",
		data : ({
			"search_txt" : name,
			"search_by" : searchBy
		}),
		success : function(resultData) {
			console.log("lobby search ok");//: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				var jsonArray = [r.lobby_searched];
				putLobbyOnTop(jsonArray);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function startGame(ev, id_lobby) {
	var lobby_name = $("#" + id_lobby).children('#lobby_name_div').text();
	$.ajax({
		url : "delete_lobby_by_name",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name,
		}),
		success : function(resultData) {
			console.log("Start game ok");//: " + resultData);
			window.location.href = "http://localhost:8080/ASDE-puzzle_game/game";
		},
		error : function(e) {
			alert(e.responseText);
			console.log("START GAME ERROR: ", e);
		}
	});
}
// 0000000000000000000000000000000000000000000000000000000000000000000000000000000
// 00000000000000000000000 - UTILITY - 0000000000000000000000000000000000000000000
// 0000000000000000000000000000000000000000000000000000000000000000000000000000000

var reloadList = function (reset, lobbies, lobbies_guest, lobbies_owner){
	if(reset){
		clearLobbiesList();
	}
	loadMore(lobbies, lobbies_guest, lobbies_owner);
}

var clearLobbiesList = function () {
	var list = document.getElementById("id_lobbies_list_ul");
	while (list.firstChild) {
		list.removeChild(list.firstChild);
	}
}

var loadMore = function(lobbies, lobbies_guest, lobbies_owner) {
	var list = document.getElementById("id_lobbies_list_ul");
	console.log("ENTERED ON loadMore");//:"+lobbies);
	for(var i in lobbies){
		var lobby = lobbies[i];
		var id = lobby.id;
		var name = lobby.name;
		var owner = lobby.owner;
		var guest = lobby.guest;
		var newLobby = buildLobbyRow(id, name, owner, guest, user);
		if(conteinedIn(lobby, lobbies_guest) === false && conteinedIn(lobby, lobbies_owner) === false){
			list.append(htmlToElement(newLobby));
		}
	}
	putLobbyOnTop(lobbies_guest);
	putLobbyOnTop(lobbies_owner);
	
}
function conteinedIn(lobby, list) {
	for ( var i in list) {
		var tmp = list[i];
		if (lobby.name === tmp.name) {
			console.log("conteinedIn lobbies[i]:" + tmp.name);
			return true;
		}
	}
	return false;
}
function putLobbyOnTop(lobbies) {
	var list = document.getElementById("id_lobbies_list_ul");
	for(var i in lobbies){
		var lobby = lobbies[i];
		var id = lobby.id;
		var name = lobby.name;
		var owner = lobby.owner;
		var guest = lobby.guest;
		var newLobby = buildLobbyRow(id, name, owner, guest, user);
		if($("#id_lobby_" + name)!== undefined){
			$("#id_lobby_" + name).remove();	
			console.log("loadputLobbyOnTop...removed:"+name);
		}
		console.log("loadputLobbyOnTop:"+name);
		list.prepend(htmlToElement(newLobby));
	}
}
function buildLobbyRow(id, name, owner, guest, username) {
	var newLobby = "";
	newLobby += "<li class=\"list-group-item card-with-shadow lobby_row\" id=\"id_lobby_"
			+ name
			+ "\" >"
			+ "<div class=\"text-center\" id=\"lobby_name_div\">"
			+ name
			+ "</div>"
			+ "<div class=\" text-center\">"
			+ "<img src=\"resources/images/avatar.svg\" class=\"img-circle\" height=\"64\" width=\"64\" alt=\"Avatar\">";
	if (owner != "") {
		newLobby += "<span>" + owner + "</span>";
	} else {
		newLobby += "<span>EMPTY</span>";
	}
	newLobby += "<img style=\"max-height: 1cm; max-width: 1cm; margin-left: 0%;\" src=\"resources/img/vs.jpg\">";
	if (guest != "") {
		newLobby += "<span>" + guest + "</span>";
	} else {
		newLobby += "<span>EMPTY</span>";
	}
	newLobby += "<img src=\"resources/images/avatar.svg\" class=\"img-circle\"	height=\"64\" width=\"64\" alt=\"Avatar\">"
	if (username != owner) {
		newLobby += "<button id=\"join_btn_lobby_"
				+ name
				+ "\" type=\"button\" onclick=\"joinLobby(event,'id_lobby_"
				+ name
				+ "')\" class=\"btn btn-warning btn-lg float-right\">Join</button>";
	} else {
		newLobby += "<button id=\"start_btn_lobby_"
				+ name
				+ "\" type=\"button\" onclick=\"startGame('#id_lobby_"+ name+"')\" class=\"btn btn-warning btn-lg float-right\">Start</button>";
	}
	newLobby += "</div>" + "</li>";
	return newLobby;
}

function htmlToElement(html) {
	var template = document.createElement('template');
	html = html.trim(); // Never return a text node of whitespace as the result
	template.innerHTML = html;
	return template.content.firstChild;
}
// 00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
