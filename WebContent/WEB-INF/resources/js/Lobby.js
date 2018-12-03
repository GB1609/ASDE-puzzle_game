var listElm;
var user;
var currentlyShowed;
var id_lobby = "id_lobby_";
var avatars_path = "resources/images/avatars/";
var lobbies_saved = "[]";
var lobbies_owner = "[]";
var lobbies_guest = "[]";
var avatars = "[]";
var grid = false;


$(document)
	.ready(
		function () {
			listElm = document.querySelector('#lobbies_div');
			listElm
				.addEventListener(
					'scroll',
					function () {
						if (listElm.scrollTop +
							listElm.clientHeight >= (listElm.scrollHeight - 1)) {
							getLobbies(false, false);
						}
					});
			checkIfListening();
			currentlyShowed = 0;
			getLobbies(true, true);
		});


function getLobbies(reset, automatic_refresh) {
	if (reset) {
		currentlyShowed = 0;
		lobbies_saved = "[]";
	}
	console.log("currentlyShowed : " + currentlyShowed);
	console.log("lobbies_saved : " + JSON.stringify(lobbies_saved));
	$.ajax({
		url: "get_lobbies",
		type: "get",
		data: ({
			"lobbies": JSON.stringify(lobbies_saved),
			"currently_showed": currentlyShowed
		}),
		success: function (resultData) {
			console.log("refresh ok: " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				console.log("ERROR: " + r.err_msg);
			} else {
				user = r.username;
				avatars = r.avatars;
				if (Object.keys(r.lobbies_to_add).length) {
					currentlyShowed += Object.keys(r.lobbies_to_add).length;
					//      alert(currentlyShowed+ "r.lobbies_to_add:"+JSON.stringify(r.lobbies_to_add));
				}
				if (r.lobbies_changed) {
					lobbies_saved = r.lobbies;
					//     alert("CHANGED");
					lobbies_owner = r.lobbies_owner;
					lobbies_guest = r.lobbies_guest;
					reloadList(true, lobbies_saved, lobbies_guest,
						lobbies_owner, avatars);
				} else {
					if (!automatic_refresh) {
						reloadList(false, r.lobbies_to_add, lobbies_guest,
							lobbies_owner, avatars);
						// automatic_refresh = false;
					}
				}
				if (automatic_refresh) {
					setTimeout(function () {
						getLobbies(false, automatic_refresh);
					}, 5000);
				}
			}
		},
		error: function (e) {
			console.log(e.responseText);
			console.log("REFRESH ERROR: ", e);
			if (automatic_refresh) {
				setTimeout(function () {
					getLobbies(false, automatic_refresh);
				}, 10000);
			}
		}
	});
}


function checkIfListening() {
	$.ajax({
		url: "check_is_listening_for",
		type: "post",
		data: ({}),
		success: function (result) {
			if ($.trim(result)) {
				console.log(result);
				var r = JSON.parse(result);
				if (r.start) {
					listenForStartGame(r.lobby_name);
				} else if (r.join) {
					listenForJoinToLobby(r.lobby_name, false, false);
				}
			}
		},
		error: function (e) {
			console.log(e.responseText);
		}
	});
}


function toggleModal() {
	$("#alert-modal").modal("toggle");
}

function listenForStartGame(lobby_name) {
	console.log("listenStartfor" + lobby_name);
	var xhr = $.ajax({
		url: "check_start",
		type: "post",
		data: ({
			"lobby_name": lobby_name
		}),
		success: function (result) {
			if (!$.trim(result))
				listenForStartGame(lobby_name);
			else {
				console.log(result);
				var r = JSON.parse(result);
				if (r.start) {
					window.location.href = "/ASDE-puzzle_game/joiner_to_game";
				} else if (r.leave) {
					console.log("in Leave in listen for start game" + r.joiner +
						"user->" + $("#user").val());
					if (r.joiner === $("#user").val())
						return;
					toggleModal();
					// swal("info","The previous owner leave the lobby, you are the new owner!!!","info");
					getLobbies(true, false);
					listenForJoinToLobby(lobby_name, false);
					console.log("Lobby destruct"); // TODO make alert
				}
			}
		},
		error: function (e) {
			console.log(e.responseText);
			setTimeout(function () {
				listenForStartGame(lobby_name);
			}, 5000);
		}
	});

	console.log(xhr);

}

function listenForJoinToLobby(lobby_name, showAlertLeave, showAlertJoin) {
	// console.log("in join")
	console.log("listenForJoinToLobby" + lobby_name);

	var xhr = $
			.ajax({
				url : "check_join",
				type : "post",
				data : ({
					"lobby_name" : lobby_name
				}),
				success : function(result) {
					if ($.trim(result)) {
						var r = JSON.parse(result);
						if (r.join) {
							console.log("listenForJoinToLobby: r.join"
									+ r.joiner);
							if (r.joiner === $("#user").val())
								return;
							$("#start_button").removeClass("hidden-field");
							$("#empty_slot").text(r.joiner);
							if (!showAlertJoin) {
								console
										.log("listenForJoinToLobby: r.join > show alert");
								$('#join_alert').fadeIn('slow', function() {
									$('#join_alert').delay(5000).fadeOut();
								});
								showAlertJoin = true
							} else {
								showAlertJoin = false;
							}
						} else if (r.leave) {
							if (r.owner != null) {
								console.log("listenForJoinToLobby: r.leave ow:"
										+ r.owner);
								if (r.owner === $("#user").val()) {
									console
											.log("listenForJoinToLobby: r.leave user");
									getLobbies(true,false);
									return;
								}
							} else {
								console
										.log("listenForJoinToLobby: r.joiner jo:"
												+ r.joiner);
								if (!showAlertLeave
										&& !(r.joiner === $("#user").val())) {
									$("#start_button").addClass("hidden-field");
									$('#leave_alert').fadeIn(
											'slow',
											function() {
												$('#leave_alert').delay(5000)
														.fadeOut();
											});
									showAlertLeave = true;
								} else {
									showAlertLeave = false;
								}
							}
						}
					}
					listenForJoinToLobby(lobby_name, showAlertLeave,
							showAlertJoin);

				},
				error : function(e) {
					console.log(e.responseText);
					setTimeout(function() {
						listenForJoinToLobby(lobby_name, showAlertLeave,
								showAlertJoin);
					}, 5000);
				}
			});
	console.log(xhr);

}

function joinLobby(lobby_name) {
	// var lobby_name = $("#" + id_lobby).children('#lobby_name_div').text();
	// console.log("in join lobby");
	$.ajax({
		url : "join_lobby",
		type : "POST",
		data : ({
			"lobby_name" : lobby_name
		}),
		success : function(resultData) {
			// console.log("join ok"); // : " + resultData);

			var r = JSON.parse(resultData);
			if (r.error) {
				console.log("ERROR: " + r.err_msg);
			} else {
				getLobbies(true,false);
				listenForStartGame(lobby_name);
			}
		},
		error : function(e) {
			console.log(e.responseText);
			console.log("JOIN ERROR: ", e);
		}
	});
}

function startGame() {
	$("#ftg_form").submit();
}

function leaveLobby(lobby_name) {
	// var lobby_name = $("#" + id_lobby).children('#lobby_name_div').text();
	console.log("in leave lobby");
	$.ajax({
		url: "leave_lobby",
		type: "POST",
		data: ({
			"lobby_name": lobby_name
		}),
		success: function (resultData) {
			console.log("leave ok : " + resultData);

			var r = JSON.parse(resultData);
			if (r.error) {
				console.log("ERROR: " + r.error);
			} else {
				getLobbies(true, false);
				swal("Lobby leaved", "You leaved the lobby", "info");
			}
		},
		error: function (e) {
			console.log(e.responseText);
			console.log("LEAVE ERROR: ", e);
		}
	});
	// ev.preventDefault();
}



function createLobby() {
	var lobby_name = $('#id_lobby_name').val();
	$.ajax({
		url: "create_lobby",
		type: "POST",
		data: ({
			"lobby_name": lobby_name
		}),
		success: function (resultData) {
			console.log("lobby create ok"); // : " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				console.log("ERROR: " + r.err_msg);
			} else {
				getLobbies(true, false);
				$('#create-modal').modal("toggle");
				listenForJoinToLobby(lobby_name, false, false);
				swal("CREATED", "lobby created", "info");
			}
		},
		error: function (e) {
			console.log(e.responseText);
			console.log("LOBBY CREATE ERROR: ", e);
		}
	});
}

function searchLobby(ev, searchBy) {
	var name = $('#id_search_txt').val();
	ev.preventDefault();
	$.ajax({
		url: "search_lobby",
		type: "POST",
		data: ({
			"search_txt": name,
			"search_by": searchBy
		}),
		success: function (resultData) {
			console.log("lobby search ok"); // : " + resultData);
			var r = JSON.parse(resultData);
			if (r.error) {
				console.log("ERROR: " + r.err_msg);
			} else {
				var jsonArray = [r.lobby_searched];
				putLobbyOnTop(jsonArray);
			}
		},
		error: function (e) {
			console.log(e.responseText);
			console.log("LOBBY SEARCH ERROR: ", e);
		}
	});
}

var reloadList = function (reset, lobbies, lobbies_guest, lobbies_owner, avatars) {
	if (reset) {
		clearLobbiesList();
	}
	loadMore(lobbies, lobbies_guest, lobbies_owner, avatars);
}

var clearLobbiesList = function () {
	$('#id_lobbies_list_ul').children()
		.not(document.getElementById("template")).remove();
}

function getAvatar(username, avatars) {
	// alert("Username:"+username);
	for (var i in avatars) {
		var obj = avatars[i];
		if (obj.user === username) {
			// alert("Obj.name:"+obj.user+"__avatar:"+obj.avatar);
			return obj.avatar;
		}
	}
	return "";
}

function addLobbiesToList(list, mode, lobbies, avatars) {
	for (var i in lobbies) {
		var lobby = lobbies[i];
		var id = lobby.id;
		var name = lobby.name;
		var owner = lobby.owner;
		var guest = lobby.guest;
		var avatarOwner = getAvatar(owner, avatars);
		var avatarGuest = getAvatar(guest, avatars);
		var newLobby = buildLobbyRow(id, name, owner, guest, user,
			avatarOwner, avatarGuest);
		if (mode === 'append') {
			if (conteinedIn(lobby, lobbies_guest) === false &&
				conteinedIn(lobby, lobbies_owner) === false) {
				list.append(newLobby[0]);
			}
		} else if (mode === 'prepend') {
			if ($("#id_lobby_" + name) !== undefined) {
				$("#id_lobby_" + name).remove();
				console.log("loadputLobbyOnTop...removed:" + name);
			}
			console.log("loadputLobbyOnTop:" + name);
			list.prepend(newLobby[0]);
		}
	}
}
var loadMore = function (lobbies, lobbies_guest, lobbies_owner, avatars) {
	var list = document.getElementById("id_lobbies_list_ul");
	console.log("ENTERED ON loadMore"); // :"+lobbies);
	addLobbiesToList(list, 'append', lobbies, avatars);
	addLobbiesToList(list, 'prepend', lobbies_guest, avatars);
	addLobbiesToList(list, 'prepend', lobbies_owner, avatars);

}

function conteinedIn(lobby, list) {
	for (var i in list) {
		var tmp = list[i];
		if (lobby.name === tmp.name) {
			console.log("conteinedIn lobbies[i]:" + tmp.name);
			return true;
		}
	}
	return false;
}

function buildLobbyRow(id, name, owner, guest, username, avatarOwner,
	avatarGuest) {
	// name = "LOBBY_NAME";
	// owner = "Ciccio";
	// guest = "a";
	// username = "a";
	// avatarOwner = "avatar_1.png";
	// avatarGuest = "avatar_10.png";

	// alert("owner:"+owner+", guest:"+guest);
	// alert(id+"-"+name+"-"+owner+"-"+guest+"-"+username+"-"+avatarOwner+"-"+avatarGuest);
	if (owner != undefined || guest != undefined) {
		var template_copy = $('#template').clone();
		template_copy.attr('id', id_lobby + name);
		var div = template_copy.children('#lobby_name_div');
		div.text(name);
		div = template_copy.children('#id_lobby_div');
		if (owner != undefined) {
			if (owner != "") {
				div.children('#id_img_owner').attr('src',
					avatars_path + avatarOwner);
				div.children('#id_owner_name').text(owner);
			} else {
				div.children('#id_img_owner').attr('src',
					avatars_path + "avatar.svg");
				div.children('#id_owner_name').text("EMPTY");
			}
		} else {
			// alert(" ELSE ");
			div.children('#id_img_owner').attr('src',
				avatars_path + "avatar.svg");
			div.children('#id_owner_name').text("EMPTY");
		}
		if (guest != undefined) {
			if (guest != "") {
				div.children('#id_img_guest').attr('src',
					avatars_path + avatarGuest);
				div.children('#id_guest_name').text(guest);
			} else {
				div.children('#id_img_guest').attr('src',
					avatars_path + "avatar.svg");
				if (owner !== username) {
					div.children('#id_guest_name').text("EMPTY");
				} else {
					div.children('#id_guest_name').attr('id', 'empty_slot').text("EMPTY");
				}
			}
		} else {
			// alert(" ELSE ");
			div.children('#id_img_guest').attr('src',
				avatars_path + "avatar.svg");
			div.children('#id_guest_name').text("EMPTY");
		}
		if (username != owner) {
			if (guest === "") {
				var join_btn = div.children('#join_btn')
				join_btn.removeClass('hidden-field');
				join_btn.click(function () {
					joinLobby(name);
				});
			}
		} else {
			div
				.children('#id_lobby_div')
				.append(
					'<input id="created_lobby" type="hidden" value="created" />');
			if (guest !== "") {
				div.children('#start_button').removeClass('hidden-field');
			}
			div.children('#ftg_form').append(
				'<input type="hidden" id="lobby_name" name="lobby_name" value="' +
				name + '">');
		}
		if (owner === username || guest === username) {
			var leave_btn = div.children('#leave_btn');
			leave_btn.removeClass('hidden-field');
			leave_btn.click(function () {
				leaveLobby(name);
			});
		}
		// $('#id_lobbies_list_ul').append(template_copy);
	}
	template_copy.fadeIn('fast');
	return template_copy;
}