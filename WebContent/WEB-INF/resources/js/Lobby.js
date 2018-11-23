$(document).ready(function() {
	// $('#base').addClass($('#base').attr('value'));
});

function joinLobby(ev, id_lobby) {
	var lobby_name = $("#"+id_lobby).children(
	'#lobby_name_div').text();
	console.log(lobby_name);
	//alert("joinLobby NAME = "+lobby_name);
	$.ajax({
		url : "join_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		complete: function(jqXHR, textStatus){
				refreshDivByID( 'lobbies_list', id_lobby);
				//putLobbyOnTop(id_lobby);
			//alert("COMPLETE: " + textStatus);
		},
		success : function(resultData) {
			console.log("join ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				//refreshDivByID(resultData, 'lobbies_list', id_lobby);
//				putLobbyOnTop(id_lobby);
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
			refreshDivByID(resultData, "lobbies_div");
			$('#create-modal').hide();
			putLobbyOnTop(id_lobby);
		},
		error : function(e) {
			alert(e.responseText);
			console.log("LOBBY CREATE ERROR: ", e);
		}
	});
}

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
			putLobbyOnTop(id_lobby);
		},
		error : function(ev) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function refreshDivByID(/*resultData,*/ id_div, id_lobby_on_top) {
	/*var r = JSON.parse(resultData);
	if (r.error) {
		alert("ERROR: " + r.err_msg);
	} else {*/
		simpleRefreshByID(id_div,id_lobby_on_top);
//	}
}

function simpleRefreshByID(id_div,id_lobby_on_top){
	//alert("SIMPLE refresh: "+id_div);
	$("#" + id_div).load(location.href + " #" + id_div + ">*", "");
	var row_copy = $("#"+id_lobby_on_top).clone();
	var pos = row_copy.prevAll(".card-with-shadow").length;
	//alert("PUT ON TOP....ELEMENT: "+lobby_row);
	$("#"+id_lobby_on_top).remove();
	$("#lobbies_list").prepend(row_copy);
}

function putLobbyOnTop(id_lobby){
	var row_copy = $("#"+id_lobby).clone();
	var pos = row_copy.prevAll(".card-with-shadow").length;
	//alert("PUT ON TOP....ELEMENT: "+lobby_row);
	$("#"+id_lobby).remove();
	$("#lobbies_list").prepend(row_copy);
}

