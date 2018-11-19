<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Puzzle Game</title>
<meta charset="utf-8">

<%@include file="includes/includes.jsp"%>




<!-- link href="resources/css/reset.css" rel="stylesheet" type="text/css" />-->
<link href="resources/css/style.css" rel="stylesheet" type="text/css" />
<link href="resources/css/reset.css" rel="stylesheet" type="text/css" />
<script src="resources/js/modernizr.js"></script>
<!-- Modernizr -->
<script src="resources/js/main.js"></script>
<!-- Gem jQuery -->
<script type="text/javascript" src="resources/js/script.js"></script>





</head>
<body class="wsmenucontainer">

	<!--  COMMENTOOOOOOOOOOOOO
	<div class="container">
		<h3 align="center">Sign in</h3>
		<form class="form-horizontal" action="login">
			<div class="form-group">
				<label class="control-label col-sm-2" for="username">Username:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="username"
						placeholder="Enter username" name="username">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="pwd">Password:</label>
				<div class="col-sm-10">
					<input type="password" class="form-control" id="pwd"
						placeholder="Enter password" name="password">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Sign in</button>
				</div>
			</div>
		</form>

		<c:if test="${not empty loginFailed}">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<strong>${loginFailed}</strong>
			</div>

		</c:if>

	</div>
	<div class="container">
		<h3 align="center">Sign up</h3>
		<form class="form-horizontal" action="creationAccount">
			<div class="form-group">
				<label class="control-label col-sm-2" for="username">Username:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="username"
						placeholder="Enter username" name="username">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="pwd">Password:</label>
				<div class="col-sm-10">
					<input type="password" class="form-control" id="pwd"
						placeholder="Enter password" name="password">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Sign up</button>
				</div>
			</div>
		</form>

		<c:if test="${not empty creationFailed}">
			<div class="alert alert-danger alert-dismissible" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<strong>${creationFailed}</strong>
			</div>

		</c:if>
		<form class="form-horizontal" action="logout">
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Logout</button>
				</div>
			</div>
		</form>

	</div>  COMMENTOOOOOOOO -->



	<!--  nav class="navbar navbar-inverse navbar-fixed-top">  -->





	<div class="cd-user-modal">
		<!-- this is the entire modal form, including the background -->
		<div class="cd-user-modal-container">
			<!-- this is the container wrapper -->
			<ul class="cd-switcher" style="list-style: none;">
				<li><a href="#0">Sign in</a></li>
				<li><a href="#0">New account</a></li>
			</ul>

			<div id="cd-login">
				<!-- log in form -->

				<form id="login-form" class="cd-form" action="login">
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signup-username" type="text" placeholder="Username"
							name="username"> 
							<c:if test="${not empty loginFailed}">
						<span class="cd-error-message is-selected;">${loginFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="signin-password">Password</label>
						<input class="full-width has-padding has-border"
							id="signin-password" type="text" placeholder="Password"
							name="password"> <a href="#0" class="hide-password">Hide</a>
									<c:if test="${not empty loginFailed}">
						<span class="cd-error-message is-selected;">${loginFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<input class="full-width" type="submit" value="Login">
					</p>
				</form>

				<!-- <a href="#0" class="cd-close-form">Close</a> -->
			</div>
			<!-- cd-login -->

			<div id="cd-signup">
				<!-- sign up form -->
				<form id="reg_form" action="creationAccount" class="cd-form">
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signup-username" type="text" placeholder="Username"
							name="username"> 
							<c:if test="${not empty creationFailed}">
						<span class="cd-error-message">${creationFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="password">Password</label>
						<input class="full-width has-padding has-border"
							id="signup-password" type="text" placeholder="Password"
							name="password"> <a href="#0" class="hide-password">Hide</a>
							<c:if test="${not empty creationFailed}">
						<span class="cd-error-message">${creationFailed}</span>
						</c:if>
					</p>

					<p class="fieldset">
						<input class="full-width has-padding" type="submit"
							value="Create account">
					</p>
				</form>
			</div>
			<!-- cd-signup -->


			<a href="#0" class="cd-close-form">Close</a>
		</div>
		<!-- cd-user-modal-container -->
	</div>
	<!-- cd-user-modal -->
	<!-- Page Content -->

	<!-- /.row -->




	<!-- /.container -->



<script type="text/javascript">
	var creationFailed ="<%=session.getAttribute("creationFailed")%>";
</script>


</body>
</html>