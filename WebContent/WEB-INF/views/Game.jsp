<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<!DOCTYPE html>
		<html>

		<head>
			<meta charset="ISO-8859-1">
			<%@include file="includes/includes.jsp"%>
				<script src="resources/js/Game.js"></script>
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
						<div id="view" class="row justify-content-center align-items-center col-md-10 col-sm-12 col-xs-12">
							<div class="row justify-content-center col-md-12">
								<div class="board col-md-auto">
									<input id="difficulty" class="hidden-field" value="${randomGrid.difficulty}" />
									<div id="initial_location" class="myGrid ${randomGrid.difficulty}">
										<c:forEach items="${randomGrid.nameImages}" var="piece">
											<span class="box_piece" ondrop="drop(event)" ondragover="allowDrop(event)">
												<img draggable="true" ondragstart="drag(event)" id="${piece}" class="piece" src="resources/images/${randomGrid.subjectName}/${randomGrid.difficulty}/${piece }.png "
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
						</div>
						<div class="row minimal-row col-12 col-md-2 col-sm-12 col-xs-12 game_info card-with-shadow">
							<div class="row minimal-row fit-row">
								<div id="dynamic" class="progress-bar progress-bar-success progress-bar-striped active" role="progressbar" aria-valuenow="0"
								    aria-valuemin="0" aria-valuemax="100" style="width: 0%">
									<span id="current-progress"></span>
								</div>
							</div>
						</div>
					</div>
				</div>
		</body>

		</html>