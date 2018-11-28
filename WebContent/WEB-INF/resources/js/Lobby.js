var listElm;
var user;
var currentlyShowed;
$(document)
		.ready(
				function() {
					listElm = document.querySelector('#lobbies_div');
					listElm
							.addEventListener(
									'scroll',
									function() {
										if (listElm.scrollTop
												+ listElm.clientHeight >= (listElm.scrollHeight - 1)) {
											getLobbies(false);
										}
									});
					var created_lobby = $("#created_lobby");
					if (performance.navigation.type == 1
							&& created_lobby.val() === "created") {
						var lobby_name = $.trim($("#created_lobby").parent()
								.prev().text());
						listenForJoinToLobby(lobby_name);
					}
					currentlyShowed = 0;
					getLobbies(true);
					// $('#base').addClass($('#base').attr('value'));
				});

var grid=false;
function changeTypeList() {
	var element = document.getElementById("id_lobbies_list_ul");
	if (grid) {
		element.classList.remove("grid-list-view");
		grid = false;
	} else {
		element.classList.add("grid-list-view");
		grid = true;
	}
}
function listenForStartGame(lobby_name) {
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
				var r = JSON.parse(result);
				if (r.start) {
					window.location.href = "/ASDE-puzzle_game/game";
				} else if (r.leave) {
					alert("Lobby destruct");// TODO make alert
				}
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

function getLobbies(reset) {
	if(reset){
		currentlyShowed = 0;
	}
	alert("currentlyShowed : " + currentlyShowed);
	$.ajax({
		url : "get_lobbies",
		type : "POST",
		data : ({
			"currently_showed" : currentlyShowed
		}),
		success : function(resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				user = r.username;
				if(Object.keys(r.lobbies).length){
					currentlyShowed += Object.keys(r.lobbies).length;
				}
				reloadList(reset, r.lobbies, r.lobbies_guest, r.lobbies_owner);
			}
		},
		error : function(e) {
			alert(e.responseText);
			console.log("REFRESH ERROR: ", e);
		}
	});
}

function joinLobby(lobby_name) {
	// var lobby_name = $("#" + id_lobby).children('#lobby_name_div').text();
	console.log("in join lobby");
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
				listenForStartGame(lobby_name);
			}
		},
		error : function(e) {
			console.log(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
	ev.preventDefault();
}


function startGame(ev) {
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
			if ($.trim(result) && !(result === "already-joined")) {
				var r = JSON.parse(result);
				if (r.join) {
					// TODO listen for leave lobby
					$("#start_button").removeClass("hidden-field");
					$('#join_alert').fadeIn('slow', function() {
						$('#join_alert').delay(5000).fadeOut();
					});
				} else if (r.leave) {
					$("#start_button").addClass("hidden-field");
					$('#leave_alert').fadeIn('slow', function() {
						$('#leave_alert').delay(5000).fadeOut();
					});
				}
				// $("#lobby_name").val(lobby_name);
				// $("#ftg_form").submit();
			}
			listenForJoinToLobby(lobby_name);

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
				listenForJoinToLobby(lobby_name);
			}
		},
		error : function(e) {
			console.log(e.responseText);
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
				+ "\" type=\"button\" onclick=\"joinLobby('"
				+ name
				+ "')\" class=\"btn btn-warning btn-lg float-right\">Join</button>";
	} else {
		
		newLobby += "<input id=\"created_lobby\" type=\"hidden\" value=\"created\" />";
		newLobby += "<button id=\"start_button\" type=\"button\" onclick=\"startGame()\" class=\"btn btn-warning btn-lg float-right hidden-field\">Start</button>";
		newLobby += "<div id=\"join_alert\" class=\"alert alert-info hidden-field\" role=\"alert\">A player joined to lobby</div>";
		newLobby += "<div id=\"leave_alert\" class=\"alert alert-danger hidden-field\" role=\"alert\">The player leaved the lobby</div>";
		newLobby += "<form style=\"display: hidden\" action=\"forward_to_game\"	method=\"post\" id=\"ftg_form\">";
		newLobby += "<input type=\"hidden\" id=\"lobby_name\" name=\"lobby_name\" value=\""+ name +"\" />";
		newLobby += "</form>";
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
