<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<li class="list-group-item col-md-12" id="lobby_row"
	style="display: inline; margin-bottom: 0%; margin-top: 0%; background-color: rgba(0, 0, 0, 0.5); border-color: #e0a800;">
	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12"
			style="margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="margin-left: 6%;"> 
				<c:forEach items="${match.users}" var="user" varStatus="loop">
					<c:if test="${loop.index=='0'}">${user.username}</c:if>
				</c:forEach>
			</label>
			<div class="col-md-1" style="display: inline;">
				<img style="max-height: 1cm; max-width: 1cm; margin-left: 0%;"
					src="resources/img/vs.png">
			</div>
			<label class="col-md-3 control-label" for="firstname-field">
				<c:forEach items="${match.users}" var="user" varStatus="loop">
					<c:if test="${loop.index=='1'}">${user.username}</c:if>
				</c:forEach>
			</label>
		</div>
	</fieldset>
	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12"
			style="padding-left: 21%; margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="color: #e0a800;"> Winner:</label> <label
				class="col-md-3 control-label" for="firstname-field">
				${match.winner.username} </label>
		</div>
	</fieldset>




	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12"
			style="padding-left: 10%; margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-6 control-label" for="firstname-field"
				style="color: #e0a800; padding-right: 0%; margin-right: -7%; margin-left: -3%">
				Match's time:</label> <label class="col-md-4 control-label"
				for="firstname-field" style="padding-left: 0%">${fn:substringBefore(match.time div 60,'.')}
				m : ${match.time % 60} s </label>

		</div>
	</fieldset>
</li>