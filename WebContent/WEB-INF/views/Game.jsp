<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="ISO-8859-1">
			<%@include file="includes/includes.jsp"%>
				<script src="resources/js/Game.js"></script>
				<script src="https://rawgit.com/kimmobrunfeldt/progressbar.js/1.0.0/dist/progressbar.js"></script>
				<link rel="stylesheet" type="text/css" media="all" href="resources/css/style_game.css" />
				<title>Game Page</title>
		</head>

		<body class="wsmenucontainer">
			<%@include file="includes/navbar.jsp"%>

				<div class="row col-12">
					<div class="col-lg-9 col-md-12 col-sm-12 col-12">
						<div class="row minimal-row">
							<div class="board col-md-auto">
								<input id="difficulty" class="hidden-field" value="${randomGrid.difficulty}" />
								<div id="initial_location" class="myGrid ${randomGrid.difficulty}">
									<c:forEach items="${randomGrid.nameImages}" var="piece">
										<span class="box_piece" ondrop="drop(event)" ondragover="allowDrop(event)">
											<img draggable="true" ondragstart="drag(event)" id="${piece}" class="piece" src="resources/images/puzzles/${randomGrid.subjectName}/${randomGrid.difficulty}/${piece}.png"
											/>
										</span>
									</c:forEach>
								</div>
							</div>
							<div class="board col-md-auto offset-0 offset-md-1">
								<div id="to_complete" class="myGrid cinque">
									<c:forEach items="${randomGrid.nameImages}" var="piece">
										<span class="box_piece" ondrop="drop(event)" ondragover="allowDrop(event)"></span>
									</c:forEach>
								</div>
							</div>
						</div>
						<div class="row information-game">
							<div class="col-6 col-md-6 col-lg-6 col-sm-6 col-12">
								<div class="center-label">
									<button id="show-image-button" type="button" class="btn btn-warning" onclick="allowHint()" data-target="#show-modal">
										<div id="numHint"></div>
									</button>
									<div class="modal fade " id="show-modal">
										<div class="modal-dialog">
											<div class="modal-content card-with-shadow">
												<div class="modal-header">
													<h4 class="modal-title">image to compose</h4>
													<button type="button" class="close" data-dismiss="modal">&times;</button>
												</div>
												<div class="modal-header">
													<img alt="imageToCompose" src="resources/images/puzzles/${randomGrid.subjectName}/${randomGrid.subjectName}.jpg" width="100%"
													    height="auto">
												</div>
												<div class="modal-footer">
													<div id="numHint"></div>
													<button type="button" data-dismiss="modal" class="btn btn-warning btn-sm">Close</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-6 col-md-6 col-lg-6 col-sm-6 col-12">
								<label id="time" class="center-label">00:00:00</label>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-md-12 col-sm-12 col-12">
						<div class="row minimal-row card-with-shadow">
							<div class="col-lg-12 col-md-6 col-sm-6 col-12">
								<label for="progress_name_text" class="centreLabel">Opponent's progress
								</label>
								<div id="dynamic">
								</div>
							</div>
							<div class="col-lg-12 col-md-6 col-sm-6 col-12">
								<div class="chat">
									<div class="row force-max-heigth">
										<div class="row chat-list scrollbar scrollbar-primary">
											<div class=" force-overflow">
												<ul class="list-group" id="chat_content">

												</ul>
											</div>
										</div>
									</div>
									<div class="row input-chat">
										<input type="text" class="form-control col-lg-9 col-md-9 col-sm-9 col-9" id="message_text" placeholder="Message to send">
										<button type="button" onclick="sendMessage()" class="form-control btn btn-warning button-send  col-lg-3 col-md-3 col-sm-3 col-3">
											<span class="fas fa-comment"></span>
										</button>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
		</body>

		</html>