
<form class="form-horizontal">
	<fieldset class="fieldset">
		<!-- Form Name -->
		<div class="box-title main-title">
			<h2 style="text-align: center;">Overview</h2>
		</div>
		<!-- Text  Firstname -->
		<div class="form-group" style="margin-top: 3%;">
			<label class="col-md-12 control-label" for="firstname-field">First
				Name</label>
			<div class="col-md-12" align="center">
				<label class="col-md-12 control-label" for="firstname-field"
					style="color: black;">${firstname}</label>
			</div>
		</div>
		<!-- Text  Lastname -->
		<div class="form-group" style="margin-top: 3%;">
			<label class="col-md-12 control-label" for="lastname-field">Last
				Name</label>
			<div class="col-md-12" align="center">
				<label class="col-md-12 control-label" for="lastname-field"
					style="color: black;">${lastname}</label>
			</div>
		</div>
		<!-- Text  Password -->
		<div class="form-group" style="margin-top: 3%;">
			<label class="col-md-6 control-label" for="password-field">Password</label>

			<div class="col-md-12" align="center">
				<label id="showPassLabel" class="col-md-12 control-label"
					for="firstname-field" style="color: black;"
					onmouseover="showPassword()" onmouseout="hidePassword()">
					*******</label> <input id="password" value="${password}" class="hidden">
			</div>
		</div>
	</fieldset>
</form>


<!-- <div class="row col-md-12" style="	border-bottom: 1px solid #e5e5e5; margin: 10px 0 10px;	padding-bottom: 3px;" > </div>  -->
