<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="includes/includes.jsp"%>
<script src="resources/js/Lobby.js"></script>
<title>Lobby Page</title>
<link rel="stylesheet" type="text/css" media="all"
	href="resources/css/style_lobby.css" />
</head>
<body class="wsmenucontainer">
	<%@include file="includes/navbar.jsp"%>
	<div align="center">
		<div class="row center-content-row">
			<div class="dropdown col-sm-6 row">
				<div class="create-btn col">
					<a href="#" class="btn btn-warning btn-lg button-in-row "
						data-toggle="modal" data-target="#create-modal">Create</a>
				</div>
				<div class="modal fade" id="create-modal" tabindex="-1"
					role="dialog" aria-hidden="true" style="display: none;">

					<div class="modal-dialog">
						<div class="create_modal-container">
							<h1>Create your lobby</h1>
							<br>
							<form id="id_create_lobby_form">
								<div class="form-group">
									<label for="lobby_name_text" style="color: white;">Lobby's
										name:</label> <input type="text" class="form-control"
										id="id_lobby_name"> <br>
									<button type="button" onclick="createLobby()"
										class="btn btn-warning btn-sm">Create</button>
								</div>
							</form>
						</div>
					</div>
				</div>
				<div class="dropdown col">
					<button type="button"
						class="btn btn-warning btn-lg dropdown-toggle button-in-row"
						data-toggle="dropdown">Search Lobby</button>
					<div class="dropdown-menu dropdown-content">
						<form>
							<div class="form-group">
								<input type="text" class="form-control" id="id_search_txt"
									name="search_txt" required="required"
									placeholder="Insert lobby's name or Username"
									style="margin-bottom: inherit;">
								<div class="row row-search-lobby">
									<button type="submit" onclick="searchLobby(event,'LOBBY_NAME')"
										class="btn btn-warning btn-sm col"
										style="margin-right: 0.5rem">by Name</button>
									<button type="submit" onclick="searchLobby(event,'USERNAME')"
										class="btn btn-warning btn-sm col" style="margin-left: 0.5rem">by
										Username</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="row center-content-row">
			<div class="row col-md-10 lobbyesBox">
				<div id="lobbies_div" class=" card scrollbar-ripe-malinka">
					<div class="card-body">
						<c:if test="${not empty error}">
							<div class="alert alert-danger alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<strong>${error}</strong>
							</div>
						</c:if>
						<c:if test="${not empty lobbies}">
							<ul class="list-group">
								<c:forEach items="${lobbies}" var="lobby">
									<li class="list-group-item card-with-shadow" id="lobby_row">
										<div class="text-center" id="lobby_name_div">${lobby.name}</div>
										<div class=" text-center">
											<img src="resources/images/avatar.svg" class="img-circle"
												height="64" width="64" alt="Avatar">
											<c:choose>
												<c:when test="${not empty lobby.owner}">${lobby.owner}</c:when>
												<c:otherwise>empty</c:otherwise>
											</c:choose>
											<img
												style="max-height: 1cm; max-width: 1cm; margin-left: 0%;"
												src="resources/img/vs.jpg">
											<c:choose>
												<c:when test="${not empty lobby.guest}">${lobby.guest}</c:when>
												<c:otherwise>empty</c:otherwise>
											</c:choose>
											<img src="resources/images/avatar.svg" class="img-circle"
												height="64" width="64" alt="Avatar">

											<c:choose>
												<c:when test="${sessionScope.username eq lobby.owner}">
													<button type="button" onclick="startGame()"
														class="btn btn-primary float-right">Start</button>
												</c:when>
												<c:otherwise>
													<button type="button" onclick="joinLobby()"
														class="btn btn-warning btn-lg float-right">Join</button>
												</c:otherwise>
											</c:choose>
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
