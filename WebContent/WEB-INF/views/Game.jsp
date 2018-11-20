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
</head>
<body class="wsmenucontainer">
	<%@include file="includes/navbar.jsp"%>
	<div class="no_margin">
		<div class="row justify-content-md-end cover_all no_margin">
		<div id="view" class="row justify-content-center align-items-center col-md-10 col-sm-12 col-xs-12">
			<div  class="row justify-content-center col-md-12">
			<div class="board col-md-auto">
				<div id="initial_location" class="myGrid ${randomGrid.difficulty}">
					<c:forEach items="${randomGrid.nameImages}" var="piece">
						<span class="box_piece" ondrop="drop(event)"
							ondragover="allowDrop(event)"><img draggable="true"
							ondragstart="drag(event)" id="${piece}" class="piece"
							src="resources/images/${randomGrid.subjectName}/${randomGrid.difficulty}/${piece }.png " /></span>
					</c:forEach>
				</div>
			</div>
			<div class="board col-md-auto offset-0 offset-md-1">
				<div id="to_complete" class="myGrid cinque">
					<c:forEach items="${randomGrid.nameImages}" var="piece">
						<span class="box_piece" ondrop="drop(event)"
							ondragover="allowDrop(event)"></span>
					</c:forEach>
				</div>
			</div>
			</div>
		</div>
		
		
		<div class="row col-12 col-md-2 col-sm-12 col-xs-12 game_info pr-0">
				<div class="row">
		        <div class="col-md-3 col-sm-6">
		            <div class="progress pink">
		                <span class="progress-left">
		                    <span class="progress-bar"></span>
		                </span>
		                <span class="progress-right">
		                    <span class="progress-bar"></span>
		                </span>
		                <div class="progress-value">90%</div>
		            </div>
		        </div>
		    </div>
		</div>
		</div>
	</div>
</body>
</html>