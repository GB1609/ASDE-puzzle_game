<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<html>

		<head>
			<meta charset="ISO-8859-1">
			<%@include file="includes/includes.jsp"%>
				<script src="resources/js/Lobby.js"></script>
				<title>Lobby Page</title>
				<link rel="stylesheet" type="text/css" media="all" href="resources/css/style_lobby.css" />
		</head>

		<body class="wsmenucontainer">
			<%@include file="includes/navbar.jsp"%>
				<div>
					<input id="user" type="hidden" value="${username }" />
					<div class="row center-content-row">
						<div class="col-md-auto col-sm-2 col-auto">
							<button type="button" class="btn btn-warning button-in-row" onclick="getLobbies('true')">Refresh</button>
						</div>
						<div class="col-md-auto col-sm-2 col-auto">
							<button type="button" class="btn btn-warning button-in-row" data-toggle="modal" data-target="#create-modal">Create</button>
							<div class="modal fade" id="create-modal">
								<div class="modal-dialog">
									<div class="modal-content">
										<div class="modal-header">
											<h4 class="modal-title">Create your lobby</h4>
											<button type="button" class="close" data-dismiss="modal">&times;</button>
										</div>
										<form id="id_create_lobby_form">
											<div class="modal-header center-content-row">
												<div class="form-group">
													<label for="lobby_name_text" style="color: white;">Lobby's name: </label>
													<input type="text" class="form-control" id="id_lobby_name" onkeypress="createLobbyEnter(event)">
													<br>
												</div>
											</div>
											<div class="modal-footer center-content-row">
												<button type="button" onclick="createLobby()" class="btn btn-warning btn-sm">Create</button>
											</div>
										</form>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-auto col-sm-2 col-auto">
							<div class="dropdown">
								<button type="button" class="btn btn-warning dropdown-toggle button-in-row" aria-haspopup="true" data-toggle="dropdown">Search</button>
								<div class="dropdown-menu dropdown-content">
									<form>
										<div class="form-group">
											<input type="text" class="form-control" id="id_search_txt" name="search_txt" required="required" placeholder="Insert lobby's name or Username"
											    style="margin-bottom: inherit;">
											<div class="row row-search-lobby">
												<button type="submit" onclick="searchLobby(event,'LOBBY_NAME')" class="btn btn-warning btn-sm col" style="margin-right: 0.5rem">by Name</button>
												<button type="submit" onclick="searchLobby(event,'USERNAME')" class="btn btn-warning btn-sm col" style="margin-left: 0.5rem">by Username
												</button>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
					<div class="row center-content-row">
						<div class="row col-10 lobbyesBox">
							<div id="lobbies_div" class=" card scrollbar-ripe-malinka">
								<div class="card-body" id="id_lobbies_list_div">
									<ul class="list-group" id="id_lobbies_list_ul">
										<%@include file="template/lobby_item.jsp"%>
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- MODAL FOR LEAVE LOBBY -->
				<div class="modal fade" id="alert-modal">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4 class="modal-title">INFO</h4>
								<button type="button" class="close" data-dismiss="modal">&times;</button>
							</div>
							<div class="modal-header center-content-row" style="color:white;">
								The Lobby's owner leaved it!
								<br> Now you are the new owner
							</div>
							<div class="modal-footer center-content-row">
								<button type="button" onclick="toggleModal()" class="btn btn-warning btn-sm">OK</button>
							</div>
						</div>
					</div>
				</div>
				<!-- END MODAL FOR LEAVE LOBBY -->
		</body>

		</html>