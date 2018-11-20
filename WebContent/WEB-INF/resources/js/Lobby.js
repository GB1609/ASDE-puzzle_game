$(document).ready(function() {
  //$('#base').addClass($('#base').attr('value'));
});

function joinLobby(ev) {
  //ev.preventDefault();
  //ev.target.appendChild(document.getElementById(data));
  var lobby_name = $(document.activeElement).closest('#lobby_row').children('#lobby_name_div').text();
  console.log(lobby_name);
  $.ajax({
    url: "join_lobby",
    type: "POST",
    data: ({
      "lobby_name": lobby_name
    }),
    success: function(resultData) {
      console.log("join ok: " + resultData);
      $("#lobbies_div").load(location.href + " #lobbies_div>*", "");
    },
    error: function(e) {
      alert(e.responseText);
      console.log("JOIN ERROR: ", e);
    }
  });
}

function startGame(ev) {
  console.log("ENTRATO IN START GAME");
  $.ajax({
    url: "game",
    type: "GET",
    success: function(resultData) {
      console.log("Start game ok: " + resultData);
      deleteLobby(ev);
      window.location.href = "http://localhost:8080/ASDE-puzzle_game/game";
    },
    error: function(e) {
      alert(e.responseText);
      console.log("START GAME ERROR: ", e);
    }
  });
}

function deleteLobby(ev) {
  //ev.preventDefault();
  //ev.target.appendChild(document.getElementById(data));
  var lobby_name = $(document.activeElement).closest('#lobby_row').children('#lobby_name_div').text();
  console.log("ENTRATO IN START GAME lobby_name:" + lobby_name);
  $.ajax({
    url: "delete_lobby_by_name",
    type: "POST",
    data: ({
      "lobby_name": lobby_name
    }),
    success: function(resultData) {
      console.log("lobby DELETE ok: " + resultData);
      $("#lobbies_div").load(location.href + " #lobbies_div>*", "");
    },
    error: function(e) {
      alert(e.responseText);
      console.log("LOBBY DELETE ERROR: ", e);
    }
  });
}
