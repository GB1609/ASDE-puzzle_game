var listElm;
var lastIndex;
var lobbies;
var user;
$(document)
		.ready(
				function() {
					getLobbies();
					// orderLobbies("lobby_on_top");
					// orderLobbies("lobby_created");
					// lobbies = JSON.parse(sessionStorage.getItem('lobbies'));
					// user = sessionStorage.getItem('user');
					// for infinite scroll
					// Detect when scrolled to bottom.
					// lastIndex = 0; //to save index of last lobby shown
					listElm = document.querySelector('#lobbies_div');
					listElm
							.addEventListener(
									'scroll',
									function() {
										// console.log("padding:"+$(listElm).css('padding')+"|||listElm.scrollTop:"+listElm.scrollTop+"|
										// listElm.clientHeight:"+listElm.clientHeight+">=listElm.scrollHeight:"+listElm.scrollHeight+"
										// - padding:"+
										// $(listElm).css('padding'));
										if (listElm.scrollTop
												+ listElm.clientHeight >= (listElm.scrollHeight - 5)) {
											loadMore();
										}
									});
					// Initially load some items.
					// resetList();
					// $('#base').addClass($('#base').attr('value'));
				});

function getLobbies() {
	$.ajax({
		url : "get_lobbies",
		type : "POST",
		success : function(resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				// sessionStorage.setItem('lobbies', JSON.stringify(r.lobbies));
				// sessionStorage.setItem('user', r.usr);
				lobbies = r.lobbies;
				user = r.usr;
				resetList();
				orderLobbies("lobby_on_top");
				orderLobbies("lobby_created");
				// loadMore();
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
	console.log(lobby_name);
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
				if (r.joined) {
					lobbies = r.lobbies;
					resetList(); // refreshLocalLobbiesList(r.lobbies,
					// r.usr);
					sessionStorage.setItem('lobby_on_top', lobby_name);
					putLobbyOnTop(lobby_name);
				} else {
					alert("Lobby no more available");
				}
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
				lobbies = r.lobbies;
				resetList(); // refreshLocalLobbiesList(r.lobbies, r.usr);
				// var id_lobby = "id_lobby_"+r.new_lobby;
				sessionStorage.setItem('lobby_created', r.new_lobby);
				// $("#"+id_lobby).addClass("lobby_created");
				putLobbyOnTop(r.new_lobby);
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
			console.log("lobby search ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				alert("ERROR: " + r.err_msg);
			} else {
				lobbies = r.lobbies;
				resetList();
				// var id_lobby = "id_lobby_"+r.lobby_searched;
				// console.log("LOBBY SEARCHED: "+id_lobby);
				// $("#"+id_lobby).addClass("searched");
				putLobbyOnTop(r.lobby_searched);
			}
		},
		error : function(ev) {
			alert(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

function startGame(ev) {
	$
			.ajax({
				url : "game",
				type : "GET",
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

// 000000000000000000000000000000000 - UTILITY -
// 00000000000000000000000000000000000000000000000000000000000000000000000000000
function orderLobbies(class_label) {
	var id_ontop = $("." + class_label).attr('id');
	if (id_ontop !== undefined) {
		var name = id_ontop.replace('id_lobby_', '');
		console.log("LOBBY ON TOP :" + id_ontop.attr('class'));
		putLobbyOnTop(name);
	} else {
		console.log("LOBBY ON TOP UNDEFINED... SESSION: " + "#id_lobby_"
				+ sessionStorage.getItem(class_label));
		id_ontop = $("#id_lobby_" + sessionStorage.getItem(class_label)).attr(
				'id');
		// if(id_ontop !== undefined){
		// var name = id_ontop.replace('id_lobby_', '');
		putLobbyOnTop(sessionStorage.getItem(class_label));
		// }else{
		// console.log("LOBBY on top UNDEFINED ALSO IN SESSION");
		// }
	}

}
/*
 * function putLobbyOnTop2(nome_lobby){ var list =
 * document.getElementById("id_lobbies_list_ul"); if(nome_lobby !== undefined){
 * $("#id_lobby_"+name).remove(); var lobby = getLobbyByName(nome_lobby); var
 * id= lobby.id; var name = lobby.name; var owner = lobby.owner; var guest =
 * lobby.guest; var username = user; $("#id_lobby_"+nome_lobby).remove();
 * //console.log("on TOP 2
 * id:"+id+"|name:"+name+"|owner:"+owner+"|guest:"+guest+"|username:"+username);
 * var newLobby = buildLobbyRow(id,name,owner,guest,username);
 * list.prepend(htmlToElement(newLobby)); } }
 */
function putLobbyOnTop(nome_lobby) {
	var list = document.getElementById("id_lobbies_list_ul");
	if (nome_lobby !== undefined) {
		console.log("PUT LOBBY ON TOP");
		$("#id_lobby_" + nome_lobby).remove();
		var lobby = getLobbyByName(nome_lobby);
		// console.log("lobby json:"+lobby);
		var id = lobby.id;
		var name = lobby.name;
		var owner = lobby.owner;
		var guest = lobby.guest;
		var username = user;
		var newLobby = buildLobbyRow(id, name, owner, guest, username);
		list.prepend(ciao);
	} else {
		console.log("PUT on top: ID UNDEFINED");
	}
}

function getLobbyByName(name) {
	for ( var i in lobbies) {
		var lobby = lobbies[i];
		if (name === lobby.name) {
			console.log("lobbies[i]:" + lobby.name);
			return lobby;
		}
	}
	return undefined;
}

function resetList() {
	clearLobbiesList();
	lastIndex = 0;
	loadMore();
}

function clearLobbiesList() {
	var list = document.getElementById("id_lobbies_list_ul");
	while (list.firstChild) {
		list.removeChild(list.firstChild);
	}
}

var loadMore = function() {
	var toAdd = 20;// Add 20 items.
	console.log("ENTERED ON loadMore");
	var list = document.getElementById("id_lobbies_list_ul");
	var i = lastIndex;
	for (; (i < lastIndex + toAdd) && (lobbies[i] !== undefined); i++) {
		var lobby = lobbies[i];
		if ((lobby.name !== sessionStorage.getItem("lobby_on_top"))
				&& (lobby.name !== sessionStorage.getItem("lobby_created"))) {
			var id = lobby.id;
			var name = lobby.name;
			var owner = lobby.owner;
			var guest = lobby.guest;
			var username = user;
			// console.log("LOAD MORE
			// ind:"+i+"|id:"+id+"|name:"+name+"|owner:"+owner+"|guest:"+guest+"|username:"+username);
			var newLobby = buildLobbyRow(id, name, owner, guest, username);
			list.append(htmlToElement(newLobby));
		} else {// yet visible on top
			console.log("Load More yet visible:" + lobby.name);
		}
	}
	lastIndex = i;
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
				+ "\" type=\"button\" onclick=\"startGame()\" class=\"btn btn-warning btn-lg float-right\">Start</button>";
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
/*
 * function refreshDivByID(resultData, id_div) { ) { var r =
 * JSON.parse(resultData); if (r.error) { alert("ERROR: " + r.err_msg); } else {
 * simpleRefreshByID(id_div); } }
 * 
 * function simpleRefreshByID(id_div){ //alert("SIMPLE refresh: "+id_div);
 * console.log("REFRESH LOBBIES LIST"); $("#" + id_div).load(location.href + " #" +
 * id_div + ">*", ""); }
 */