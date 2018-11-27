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
				<style type="text/css">
					#container {
						margin: 20px;
						width: 200px;
						height: 100px;
					}
				</style>
				<!-- <script type="text/javascript">
	function getEventsFromServer() {
		$.ajax({
			url : "get_progress",
			type : "post",
			data : ({
				"gameId" : "1"
			}),
			success : function(result) {
				alert(result);

				$("#dynamic").css("width", result + "%").attr("aria-valuenow",
						result).text(result + "% Complete");
				getEventsFromServer();
			},
			error : function(e) {
				alert(e.responseText);
				setTimeout(function() {
					getEventsFromServer();
				}, 5000);
			}
		});
	}
	$(document).ready(function() {
		getEventsFromServer();
		/*var progress=10.0;
		var current_progress = 0;
		if($("#difficulty").val()==="cinque"){
			progress=100/25;
		}*/

		/*$(function() {
			var current_progress = 0;
			var interval = setInterval(function() {
				current_progress += progress;
				$("#dynamic").css("width", current_progress + "%")
						.attr("aria-valuenow", current_progress).text(
								current_progress + "% Complete");
				if (current_progress >= 100)
					clearInterval(interval);
			}, 1000);
		});*/

	});
</script> -->
		</head>

		<body class="wsmenucontainer">
			<%@include file="includes/navbar.jsp"%>
				<div class="no_margin">
					<div class="row justify-content-md-end cover_all no_margin">
						<div id="view" class="row justify-content-center align-items-center col-md-9 col-sm-12 col-xs-12">
							<div class="row justify-content-center col-md-12">
								<div class="board col-md-auto">
									<input id="difficulty" class="hidden-field" value="${randomGrid.difficulty}" />
									<div id="initial_location" class="myGrid ${randomGrid.difficulty}">
										<c:forEach items="${randomGrid.nameImages}" var="piece">
											<span class="box_piece" ondrop="drop(event)" ondragover="allowDrop(event)">
												<img draggable="true" ondragstart="drag(event)" id="${piece}" class="piece" src="resources/images/puzzles/${randomGrid.subjectName}/${randomGrid.difficulty}/${piece }.png"
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
							<div class="row justify-content-center col-md-12">
								<button type="button" class="btn btn-warning btn-lg button-in-row" data-toggle="modal" data-target="#show-modal">Show Image</button>
								<div class="modal fade " id="show-modal">
									<div class="modal-dialog">
										<div class="modal-content card-with-shadow">
											<div class="modal-header">
												<h4 class="modal-title">image to compose</h4>
												<button type="button" class="close" data-dismiss="modal">&times;</button>
											</div>
											<div class="modal-header">
												<img alt="imageToCompse" src="resources/images/puzzles/${randomGrid.subjectName}/${randomGrid.subjectName}.jpg" width="100%"
												    height="auto">
											</div>
											<div class="modal-footer">
												<button type="button" data-dismiss="modal" class="btn btn-warning btn-sm">Close</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row minimal-row col-12 col-md-3 col-sm-12 col-xs-12 game_info card-with-shadow">
							<div class="row minimal-row fit-row">
								<label for="progress_name_text" style="color: white;">Opponent's progress</label>
								<div id="dynamic">
									<!-- PROGRESS BAR -->
								</div>
								<div class="row">
									<!-- CHAT -->
									<div class="chat">
										<div class="row chat-list scrollbar-ripe-malinka" id="chat_content"></div>
										<div class="row input-chat ">
											<input type="text" class="form-control col-9 card-with-shadow" id="message_text" placeholder="Message to send">
											<button type="button" onclick="sendMessage()" class="btn btn-warning button-send btn-sm col-2">
												<img src="resources/images/icons/send.png" class="piece" width="-webkit-fill-available" height="-webkit-fill-available" />
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
		</body>

		</html>