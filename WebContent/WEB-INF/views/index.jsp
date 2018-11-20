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
	<div class="cd-user-modal">
		<!-- this is the entire modal form, including the background -->
		<div class="cd-user-modal-container">
			<!-- this is the container wrapper -->
			<ul class="cd-switcher" style="list-style: none;">
				<li><a href="#0">Sign in</a></li>
				<li><a href="#0">New account</a></li>
			</ul>

<input id=failed value="${creationFailed}" style="display: none">
			<div id="cd-login">
				<!-- log in form -->

				<form id="login-form" class="cd-form" action="login">
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signin-username" type="text" placeholder="Username"
							name="username"> 
						<c:if test="${not empty loginFailed}">
						<span class="cd-error-message is-selected;">${loginFailed}</span>
						</c:if>
						<c:if test="${not empty loginFailed1}">
						<span class="cd-error-message is-selected;">${loginFailed1}</span>
						</c:if>
						
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="signin-password">Password</label>
						<input class="full-width has-padding has-border"
							id="signin-password" type="text" placeholder="Password"
							name="password"> <a href="#0" class="hide-password">Hide</a>
						<!--<c:if test="${not empty loginFailed}">
						<span class="cd-error-message is-selected;">${loginFailed}</span>
						</c:if> -->
						<c:if test="${not empty loginFailed2}">
						<span class="cd-error-message is-selected;">${loginFailed2}</span>
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
						<label class="image-replace cd-username" for="firstName">First Name</label>
						<input class="full-width has-padding has-border"
							id="signup-username" type="text" placeholder="First Name"
							name="firstName"> 
							<c:if test="${not empty creationFailedValue1}">
						<span class="cd-error-message">${creationFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-username" for="lastName">Last Name</label>
						<input class="full-width has-padding has-border"
							id="signup-lastname" type="text" placeholder="Last Name"
							name="lastName"> 
							<c:if test="${not empty creationFailedValue2}">
						<span class="cd-error-message">${creationFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signup-username" type="text" placeholder="Username"
							name="username"> 
							<c:if test="${not empty creationFailedValue3}">
						<span class="cd-error-message">${creationFailed}</span>
						</c:if>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="password">Password</label>
						<input class="full-width has-padding has-border"
							id="signup-password" type="text" placeholder="Password"
							name="password"> 
							<a href="#0" class="hide-password">
							Hide</a>
							<c:if test="${not empty creationFailedValue4}">
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


		<!--  <a href="#0" class="cd-close-form">Close</a>-->	
		</div>
		<!-- cd-user-modal-container -->
	</div>
	<!-- cd-user-modal -->




</body>
</html>