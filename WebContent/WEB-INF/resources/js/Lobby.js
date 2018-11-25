$(document).ready(function() {
	refreshLobbies();
});

function refreshLobbies(){
	//alert("joinLobby NAME = "+lobby_name);
	$.ajax({
		url : "refresh_lobbies",
		type : "POST",
		success : function(resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				//alert("Lobby in for: "+r.lobbies[1].name);
				var row_copy = $(".ontop").clone();
				refreshLocalLobbiesList(row_copy.attr('id'), null, r.lobbies, r.usr);
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
	//alert("joinLobby NAME = "+lobby_name);
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
				//alert("Lobby in for: "+r.lobbies[1].name);
				refreshLocalLobbiesList(id_lobby, null, r.lobbies, r.usr);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
}

function startGame(ev) {
	console.log("ENTRATO IN START GAME");
	$
			.ajax({
				url : "game",
				type : "GET",
				async : false,
				success : function(resultData) {
					console.log("Start game ok: " + resultData);
					deleteLobby(ev);
					window.location.href = "http://localhost:8080/ASDE-puzzle_game/game";
				},
				error : function(e) {
					alert(e.responseText);
					console.log("START GAME ERROR: ", e);
				}
			});
}

function createLobby(ev) {
	var lobby_name = $(document.activeElement).closest('.form-group').children(
			'#id_lobby_name').val();
	console.log("ENTRATO IN CREATE LOBBY lobby_name:" + lobby_name);
	$.ajax({
		url : "create_lobby",
		type : "POST",
		async : false,
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("lobby create ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				var id_lobby = "id_lobby_"+r.new_lobby.name;
				$("#"+id_lobby).addClass("ontop");
				putLobbyOnTop(id_lobby);
				$('#create-modal').hide();
				putLobbyOnTop(id_lobby);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("LOBBY CREATE ERROR: ", e);
		}
	});
}

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

function searchLobby(ev, searchBy) {
	var name = $(document.activeElement).closest(".form-group").children(
			'#id_search_txt').val();
	console.log("ENTRATO IN search lobby by " + searchBy + ":" + name);
	$.ajax({
		url : "search_lobby",
		type : "POST",
		async : false,
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
				var id_lobby = "id_lobby_"+r.lobby_searched.name;
				$("#"+id_lobby).addClass("ontopsearched");
				putLobbyOnTop(id_lobby);
			}
		},
		error : function(ev) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

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

function putLobbyOnTop(id_lobby){
	if(id_lobby !== undefined){
		var row_copy = $("#"+id_lobby).clone();
		//var pos = row_copy.prevAll(".card-with-shadow").length;
		//alert("PUT ON TOP....ELEMENT: "+lobby_row);
		$("#"+id_lobby).remove();
		$("#id_lobbies_list_ul").prepend(row_copy);
	}else{
		console.log("PUT on top id UNDEFINED");
	}
	console.log("PUT LOBBY ON TOP");
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

function refreshLocalLobbiesList(id_ontop, id_searched, lobbies, user){
	clearLobbiesList();
	buildLobbyRows(lobbies, user);
	if(id_ontop !== undefined && id_ontop !== null && id_ontop !== ""){
		$("#"+id_ontop).addClass("ontop");
	}
	if(id_searched !== undefined && id_searched !== null && id_searched !== ""){
		$("#"+id_searched).addClass("ontop");
	}
	putLobbyOnTop(id_ontop);
	putLobbyOnTop(id_searched);
}