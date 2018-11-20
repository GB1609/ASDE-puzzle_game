<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
  <!DOCTYPE html>
  <html>
    <head>
      <%@include file="includes/includes.jsp"%>
        <meta charset="ISO-8859-1">
          <title>End Game</title>
        </head>
        <body>
          <%@include file="includes/navbar.jsp"%>
          <div class="end_game_board panel-default col-sm-6">
            <div class='end_game_board end_game_board-heading text-center'>Nome Lobby</div>
            <div class='end_game_board-body'>
              <ul class="list-group">
                <li class="list-group-item text-center">Game finished</li>
                <li class="list-group-item text-center">You Win</li>
              </ul>
              <div class='col-sm-6 text-center'>
                <ul class="list-group">
                  <li class="list-group-item">
                    <img class='player-avatar' src='http://fakeimg.pl/100x100/?text=Team 1&font=lobster' width='64' height='64'></li>
                    <li class="list-group-item">Player 1</li>
                    <li class="list-group-item">1</li>
                  </ul>
                </div>
                <div class='col-sm-6 text-center'>
                  <ul class="list-group">
                    <li class="list-group-item">
                      <img class='player-avatar' src='http://fakeimg.pl/100x100/?text=Team 2&font=lobster' width='64' height='64'></li>
                      <li class="list-group-item">Player 2</li>
                      <li class="list-group-item">3</li>
                    </ul>
                  </div>
                </div>
              </div>
            </body>
          </html>
