<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!DOCTYPE html>
		<html>
			<head>
				<meta charset="ISO-8859-1">
					<%@include file="includes/includes.jsp"%>
						<link rel="stylesheet" type="text/css" media="all" href="resources/css/end_game.css"/>
						<title>Game End</title>
					</head>
					<body class="wsmenucontainer">
						<%@include file="includes/navbar.jsp"%>
							<div class="panel panel-default col-sm-6 centered">
								<div class='panel panel-heading text-center'>Lobby Name</div>
								<div class='panel-body'>
									<ul class="list-group">
										<li class="list-group-item text-center">Game Finished</li>
										<c:set var="win" value="${false}"/>
										<c:choose>
											<c:when test="${win}">
												<li class="list-group-item text-center">Hai Vinto</li>
											</c:when>
											<c:otherwise>
												<li class="list-group-item text-center">Hai perso</li>
											</c:otherwise>
										</c:choose>
									</ul>
									<div class="row">
										<div class='col-sm-6 text-center'>
											<ul class="list-group">
												<li class="list-group-item">
													<img class='team-icon' src='http://fakeimg.pl/100x100/?text=Team 1&font=lobster' width='64' height='64'></li>
													<li class="list-group-item">Player 1</li>
													<li class="list-group-item">Tempo impiegato: 1</li>
												</ul>
											</div>
											<div class='col-sm-6 text-center'>
												<ul class="list-group">
													<li class="list-group-item">
														<img class='team-icon' src='http://fakeimg.pl/100x100/?text=Team 2&font=lobster' width='64' height='64'></li>
														<li class="list-group-item">Player 2</li>
														<li class="list-group-item">Tempo impiegato: 2</li>
													</ul>
												</div>
											</div>
										</div>
									</div>
								</body>
							</html>
