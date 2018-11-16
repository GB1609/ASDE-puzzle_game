<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">

<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
<!-- <link href="resources/style.css" rel="stylesheet">
<script type="text/javascript" src="resources/game.js"></script> -->
<title>Insert title here</title>
<style type="text/css">
#lobbies_div {
border: none;
padding: 5px;
font: 24px/36px sans-serif;
height: 600px;
overflow: scroll;
}


</style>
</head>
<body>
<div id="lobbies_div" class="col-sm-8">
	<c:if test="${not empty lobbies}">
		<c:forEach items="${lobbies}" var="lobby">
			<ul class="list-group">
			  <li class="list-group-item">
			  	<div class=" text-center">
			  		${lobby.name}
				</div>
				<div class=" text-center" >
					<img src="resources/images/avatar.svg" class="img-circle" height="15" width="15" alt="Avatar">
					<c:choose>
					  <c:when test="${not empty lobby.user1}">
				  		${lobby.user1}
					  </c:when>
					  <c:otherwise>
					  	empty slot
					  </c:otherwise>
					</c:choose>
			  		VS
					<img src="resources/images/avatar.svg" class="img-circle" height="15" width="15" alt="Avatar">
					<c:choose>
					  <c:when test="${not empty lobby.user2}">
				  		${lobby.user2}
					  </c:when>
					  <c:otherwise>
					  	empty slot
					  </c:otherwise>
					</c:choose>
					<button type="submit" class="btn btn-primary">Join</button>
				</div>
			  </li>
			</ul>
		</c:forEach>
	</c:if>
</div>
</body>
</html>