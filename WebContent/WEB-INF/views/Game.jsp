<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@include file="includes/includes.jsp" %>

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
<title>Insert title here</title>

<script type="text/javascript">
$(document).ready(function(){
	 //$('#base').addClass($('#base').attr('value'));
	});
</script>
</head>
<body class="wsmenucontainer">
<%@include file="includes/navbar.jsp" %>
<div id="view" class="row justify-content-center">
      <div class="board col-md-auto">
        <div id="base" class="myGrid ${randomGrid.difficulty}">
         <c:forEach items="${randomGrid.nameImages}" var="piece">
         <span class="box_piece"><img id="${piece}" class="piece" src="resources/images/${randomGrid.subjectName}/${randomGrid.difficulty}/${piece }.png " /></span>
         </c:forEach>
        </div>
      </div>
      <div class="board col-md-auto offset-md-1">
        <div id="" class="myGrid cinque">
          <span class="box_piece"></span>
	 <span class="box_piece"></span>
          <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
	 <span class="box_piece"></span>
        </div>
      </div>
    </div>


</body>
</html>