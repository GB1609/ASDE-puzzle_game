<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<li class="list-group-item col-md-12" id="lobby_row"
	style="display: inline; margin-bottom: 0%; margin-top: 0%">
	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12"
			style="margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="margin-left: 6%;"><c:choose>
					<c:when test="${not empty match.user1}">${match.user1}</c:when>
					<c:otherwise>empty</c:otherwise>
				</c:choose> </label>
			<div class="col-md-1" style="display: inline;">
				<img style="max-height: 1cm; max-width: 1cm; margin-left: 0%;"
					src="resources/img/vs.jpg">
			</div>
			<label class="col-md-3 control-label" for="firstname-field"><c:choose>
					<c:when test="${not empty match.user1}">${match.user2}</c:when>
					<c:otherwise>empty</c:otherwise>
				</c:choose> </label>
		</div>
	</fieldset>
	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12"
			style="padding-left: 21%; margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-3 control-label" for="firstname-field"
				style="color: black;"> Winner:</label> <label
				class="col-md-3 control-label" for="firstname-field"> <c:choose>
					<c:when test="${match.winner == '0'}">
				 ${match.user1 }
			</c:when>
					<c:otherwise>${match.user2}</c:otherwise>
				</c:choose>
			</label>
		</div>
	</fieldset>
	
	

	
	<fieldset class="fieldset col-md-12">
		<div class="form-group col-md-12" style="padding-left: 10%; margin-bottom: 0%; margin-top: 0%">
			<label class="col-md-6 control-label" for="firstname-field"
				style="color: black; padding-right: 0%;margin-right: -7%; margin-left: -3%"> Match's time:</label> <label
				class="col-md-4 control-label" for="firstname-field"
				style="padding-left: 0%">${fn:substringBefore(match.time div 60,'.')} m : ${match.time % 60} s </label>
				
		</div>
	</fieldset>
</li>