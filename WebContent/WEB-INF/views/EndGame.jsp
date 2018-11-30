<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="ISO-8859-1">
			<%@ include file="includes/includes.jsp" %>
				<link rel="stylesheet" type="text/css" media="all" href="resources/css/style_end_game.css" />
				<title>Game End</title>
		</head>

		<body class="wsmenucontainer">
			<%@ include file="includes/navbar.jsp" %>
				<div class="panel panel-default col-sm-6">
					<div class="panel panel-heading text-center">
						<strong>Lobby Name</strong>
					</div>
					<div class="panel-body">
						<ul class="list-group">
							<li class="list-group-item text-center card-with-shadow">Game Finished</li>
							<c:set var="win" value="${false}" />
							<c:choose>
								<c:when test="${username == match.winner.username }">
									<li class="list-group-item text-center card-with-shadow " style="background:#004d00;">Congrats, you win!</li>
								</c:when>
								<c:otherwise>
									<li class="list-group-item text-center card-with-shadow" style="background:#B90000;">Sorry, you are very stupid. You lose.</li>
								</c:otherwise>
							</c:choose>
						</ul>
						<div class="row">
						<c:forEach items="${match.users }" var="player">
							<div class="col-sm-6 text-center">
								<ul class="list-group">
									<li class="list-group-item card-with-shadow">
										<img class="team-icon" src="resource/images/avatars/${player.avatar }" width="64" height="64">
									</li>
									<li class="list-group-item card-with-shadow">${player.username }</li>
									<li class="list-group-item card-with-shadow">${match.time}</li>
								</ul>
							</div>
							</c:forEach>
							<!--  <div class="col-sm-6 text-center">
								<ul class="list-group">
									<li class="list-group-item card-with-shadow">
										<img class="team-icon" src="http://fakeimg.pl/100x100/?text=Team 2&font=lobster" width="64" height="64">
									</li>
									<li class="list-group-item card-with-shadow">Player 2</li>
									<li class="list-group-item card-with-shadow">Tempo impiegato: 2</li>
								</ul>
							</div>-->
						</div>
					</div>
				</div>
		</body>

		</html>