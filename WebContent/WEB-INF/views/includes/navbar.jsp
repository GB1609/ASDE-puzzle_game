<nav class="navbar navbar-expand-lg navbar-dark myNav no_margin"
	style="background-color: rgba(0, 0, 0, 0.3);">
	<a class="navbar-brand" href="#"> <img alt="Logo"
		src="resources/images/logo.png" style="height: inherit;"></a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarNavDropdown">
		<ul class="navbar-nav">
			<li class="nav-item"><a class="nav-link" href="lobby">Home<span
					class="sr-only">(current)</span>
			</a></li>
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#"
				id="navbarDropdownMenuLink" role="button" data-toggle="dropdown"
				aria-haspopup="true" aria-expanded="false"> User</a>
				<div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
					<a class="dropdown-item" href="userProfile">View Profile</a> <a
						class="dropdown-item" href="logout">Logout</a>
				</div></li>

		</ul>
		<div class="ml-auto" style="max-width: 64px;align-self: center;"><img
			src="${avatar}" height="80%" width="80%" alt="Avatar"></div>
	</div>
</nav>
