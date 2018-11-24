$(document).ready(function() {
	// $('#base').addClass($('#base').attr('value'));
});

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
			alert(resultData);
			console.log("join ok: " + resultData);
			window.location.href = "/ASDE-puzzle_game/game";
			refreshDivByID(resultData, "lobbies_div");
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

function listenForJoinToLobby(lobby_name) {
	// alert("go to forward ");
	// $("#lobby_name").val(lobby_name);
	// alert("go to forward " + $("#lobby_name").val());
	// alert("go to forward " + $("#ftg_form").attr('method'));
	// $("#ftg_form").submit();
	// alert(lobby_name);
	var xhr = $.ajax({
		url : "check_join",
		type : "post",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(result) {
			// alert("result" + result);
			if (!$.trim(result))
				listenForJoinToLobby(lobby_name);
			else
			// window.location.href = "/ASDE-puzzle_game/forward_to_game?"
			// + lobby_name;
			{
				// alert("go to forward ");
				$("#lobby_name").val(lobby_name);
				// alert("go to forward " + $("#lobby_name").val());
				$("#ftg_form").submit();
			}
		},
		error : function(e) {
			alert(e.responseText);
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
			alert(e.responseText);
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
			refreshDivByID(resultData, "lobbies_div");
		},
		error : function(ev) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function refreshDivByID(resultData, id_div) {
	var r = JSON.parse(resultData);
	if (r.error) {
		alert("ERROR: " + r.err_msg);
	} else {
		$("#" + id_div).load(location.href + " #" + id_div + ">*", "");
	}
}