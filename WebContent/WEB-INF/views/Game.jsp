<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="includes/includes.jsp"%>

<!-- Latest compiled and minified CSS 
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">

<!-- jQuery library->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Popper JS ->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript ->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<!-- <link href="resources/style.css" rel="stylesheet">
<script type="text/javascript" src="resources/game.js"></script> -->
<title>Game Page</title>
<script type="text/javascript">
	$(document).ready(function() {
		//$('#base').addClass($('#base').attr('value'));
	});
</script>
<script>
	function allowDrop(ev) {
		if (!ev.target.hasChildNodes()
				&& ev.target.getAttribute("class") == "box_piece") {
			ev.preventDefault();
		}
	}

	function drag(ev) {
		ev.dataTransfer.setData("pieceMoved", ev.target.id);
		ev.dataTransfer.setData("old", ev.target.parentElement.parentElement.id);
		


	}

	function drop(ev) {
		ev.preventDefault();
		var data = ev.dataTransfer.getData("pieceMoved");
		var old=ev.dataTransfer.getData("old");
		ev.target.appendChild(document.getElementById(data));
		
		$.ajax({
			url: "move_piece",
	     	type: "POST",
			data: ({
				"new_location": ev.target.parentElement.getAttribute("id"),
				"old_location": old,
				"piece": data,
				"new_position": $(ev.target).prevAll(".box_piece").length
	           }),
			success: function(resultData){
						console.log("ok"+resultData);
			          },
		    error : function(e) {
					 	  alert(
								"new_location   "+ev.target.parentElement.getAttribute("id")+"\n"+
								"old_location   "+ old+"\n"+
								"piece  "+ data+"\n"+
								"new_position   "+$(ev.target).prevAll(".box_piece").length
					         );
					       alert(e.responseText);
					
					console.log("ERROR: ", e);
					}
				});		

	}
</script>
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