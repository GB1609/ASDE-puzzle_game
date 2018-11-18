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
	<div id="view" class="row justify-content-center">
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
		<div class="board col-md-auto offset-md-1">
			<div id="to_complete" class="myGrid cinque">
				<c:forEach items="${randomGrid.nameImages}" var="piece">
					<span class="box_piece" ondrop="drop(event)"
						ondragover="allowDrop(event)"></span>
				</c:forEach>
			</div>
		</div>
	</div>

</body>
</html>