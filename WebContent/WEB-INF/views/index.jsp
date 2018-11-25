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
</head>

<body class="wsmenucontainer">
	<div class="cd-user-modal">
		<!-- this is the entire modal form, including the background -->
		<div class="cd-user-modal-container">
			<!-- this is the container wrapper -->
			<ul class="cd-switcher" style="list-style: none;">
				<li><a style="color: black; font-size: 20px;" href="#0">Sign
						in</a></li>
				<li><a style="color: black; font-size: 20px;" href="#0">New
						account</a></li>
			</ul>

			<input id=failed value="${creationFailed}" style="display: none">
			<input id=failedLogin value="${loginFailed}" style="display: none">
			<div id="cd-login" style="background: rgba(0, 0, 0, 0.45);">
				<!-- log in form -->

				<form id="login-form" class="cd-form" action="login" method="post"
					onsubmit="return checkSigninFields();">
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signin-username" type="text" placeholder="Username"
							name="username"> <span class="cd-error-message"></span>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="signin-password">Password</label>
						<input class="full-width has-padding has-border"
							id="signin-password" type="password" placeholder="Password"
							name="password"> <a href="#0" class="hide-password">Show</a>
						<span class="cd-error-message"></span>
					</p>
					<p class="fieldset">
						<input class="full-width" type="submit" value="Login"
							style="color: black;">
					</p>
				</form>

				<!-- <a href="#0" class="cd-close-form">Close</a> -->
			</div>
			<!-- cd-login -->





			<div id="cd-signup" style="background: rgba(0, 0, 0, 0.45);">
				<!-- sign up form -->
				<form id="reg_form" action="creationAccount" class="cd-form"
					onsubmit="return checkSignupFields();" method="post">
					<p class="fieldset">
						<label class="image-replace cd-username" for="firstName">First
							Name</label> <input class="full-width has-padding has-border"
							id="signup-firstname" type="text" placeholder="First Name"
							name="firstName"> <span class="cd-error-message"></span>

					</p>
					<p class="fieldset">
						<label class="image-replace cd-username" for="lastName">Last
							Name</label> <input class="full-width has-padding has-border"
							id="signup-lastname" type="text" placeholder="Last Name"
							name="lastName"> <span class="cd-error-message"></span>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-username" for="username">Username</label>
						<input class="full-width has-padding has-border"
							id="signup-username" type="text" placeholder="Username"
							name="username"> <span class="cd-error-message"></span>
					</p>
					<p class="fieldset">
						<label class="image-replace cd-password" for="password">Password</label>
						<input class="full-width has-padding has-border"
							id="signup-password" type="password" placeholder="Password"
							name="password"> <a href="#0" class="hide-password">
							Show</a> <span class="cd-error-message"></span>
					</p>

					<p class="fieldset">
						<input id="avatar" name="avatar" value="avatar.svg"
							style="display: none">
					</p>
					<p class="fieldset">
						<input class="full-width has-padding" type="button"
							value="Choose avatar" onclick="openAvatarGrid()" style="color: black;">
					</p>
					<p class="fieldset">
						<input class="full-width has-padding" type="submit"
							value="Create account" style="color: black;">
					</p>
				</form>
			</div>
			<!-- cd-signup -->

			<!-- Image selection  on user creation-->

			<div id="imageSection" class="row col-md-12"
				style="margin-left: 0em;">
				<div class="row" style="padding-left: 0.1em">
					<div class="avatar_board">
						<div class="avatar_grid">
							<c:forEach items="${avatars}" var="avatar">
								<p class="box_avatar">
									<img onclick="selectAvatar(this)" id="${avatar}" class="avatar"
										src="resources/images/avatars/${avatar}" />
								</p>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>

			<!-- Image selection  on user creation-->

			<!--  <a href="#0" class="cd-close-form">Close</a>-->
		</div>
		<!-- cd-user-modal-container -->
	</div>
	<!-- cd-user-modal -->




</body>
</html>