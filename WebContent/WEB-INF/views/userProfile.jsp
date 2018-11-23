<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<link href="resources/css/userProfile.css" rel="stylesheet"
	type="text/css" />
<%@include file="includes/includes.jsp"%>
<script src="resources/js/userProfile.js"></script>



<meta charset="UTF-8">
<title>User Profile</title>
</head>
<body class="wsmenucontainer">
	<%@include file="includes/navbar.jsp"%>
	<div class="container ">
		<div class="row" style="margin-bottom: 0%"></div>
		<div class="row profile">
			<div class="col-md-2"></div>
			<div class="col-md-3">
				<div class="profile-sidebar sidebar-container">
					<!-- SIDEBAR USERPIC -->
					<div class="profile-userpic">
						<img src="resources/images/avatar.svg" class="img-responsive"
							alt="">
					</div>
					<!-- END SIDEBAR USERPIC -->
					<!-- SIDEBAR USER TITLE -->
					<div class="profile-usertitle">
						<div class="profile-usertitle-name">${username}</div>
					</div>


					<div class="profile-usermenu">
						<ul class="nav">
							<li id="overview-button" class="active"><a href="#"> <i
									class="glyphicon glyphicon-home"></i> Overview
							</a></li>
						</ul>
					</div>

					<div class="profile-usermenu">
						<ul class="nav">
							<li id="settings-button"><a href="#"> <i
									class="glyphicon glyphicon-cog"></i>Settings
							</a></li>
						</ul>
					</div>

					<div class="profile-usermenu">
						<ul class="nav">
							<li class="matchHistory-button"><a href="#"> <i
									class="glyphicon glyphicon-cog"></i>Match-History
							</a></li>
						</ul>
					</div>

					<!-- END MENU -->
				</div>
			</div>
			<div class="col-md-5">
				<div class="profile-content settings-container">
					<div id="overview-div">
						<%@include file="template/profileOverview.jsp"%>
					</div>
					<div id="settings-div" class="hidden">
						<%@include file="template/settings.jsp"%>
					</div>

					<div id="matchHistory-div" class="hidden">
						<div class="box-title main-title"
							style="border-bottom-style: none;">
							<h2 style="text-align: center;">Matches History</h2>
						</div>
						<c:choose>
							<c:when test="${not empty matches}">
								<ul class="list-group"
									style="overflow: auto; max-height: 23.8em;">
									<c:forEach items="${matches}" var="match">
										<%@include file="template/matchItem.jsp"%>
									</c:forEach>
								</ul>
							</c:when>
							<c:otherwise>
								<h2 style="text-align: center; font-size: 20px;">No matches found</h2>
							</c:otherwise>
						</c:choose>
					</div>
					<!-- overview content end -->
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
	<br>
	<br>
</body>
</html>