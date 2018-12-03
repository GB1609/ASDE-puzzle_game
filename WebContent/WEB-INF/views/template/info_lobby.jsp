
<div class="modal fade" id="info-lobby">
	<div class="modal-dialog">
		<div class="modal-content"
			style="background-color: #111314; border-style: solid; border-width: 4px; border-color: black; border-radius: 10%; margin-top: 20%;">
			<div class="modal-header">
				<h3 class="modal-title" style="color: white">Your lobby is full</h3>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-header" style="border-color: #FFF907;">
				<div class="form-group ">
					<label for="lobby_name_text"
						style="color: white; justify-content: center;">Now you can
						start your game, press "GO" to go to your lobby, ignore otherwise.
						<br>
					</label>
				</div>
			</div>
			<div class="modal-footer" style="justify-content: center; border-color: #FFF907;">
				<button type="button" onclick="goToLobby()"
					class="btn btn-warning btn-sm">Go to Lobby</button>
				<button type="button" data-dismiss="modal"
					class="btn btn-warning btn-sm">Ignore</button>
			</div>
		</div>
	</div>
</div>