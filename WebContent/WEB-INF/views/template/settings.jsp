<form class="form-horizontal">
	<fieldset class="fieldset">
		<!-- Form Name -->
		<div class="box-title main-title">
			<h2 style="text-align: center;">Account Settings</h2>
		</div>
		<!-- Text input Username -->
		<div class="form-group" style="margin-top: 3%;">
			<label class="col-md-12 control-label form-valid" for="firstname-field">First
				Name</label>
			<div class="col-md-12">
				<input id="firstname-field" name="textinput" type="text"
					placeholder="New First Name" class="form-control input-md">
			</div>
		</div>
		<!-- Text input email-->
		<div class="form-group">
			<label class="col-md-12 control-label" for="lastname-field">Last
				Name</label>
			<div class="col-md-12">
				<input id="lastname-field" name="lastname" type="text"
					placeholder="New Last Name" class="form-control input-md">
			</div>
		</div>
		<!-- Text input Name -->

		<div class="form-group">
			<label class="col-md-12 control-label" for="nome-field">Password</label>
			<div class="col-md-12">
				<input id="password-field" type="password" autocomplete="off" placeholder="New Password"
					class="form-control input-md" onkeyup="checkPasswordField()">
			</div>
		</div>

		<div class="form-group hidden" id="reenter-password-div">
			<label class="col-md-12 control-label" for="nome-field">Re-enter
				Password</label>
			<div class="col-md-12">
				<input id="reenter-password-field" autocomplete="off" type="password"
					placeholder="Re-enter New Password" class="form-control input-md"
					onkeyup="checkPasswordField()">
			</div>
		</div>

		<div class="profile-usermenu">
			<ul class="nav">
				<li id="overview-button" class="active"><a href="#" onclick="updateSettings(this)" > <i
						class="glyphicon glyphicon-home"></i> Update information
				</a></li>

			</ul>
		</div>
	</fieldset>
</form>