<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="includes/includes.jsp"%>
<script src="resources/js/Lobby.js"></script>
<title>Insert title here</title>
<style type="text/css">
#lobbies_div {
	border: none;
	padding: 5px;
	font: 24px/36px sans-serif;
	height: 500px;
	overflow: scroll;
}

a {
	color: inherit;
	text-decoration: inherit;
}
</style>
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
									<c:when test="${not empty lobby.owner}">
					  		${lobby.owner}
						  </c:when>
									<c:otherwise>
						  	empty
						  </c:otherwise>
								</c:choose>
								VS <img src="resources/images/avatar.svg" class="img-circle"
									height="15" width="15" alt="Avatar">
								<c:choose>
									<c:when test="${not empty lobby.guest}">
					  		${lobby.guest}
						  </c:when>
									<c:otherwise>
						  	empty
						  </c:otherwise>
								</c:choose>
								<button type="button" onclick="joinLobby()"
									class="btn btn-primary">Join</button>
								<button type="button" onclick="startGame()"
									class="btn btn-primary">Start</button>
							</div>
						</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
		<div class="dropdown col-sm-6">
			<button type="button" class="btn btn-primary dropdown-toggle"
				data-toggle="dropdown">Create Lobby</button>
			<div class="dropdown-menu">
				<form action="create_lobby">
					<div class="form-group">
						<label for="lobby_name_text">Lobby's name:</label> <input
							type="text" class="form-control" id="id_lobby_name"
							name="lobby_name">
					</div>
					<button type="submit" class="btn btn-primary">Create</button>
				</form>
			</div>
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
	</div>
</body>
</html>