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
	<div class="row">
		<div id="lobbies_div" class="col-sm-6">
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
						<li class="list-group-item" id="lobby_row">
							<div class="text-center" id="lobby_name_div">${lobby.name}</div>
							<div class=" text-center">
								<img src="resources/images/avatar.svg" class="img-circle"
									height="15" width="15" alt="Avatar">
								<c:choose>
									<c:when test="${not empty lobby.owner}">${lobby.owner}</c:when>
									<c:otherwise>empty</c:otherwise>
								</c:choose>
								VS <img src="resources/images/avatar.svg" class="img-circle"
									height="15" width="15" alt="Avatar">
								<c:choose>
									<c:when test="${not empty lobby.guest}">${lobby.guest}</c:when>
									<c:otherwise>empty</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${sessionScope.username eq lobby.owner}">
										<button type="button" onclick="startGame()"
											class="btn btn-primary">Start</button>
									</c:when>
									<c:otherwise>
										<button type="button" onclick="joinLobby()"
											class="btn btn-primary">Join</button>
									</c:otherwise>
								</c:choose>

							</div>
						</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<div class="dropdown col-sm-6">
			<button type="button" class="btn btn-primary dropdown-toggle"
				data-toggle="dropdown">Search Lobby</button>
			<div class="dropdown-menu">
				<form action="search_lobby_by_name">
					<div class="form-group row">
						<input type="text" class="form-control" id="id_lobby_name"
							name="lobby_name" placeholder="Lobby's name">
						<button type="submit" class="btn btn-primary">by Name</button>
					</div>
				</form>
				<form action="search_lobby_by_username">
					<div class="form-group row">
						<input type="text" class="form-control" id="id_user_name"
							name="user_name" placeholder="Username">
						<button type="submit" class="btn btn-primary">by Username</button>
					</div>
				</form>
				<c:if test="${not empty error_lobby_search}">
					<div class="alert alert-danger alert-dismissible" role="alert">
						<button type="button" class="close" data-dismiss="alert"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<strong>${error_lobby_search}</strong>
					</div>
				</c:if>
			</div>
			<div class="create-btn">
				<a href="#" class="btn btn-warning btn-lg"
					style="padding: 15px; margin: 14px;" data-toggle="modal"
					data-target="#create-modal">Create</a>
			</div>
			<div class="modal fade" id="create-modal" tabindex="-1" role="dialog"
				aria-labelledby="myModalLabel" aria-hidden="true"
				style="display: none;">
				<div class="modal-dialog">
					<div class="create_modal-container">
						<h1>Create your lobby</h1>
						<br>
						<form action="create_lobby">
							<div class="form-group">
								<label for="lobby_name_text" style="color:white;">Lobby's name:</label> <input
									type="text" class="form-control" id="id_lobby_name"
									name="lobby_name">
							</div>
							<button type="submit" class="btn btn-primary">Create</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>
