function listenForStartGame(lobby_name) {
	alert("in join")
	var xhr = $.ajax({
		url : "check_start",
		type : "post",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(result) {
			if (!$.trim(result))
				listenForStartGame(lobby_name);
			else {
				window.location.href = "/ASDE-puzzle_game/game";
				// TODO listen for leave lobby
				// $("#start_button").removeClass("hidden-field");
				// $("#lobby_name").val(lobby_name);
				// $("#ftg_form").submit();
			}
		},
		error : function(e) {
			console.log(e.responseText);
			setTimeout(function() {
				listenForStartGame(lobby_name);
			}, 5000);
		}
	});
	console.log(xhr);

}

function joinLobby(ev) {
	var lobby_name = $(document.activeElement).closest('#lobby_row').children(
			'#lobby_name_div').text();
	console.log(lobby_name);
	$.ajax({
		url : "join_lobby",
		type : "POST",
		async : false,
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			console.log("join ok: " + resultData);
			// window.location.href = "/ASDE-puzzle_game/game";
			var r = JSON.parse(resultData);
			if (!r.error) {
				refreshDivByID(resultData, "lobbies_div");
				listenForStartGame(lobby_name);
			} else {
				alert("Lobby busy");
			}
		},
		error : function(e) {
			console.log(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
}

function startGame(ev) {
	// console.log("ENTRATO IN START GAME");
	// $
	// .ajax({
	// url : "game",
	// type : "GET",
	// async : false,
	// success : function(resultData) {
	// console.log("Start game ok: " + resultData);
	// deleteLobby(ev);
	// window.location.href = "http://localhost:8080/ASDE-puzzle_game/game";
	// },
	// error : function(e) {
	// console.log(e.responseText);
	// console.log("START GAME ERROR: ", e);
	// }
	// });
	// $("#lobby_name").val(lobby_name);
	$("#ftg_form").submit();
}

function listenForJoinToLobby(lobby_name) {
	// alert("in join")
	var xhr = $.ajax({
		url : "check_join",
		type : "post",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(result) {
			if (!$.trim(result))
				listenForJoinToLobby(lobby_name);
			else {
				// TODO listen for leave lobby
				$("#start_button").removeClass("hidden-field");
				// $("#lobby_name").val(lobby_name);
				// $("#ftg_form").submit();
			}
		},
		error : function(e) {
			console.log(e.responseText);
			setTimeout(function() {
				listenForJoinToLobby(lobby_name);
			}, 5000);
		}
	});
	console.log(xhr);

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
			listenForJoinToLobby(lobby_name);
		},
		error : function(e) {
			console.log(e.responseText);
			console.log("LOBBY CREATE ERROR: ", e);
		}
	});
}

function deleteLobby(ev) {
	// ev.preventDefault();
	// ev.target.appendChild(document.getElementById(data));
	var lobby_name = $(document.activeElement).closest('#lobby_row').children(
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
			console.log(e.responseText);
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
			refreshDivByID(resultData, "lobbies_div");
		},
		error : function(ev) {
			console.log(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function refreshDivByID(resultData, id_div) {
	var r = JSON.parse(resultData);
	if (r.error) {
		console.log("ERROR: " + r.err_msg);
	} else {
		$("#" + id_div).load(location.href + " #" + id_div + ">*", "");
	}
}

$(document).ready(function() {
	var created_lobby = $("#created_lobby");
	if (performance.navigation.type == 1 && created_lobby.val() === "created") {
		var lobby_name = $.trim($("#created_lobby").parent().prev().text());
		listenForJoinToLobby(lobby_name);
	}
});