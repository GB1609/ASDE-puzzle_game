$(document).ready(function() {
	refreshLobbies();
	orderLobbies("lobbyOnTop");
	orderLobbies("lobbyCreated");
	// $('#base').addClass($('#base').attr('value'));
});

function orderLobbies(class_label){
	var id_ontop = $("."+class_label).attr('id');
	if(id_ontop !== undefined){
		console.log("LOBBY ON TOP :"+id_ontop.attr('class'));
		putLobbyOnTop(id_ontop);
	}else{
		console.log("LOBBY ON TOP UNDEFINED... SESSION: "+"#"+sessionStorage.getItem(class_label));
		id_ontop = $("#"+sessionStorage.getItem(class_label)).attr('id');
		if(id_ontop !== undefined){
			putLobbyOnTop(id_ontop);
		}else{
			console.log("LOBBY on top UNDEFINED ALSO IN SESSION");
		}
	}
	
}

function refreshLobbies(){
	$.ajax({
		url : "refresh_lobbies",
		type : "POST",
		success : function(resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				refreshLocalLobbiesList(r.lobbies, r.usr);
				orderLobbies("lobbyOnTop");
				orderLobbies("lobbyCreated");
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("REFRESH ERROR: ", e);
		}
	});
}

function joinLobby(ev, id_lobby) {
	var lobby_name = $("#"+id_lobby).children(
	'#lobby_name_div').text();
	console.log(lobby_name);
	ev.preventDefault();
	$.ajax({
		url : "join_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("join ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				refreshLocalLobbiesList(r.lobbies, r.usr);
				sessionStorage.setItem('lobbyOnTop', id_lobby);
				$("#"+id_lobby).addClass("lobbyOnTop");
				putLobbyOnTop(id_lobby);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
}

function startGame(ev) {
	$.ajax({
		url : "game",
		type : "GET",
		success : function(resultData) {
			console.log("Start game ok: " + resultData);
			//deleteLobby(ev);
			window.location.href = "http://localhost:8080/ASDE-puzzle_game/game";
		},
		error : function(e) {
			alert(e.responseText);
			console.log("START GAME ERROR: ", e);
		}
	});
}

function createLobby(ev) {
	var lobby_name = $('#id_lobby_name').val();
	console.log("ENTRATO IN CREATE LOBBY lobby_name:" + lobby_name);
	$.ajax({
		url : "create_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("lobby create ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				refreshLocalLobbiesList(r.lobbies, r.usr);
				var id_lobby = "id_lobby_"+r.new_lobby;
				sessionStorage.setItem('lobbyCreated', id_lobby);
				$("#"+id_lobby).addClass("lobbyCreated");
				putLobbyOnTop(id_lobby);
				refreshLobbies();
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
	var name = $(document.activeElement).closest(".form-group").children(
			'#id_search_txt').val();
	ev.preventDefault();
	$.ajax({
		url : "search_lobby",
		type : "POST",
		data : ({
			"search_txt" : name,
			"search_by" : searchBy
		}),
		success : function(resultData) {
			console.log("lobby search ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				var id_lobby = "id_lobby_"+r.lobby_searched;
				console.log("LOBBY SEARCHED: "+id_lobby);
				$("#"+id_lobby).addClass("searched");
				putLobbyOnTop(id_lobby);
			}
		},
		error : function(ev) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function putLobbyOnTop(id_lobby){
	if(id_lobby !== undefined){
		console.log("PUT LOBBY ON TOP");
		var row_copy = $("#"+id_lobby).clone();
		$("#"+id_lobby).remove();
		$("#id_lobbies_list_ul").prepend(row_copy);
	}else{
		console.log("PUT on top id UNDEFINED");
	}
}

function buildLobbyRow(id,name,owner,guest,username){
	var newLobby = "";
	newLobby += "<li class=\"list-group-item card-with-shadow lobby_row\" id=\"id_lobby_"+name+"\" >"
	+"<div class=\"text-center\" id=\"lobby_name_div\">"+name+"</div>"
	+"<div class=\" text-center\">"
	+"<img src=\"resources/images/avatar.svg\" class=\"img-circle\" height=\"64\" width=\"64\" alt=\"Avatar\">";
	if(owner != ""){
		newLobby+="<span>"+owner+"</span>";
	}else{
		newLobby+="<span>EMPTY</span>";
	}
	newLobby+="<img style=\"max-height: 1cm; max-width: 1cm; margin-left: 0%;\" src=\"resources/img/vs.jpg\">";
	if(guest != ""){
		newLobby+="<span>"+guest+"</span>";
	}else{
		newLobby+="<span>EMPTY</span>";
	}
	newLobby+="<img src=\"resources/images/avatar.svg\" class=\"img-circle\"	height=\"64\" width=\"64\" alt=\"Avatar\">"
	if(username != owner){
		newLobby+="<button id=\"join_btn_lobby_"+name+"\" type=\"button\" onclick=\"joinLobby(event,'id_lobby_"+name+"')\" class=\"btn btn-warning btn-lg float-right\">Join</button>";
	}else{
		newLobby+="<button id=\"start_btn_lobby_"+name+"\" type=\"button\" onclick=\"startGame()\" class=\"btn btn-warning btn-lg float-right\">Start</button>";
	}
	newLobby+="</div>"
	+"</li>";
	return newLobby;
}

function htmlToElement(html) {
    var template = document.createElement('template');
    html = html.trim(); // Never return a text node of whitespace as the result
    template.innerHTML = html;
    return template.content.firstChild;
}

//00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
function refreshLocalLobbiesList(lobbies, user){
	clearLobbiesList();
	buildLobbyRows(lobbies, user);
}
function clearLobbiesList(){
	var list = document.getElementById("id_lobbies_list_ul");
	while (list.firstChild) {
		list.removeChild(list.firstChild);
	}
}
function buildLobbyRows(lobbies, user){
	var list = document.getElementById("id_lobbies_list_ul");
	for ( var i in lobbies) {
		var lobby = lobbies[i];
		var id= lobby.id;
		var name = lobby.name;
		var owner = lobby.owner;
		var guest = lobby.guest;
		var username = user;
		var newLobby = buildLobbyRow(id,name,owner,guest,username);
		list.append(htmlToElement(newLobby));
	}
}
//00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000


/*
function deleteLobby(ev) {
	var lobby_name = $(document.activeElement).closest('.lobby_row').children(
			'#lobby_name_div').text();
	console.log("ENTRATO IN START GAME lobby_name:" + lobby_name);
	$.ajax({
		url : "delete_lobby_by_name",
		type : "POST",
		async : false,
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("lobby delete ok: " + resultData);
			refreshDivByID(resultData, "lobbies_div");
		},
		error : function(e) {
			alert(e.responseText);
			console.log("LOBBY DELETE ERROR: ", e);
		}
	});
}
*/

/*function refreshDivByID(resultData, id_div) {
	var r = JSON.parse(resultData);
	if (r.error) {
		alert("ERROR: " + r.err_msg);
	} else {
		simpleRefreshByID(id_div);
	}
}

function simpleRefreshByID(id_div){
	//alert("SIMPLE refresh: "+id_div);
	console.log("REFRESH LOBBIES LIST");
	$("#" + id_div).load(location.href + " #" + id_div + ">*", "");
}*/