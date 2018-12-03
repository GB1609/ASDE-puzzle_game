<li class="list-group-item card-with-shadow lobby_row" style="display: none;" id="template">
	<div class="text-center" id="lobby_name_div"></div>
	<div id="id_lobby_div" class=" text-center">
		<img id="id_img_owner" src="" class="img-circle" height="64" width="64" alt="Avatar">
		<span id="id_owner_name"></span> <img
			style="max-height: 1cm; max-width: 1cm; margin-left: 0%;"
			src="resources/images/icons/vs.png"> <span id = "id_guest_name"></span> <img id="id_img_guest" src=""
			class="img-circle" height="64" width="64" alt="Avatar">
		<button id="join_btn" type="button" onclick=""
			class="btn btn-warning btn-lg float-right hidden-field" >Join</button>
		<!-- <input id="created_lobby" type="hidden" value="created" />  -->
		<button id="start_button" type="button" onclick="startGame()"
			class="btn btn-success btn-lg float-right hidden-field">Start</button>
		<button id="leave_btn" type="button" onclick=""
			class="btn btn-danger btn-lg float-right hidden-field ">Leave</button>
		<div id="join_alert" class="alert alert-info hidden-field" 
			role="alert">A player joined to lobby</div>
		<div id="leave_alert" class="alert alert-danger hidden-field"
			role="alert">The player leaved the lobby</div>
		<form style="display: hidden;" action="forward_to_game" method="post"
			id="ftg_form">
			<!-- <input type="hidden" id="lobby_name" name="lobby_name" value=""> -->
		</form>
	</div>
</li>