<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

#view {
  min-height: 100vh;
  padding: 1em;
}
.soil {
  display: grid;
  grid-template-columns: 20% 20% 20% 20% 20%;
  grid-template-rows: 20% 20% 20% 20% 20%;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.board{
position: relative;
  width: 50vw;
  height: 50vw;
  min-width: 300px;
  min-height: 300px;
  overflow: hidden;
  border: 12px solid #a2b9bc;
  border-radius: 12px;
	border-width:5px;  
    border-style:outset;
}
.box_piece{
	background: none;
	box-shadow: inset 2px 2px 0 rgba(255, 255, 255, 0.05), inset -2px -2px 0 #d5e1df;
}
.piece{
	max-width:100%;
	max-height:100%;
}
img:hover {
  background-color: #C37500;
  opacity:0.7 !important;
  filter:alpha(opacity=70) !important;
  box-shadow: 0 0 0px #000000 !important;
}
</style>
</head>
<body>

<section id="view">
      <div class="board">
        <div id="" class="soil">
          <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
          <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
	 <span class="box_piece"><img class="piece" src="resources/prova.jpg" /></span>
        </div>
      </div>
    </section>


</body>
</html>