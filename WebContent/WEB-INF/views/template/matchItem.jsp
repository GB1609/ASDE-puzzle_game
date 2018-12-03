<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<li class="list-group-item row" id="lobby_row"
	style="display: inline; margin-bottom: 0%; margin-top: 0%; background-color: rgba(0, 0, 0, 0.5); border-color: #e0a800;">
	<fieldset class="fieldset row">
		<div class="form-group row"
			style="margin-bottom: 0%; margin-top: 0%; width:100%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="margin-left: 6%; padding-left:4em; padding-right:3em;"> <c:forEach items="${match.users}"
					var="user" varStatus="loop">
					<c:if test="${loop.index=='0'}">${user.username}</c:if>
				</c:forEach>
			</label>
			<div class="col-md-1" style="display: inline;">
				<img style="max-height: 1cm; max-width: 1cm; margin-left: 0%;"
					src="resources/images/icons/vs.png">
			</div>
			<label class="col-md-3 control-label" for="firstname-field" style="padding-left:4em; padding-right:3em;">
				<c:forEach items="${match.users}" var="user" varStatus="loop">
					<c:if test="${loop.index=='1'}">${user.username}</c:if>
				</c:forEach>
			</label>
		</div>
	</fieldset>
	<fieldset class="fieldset row">
		<div class="form-group row"
			style="padding-left: 21%; margin-bottom: 0%; margin-top: 0%; width:100%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="color: #e0a800;"> Winner:</label> <label style="margin-left:2em;"
				class="col-md-3 control-label" for="firstname-field">
				${match.winner.username} </label>
		</div>
	</fieldset>




	<fieldset class="fieldset row">
		<div class="form-group row"
			style="padding-left: 10%; margin-bottom: 0%; margin-top: 0%; width:100%">
			<label class="col-md-6 control-label" for="firstname-field"
				style="color: #e0a800; padding-right: 0%; margin-right: -7%; margin-left: -3%">
				Match's time:</label> <label class="col-md-4 control-label"
				for="firstname-field" style="padding-left: 0%">${match.time }</label>

		</div>
	</fieldset>
</li>