
<div id="imageSection" class="row col-md-12"
	style="margin-left: -1.9em;">
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
